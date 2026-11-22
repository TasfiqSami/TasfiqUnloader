# Changelog

All notable changes to this project are documented here. The format follows
[Keep a Changelog](https://keepachangelog.com/en/1.1.0/) and the project uses
[Semantic Versioning](https://semver.org/).

## [3.0.0] - 2026-11-23

### Added
- One jar for every server from Minecraft 1.8 to 26.x on Java 8 to 25. It is built as Java 8
  bytecode against the Bukkit 1.8.8 API, and CI loads it on JDK 8, 11, 17, 21 and 25.
- Maven build (`mvn package`) with a tag-triggered release workflow that publishes the jar,
  a SHA-256 checksum and a GitHub Packages artifact.
- `/hibernate status` and `/hibernate reload`, with tab completion and a `/tasfiqunloader` alias.
- Config options `gcAfterUnload` and `unloadIntervalSeconds`.
- Log message when the plugin is ready.

### Changed
- Project owner and package are now `io.github.tasfiqsami.tasfiqunloader`.
- The decompiled, obfuscated sources were replaced with readable code.
- `unloadChunks` now controls chunk unloading while the server is empty. It was only applied once at
  startup before. If a blacklisted plugin disables hibernation, chunks are unloaded on a timer instead.
- Blacklist URLs are downloaded off the main thread, with timeouts.
- `plugin.yml` now declares `api-version: 1.13`, so modern servers no longer load it as a legacy plugin.
- README rewritten.

### Fixed
- `plugin.yml` pointed to a class in a package with the wrong letter case, so the plugin could not load.
- `/hibernate` never worked: the executor compared the command name to `TasfiqUnloader`.
- `/hibernate` with extra arguments toggled the plugin instead of showing usage.
- A blacklist download leaked its connection and could hang the server on startup.

## [2.1.0]

Last release before the rewrite.
