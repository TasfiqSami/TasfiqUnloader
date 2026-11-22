# Security Policy

## Supported versions

Only the latest release gets fixes.

| Version | Supported |
| --- | --- |
| 3.x | Yes |
| 2.x and older | No |

## Reporting a vulnerability

Please **do not open a public issue** for a security problem.

* Use GitHub's private reporting: **Security → Report a vulnerability** on the
  [repository](https://github.com/TasfiqSami/TasfiqUnloader/security/advisories/new), or
* email [tasfiq@programmer.net](mailto:tasfiq@programmer.net) with the details.

Include the plugin version, your server software and version, and steps to reproduce.

You can expect a reply within a few days. Once a fix is released, the report will be credited in the
changelog unless you prefer to stay anonymous.

## Scope notes

* The only network access is the optional `blacklist` entry in `config.yml` that points to a URL. It is
  downloaded once on startup or reload, read as plain text, and never executed. Only use URLs you trust.
* The `/hibernate` command needs the `tasfiqunloader.toggle` permission (OP by default).
