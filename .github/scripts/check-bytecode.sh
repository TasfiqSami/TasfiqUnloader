#!/usr/bin/env bash
# Fails if any compiled class is newer than Java 8 (class file major version 52).
# That guarantees the jar loads on every JVM from Java 8 up.
set -euo pipefail

dir="${1:-target/classes}"
bad=0
count=0

while IFS= read -r -d '' file; do
  count=$((count + 1))
  major=$(javap -v "$file" | awk '/major version/ {print $3}')
  if [ "$major" != "52" ]; then
    echo "::error file=$file::class file major version is $major, expected 52 (Java 8)"
    bad=1
  fi
done < <(find "$dir" -name '*.class' -print0)

if [ "$count" -eq 0 ]; then
  echo "::error::no classes found in $dir"
  exit 1
fi

if [ "$bad" -ne 0 ]; then
  exit 1
fi
echo "All $count classes are Java 8 bytecode (major version 52)."
