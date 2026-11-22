<div id="top"></div>

<div align="center">
  <a href="https://github.com/TasfiqSami/TasfiqUnloader/">
    <img src="img/logo.png" alt="Logo" width="80" height="80">
  </a>

  <h3 align="center">Tasfiq Unloader</h3>

  <p align="center">
    Hibernates your Minecraft server and unloads chunks while nobody is online.
    <br />
    One jar for <strong>Minecraft 1.8 to 26.x</strong> on <strong>Java 8 to 25</strong>.
  </p>

  <p align="center">
    <a href="https://github.com/TasfiqSami/TasfiqUnloader/actions/workflows/ci.yml"><img src="https://github.com/TasfiqSami/TasfiqUnloader/actions/workflows/ci.yml/badge.svg" alt="CI"></a>
    <a href="https://github.com/TasfiqSami/TasfiqUnloader/releases/latest"><img src="https://img.shields.io/github/v/release/TasfiqSami/TasfiqUnloader" alt="Latest release"></a>
    <a href="https://github.com/TasfiqSami/TasfiqUnloader/releases"><img src="https://img.shields.io/github/downloads/TasfiqSami/TasfiqUnloader/total" alt="Downloads"></a>
    <a href="https://github.com/TasfiqSami/TasfiqUnloader/stargazers"><img src="https://img.shields.io/github/stars/TasfiqSami/TasfiqUnloader" alt="Stars"></a>
    <a href="https://github.com/TasfiqSami/TasfiqUnloader/issues"><img src="https://img.shields.io/github/issues/TasfiqSami/TasfiqUnloader" alt="Issues"></a>
  </p>

  <p align="center">
    <a href="https://github.com/TasfiqSami/TasfiqUnloader/releases/latest"><strong>Download</strong></a>
    ·
    <a href="https://github.com/TasfiqSami/TasfiqUnloader/issues/new/choose">Report a bug</a>
    ·
    <a href="https://github.com/TasfiqSami/TasfiqUnloader/issues/new/choose">Request a feature</a>
    ·
    <a href="CHANGELOG.md">Changelog</a>
  </p>
</div>

<details>
  <summary>Table of Contents</summary>
  <ol>
    <li><a href="#about">About</a></li>
    <li><a href="#compatibility">Compatibility</a></li>
    <li><a href="#installation">Installation</a></li>
    <li><a href="#configuration">Configuration</a></li>
    <li><a href="#commands--permissions">Commands &amp; Permissions</a></li>
    <li><a href="#caveats">Caveats</a></li>
    <li><a href="#building-from-source">Building from source</a></li>
    <li><a href="#releases">Releases</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>

## About

[![Title][product-screenshot]](#about)

TasfiqUnloader drops CPU usage to about 0-1% while no players are online. When the last player leaves, the plugin saves all worlds and then intentionally "freezes" the server's tick loop until someone joins again. While frozen it also unloads loaded chunks, so memory is released too.

*It is only useful for small servers that tend to be empty at night or during school days. If you have 500 players, it will never kick in.*

### Features

* Save electricity if you host the server yourself
* Let other software use the CPU when no one is playing
* Run more servers on one machine
* No effect at all while players are online
* One jar for every version, with nothing else to install

### Server with no players online
<img src="img/example1.png" alt="Server without TasfiqUnloader" width="626" height="23">

### Server with no players online and TasfiqUnloader installed
<img src="img/example2.png" alt="Server with TasfiqUnloader" width="624" height="24">

> **Paper 1.21.2+ users:** Paper has a built-in `pause-when-empty-seconds` option (see its global config) that does a similar job natively. TasfiqUnloader remains useful on every other version, and when you also want the chunk unloading and the plugin blacklist.

<p align="right">(<a href="#top">back to top</a>)</p>

## Compatibility

| | Supported |
| --- | --- |
| **Minecraft** | 1.8 to 26.x |
| **Java** | 8 to 25 |
| **Server software** | Spigot, Paper (and forks such as Purpur), CraftBukkit |
| **Not supported** | Folia (its regionized threading cannot be frozen this way) |

The jar is compiled as Java 8 bytecode against the Bukkit 1.8.8 API, so it can only use calls that exist on every supported version. Every build is checked in CI to be pure Java 8 bytecode and is loaded and verified on JDK 8, 11, 17, 21 and 25. A separate workflow boots real servers from 1.8.8 to the latest release with the plugin installed.

Use the Java version your Minecraft version needs (for example Java 8 for 1.8 to 1.16, Java 17 for 1.18, Java 21 for 1.20.5+, Java 25 for 26.1+). The plugin runs on all of them.

<p align="right">(<a href="#top">back to top</a>)</p>

## Installation

1. Download `TasfiqUnloader-<version>.jar` from the [latest release](https://github.com/TasfiqSami/TasfiqUnloader/releases/latest).
2. Put it in your server's `plugins` folder.
3. Restart the server. That's it.

Each release also has a `.sha256` file to check the download.

## Configuration

`plugins/TasfiqUnloader/config.yml`:

| Key | Default | Description |
| --- | --- | --- |
| `enabled` | `true` | Whether hibernation is active at startup. |
| `sleepMillis` | `1000` | How long the server sleeps per tick while empty (1-10000). Keep it well below your server's watchdog timeout. |
| `unloadChunks` | `true` | Unload loaded chunks while nobody is online. |
| `gcAfterUnload` | `true` | Run the garbage collector after chunks were unloaded. |
| `unloadIntervalSeconds` | `30` | Only used when hibernation is disabled by the blacklist: how often chunks are unloaded while empty. |
| `blacklist` | `[]` | Plugin names (case-insensitive) that break while the server is frozen. If any is installed, hibernation stays off and only chunk unloading runs. An entry starting with `http://` or `https://` is downloaded as a list with one plugin name per line (`#` for comments). |

Use `/hibernate reload` to apply changes.

## Commands & Permissions

| Command | Description |
| --- | --- |
| `/hibernate` or `/hibernate toggle` | Turn hibernation off or on again. Not saved over a reload or restart. |
| `/hibernate status` | Show whether the plugin is enabled and whether the server is currently sleeping. |
| `/hibernate reload` | Reload `config.yml`. |

`/tasfiqunloader` works as an alias. All subcommands need the permission `tasfiqunloader.toggle` (OP by default). Toggling is useful when you need to generate a world or run a task while nobody is online.

## Caveats

* The first player to join while the server is hibernating might have to try to connect twice. In most cases this is not needed, but it is possible.
* Since the server is frozen, plugins like Dynmap stop working while nobody is online (for Dynmap an advanced workaround is to use your own web server instead of the built-in one).
* The plugin won't help if your server is never empty.
* "Can't keep up!" messages might show up in your console after hibernation.
* While the server is hibernating, your TPS goes down (intentionally). Plugins that monitor TPS may report issues during hibernation and for a few minutes after, because they use averages of the past X minutes.

<p align="right">(<a href="#top">back to top</a>)</p>

## Building from source

You need JDK 17 or newer (the build targets Java 8 bytecode with `--release 8`) and Maven.

```sh
git clone https://github.com/TasfiqSami/TasfiqUnloader.git
cd TasfiqUnloader
mvn package
```

The jar is written to `target/TasfiqUnloader-<version>.jar`.

### Use it as a dependency

Releases are also published to GitHub Packages (GitHub requires a token with `read:packages` to download from it):

```xml
<repositories>
  <repository>
    <id>github</id>
    <url>https://maven.pkg.github.com/TasfiqSami/TasfiqUnloader</url>
  </repository>
</repositories>

<dependency>
  <groupId>io.github.tasfiqsami</groupId>
  <artifactId>tasfiqunloader</artifactId>
  <version>3.0.0</version>
  <scope>provided</scope>
</dependency>
```

## Releases

Releases are built by GitHub Actions. To publish one:

1. Bump the version in `pom.xml` and add a section for it to `CHANGELOG.md`, then merge to `main`.
2. Publish a GitHub release whose tag is `v` plus that version, using the CHANGELOG section as the notes. In the browser use **Releases → Draft a new release**, or from a terminal: `gh release create v3.0.0 --title "TasfiqUnloader 3.0.0" --notes-file notes.md`.

Publishing the release starts the [Release workflow](.github/workflows/release.yml). It checks that the tag matches the pom, builds the jar, checks it is Java 8 bytecode, attaches the jar and its checksum to the release, and publishes the package. Run the [Server compatibility workflow](.github/workflows/compat.yml) from the Actions tab before a release to boot real servers with the new jar.

<p align="right">(<a href="#top">back to top</a>)</p>

## Contact

Tasfiq - [Discord](https://discord.gg/kKQd6PJe22) - tasfiq@programmer.net

Project Link: [https://github.com/TasfiqSami/TasfiqUnloader](https://github.com/TasfiqSami/TasfiqUnloader)

<p align="right">(<a href="#top">back to top</a>)</p>

[product-screenshot]: img/title.jpg
