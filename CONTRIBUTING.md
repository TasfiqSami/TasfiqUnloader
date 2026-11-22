# Contributing to TasfiqUnloader

Thanks for wanting to help! Bug reports, ideas and pull requests are all welcome. By taking part you
agree to follow the [Code of Conduct](CODE_OF_CONDUCT.md).

## Reporting bugs and asking for features

* Search the [existing issues](https://github.com/TasfiqSami/TasfiqUnloader/issues) first.
* Open a new issue with the [bug report or feature request form](https://github.com/TasfiqSami/TasfiqUnloader/issues/new/choose).
  For a bug, include your server software and Minecraft version, your Java version, your `config.yml`
  and the relevant part of `latest.log`.
* For questions and setup help, ask in [Discord](https://discord.gg/kKQd6PJe22) (see [SUPPORT](.github/SUPPORT.md)).
* Security problems go through [SECURITY.md](SECURITY.md), not a public issue.

## Development setup

You need **JDK 17 or newer** and **Maven**. The code itself targets Java 8.

```sh
git clone https://github.com/TasfiqSami/TasfiqUnloader.git
cd TasfiqUnloader
mvn verify
```

The jar is written to `target/TasfiqUnloader-<version>.jar`. Copy it into a test server's `plugins`
folder to try it.

## The compatibility rules

One jar has to work on every Minecraft version from 1.8 to the newest, and on every Java version
from 8 to 25. That gives a few hard rules, and CI enforces them:

* **Java 8 only.** The build uses `--release 8`, so Java 9+ APIs (`List.of`, `HttpClient`, `var`,
  records, text blocks, `String.isBlank`, ...) do not compile. Lambdas and streams are fine.
* **Only Bukkit 1.8.8 API.** The plugin compiles against `spigot-api 1.8.8`, so a call that does not
  exist there does not compile. Don't bump that dependency.
* **No new dependencies and no NMS or CraftBukkit internals.** Version-specific code breaks the "one jar" goal.
* **No Paper-only or Folia-only API.** Spigot, Paper and forks must all work.

If a feature really needs newer API, check for it at runtime (for example with reflection) and keep a
fallback for older versions. Mention it in the pull request.

## Making a change

1. Fork the repo and create a branch from `main`.
2. Make your change. Keep it focused, and match the existing style (4 spaces, see `.editorconfig`).
3. Run `mvn verify`.
4. Try it on a real server if you can, ideally an old one and a new one.
5. Update `README.md` and `src/main/resources/config.yml` if you changed behavior or options, and add a
   line under **Unreleased** in `CHANGELOG.md`.
6. Open a pull request using the template and describe what and why.

Commit messages: a short summary line in the imperative ("Fix blacklist download timeout"), then
details if needed.

## Credit

Everyone whose pull request is merged shows up in the
[contributors list](https://github.com/TasfiqSami/TasfiqUnloader/graphs/contributors) automatically, based
on the author of their commits. Make sure your git email is linked to your GitHub account.

## Releases

Releases are made by the maintainer. See the **Releases** section of the [README](README.md#releases).

## License

By contributing you agree that your contributions are licensed under the [MIT License](LICENSE).
