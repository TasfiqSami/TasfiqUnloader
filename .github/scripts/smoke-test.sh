#!/usr/bin/env bash
# Boots a real Paper server with the plugin installed and checks that it loads and that
# /hibernate works. Usage: smoke-test.sh <minecraft-version|latest> <path-to-plugin-jar>
set -euo pipefail

mc="$1"
jar="$(realpath "$2")"

if [ "$mc" = "latest" ]; then
  mc=$(curl -fsSL "https://fill.papermc.io/v3/projects/paper" \
    | jq -r '.versions[][]' | grep -v -- '-' | sort -V | tail -1)
  echo "Latest Paper version: $mc"
fi

work="$(mktemp -d)"
cd "$work"

echo "::group::Download Paper $mc"
url=$(curl -fsSL "https://fill.papermc.io/v3/projects/paper/versions/$mc/builds/latest" \
  | jq -r '.downloads["server:default"].url // empty' || true)
if [ -z "$url" ]; then
  echo "Not found in the v3 API, trying v2"
  build=$(curl -fsSL "https://api.papermc.io/v2/projects/paper/versions/$mc" | jq -r '.builds[-1]')
  url="https://api.papermc.io/v2/projects/paper/versions/$mc/builds/$build/downloads/paper-$mc-$build.jar"
fi
echo "$url"
curl -fsSL -o server.jar "$url"
echo "::endgroup::"

echo "eula=true" > eula.txt
cat > server.properties <<'EOF'
online-mode=false
server-port=25599
view-distance=3
max-tick-time=-1
spawn-monsters=false
EOF
mkdir -p plugins
cp "$jar" plugins/

mkfifo console
java -Xmx1G -jar server.jar nogui < console > server.log 2>&1 &
server_pid=$!
exec 3> console

fail() {
  echo "::error::$1"
  echo "::group::server.log"
  cat server.log
  echo "::endgroup::"
  kill "$server_pid" 2>/dev/null || true
  exit 1
}

# expect <regex> <seconds>
expect() {
  for _ in $(seq 1 "$2"); do
    if grep -Eq "$1" server.log; then return 0; fi
    if ! kill -0 "$server_pid" 2>/dev/null; then return 1; fi
    sleep 1
  done
  return 1
}

expect 'Done \(' 300 || fail "Server did not finish starting"
expect 'Ready\. The server will hibernate' 60 || fail "Plugin did not report ready"

echo "hibernate status" >&3
expect 'Plugin is currently enabled' 60 || fail "/hibernate status gave no answer"

echo "hibernate reload" >&3
expect 'Configuration reloaded' 60 || fail "/hibernate reload gave no answer"

echo "hibernate" >&3
expect 'Plugin is now disabled' 60 || fail "/hibernate toggle gave no answer"

echo "stop" >&3
for _ in $(seq 1 120); do
  kill -0 "$server_pid" 2>/dev/null || break
  sleep 1
done
kill -0 "$server_pid" 2>/dev/null && fail "Server did not stop"

if grep -Eiq "Could not load 'plugins/TasfiqUnloader|Error occurred while enabling TasfiqUnloader|Error occurred while disabling TasfiqUnloader" server.log; then
  fail "The plugin logged a load/enable/disable error"
fi

echo "Smoke test passed on Paper $mc"
