<div id="top"></div>
<!--
*** Thanks for checking out the Best-README-Template. If you have a suggestion
*** that would make this better, please fork the repo and create a pull request
*** or simply open an issue with the tag "enhancement".
*** Don't forget to give the project a star!
*** Thanks again! Now go create something AMAZING! :D
-->



<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->
[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![LinkedIn][linkedin-shield]][linkedin-url]



<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/TasfiqSami/TasfiqUnloader/">
    <img src="img/logo.png" alt="Logo" width="80" height="80">
  </a>

  <h3 align="center">Tasfiq Unloader</h3>

  <p align="center">
    An awesome minecraft plugin that unloads Loaded chunk in your minecraft server
    <br />
    <a href="https://github.com/TasfiqSami/TasfiqUnloader#readme"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/TasfiqSami/TasfiqUnloader">View Demo</a>
    ·
    <a href="https://github.com/TasfiqSami/TasfiqUnloader/issues">Report Bug</a>
    ·
    <a href="https://github.com/TasfiqSami/TasfiqUnloader/issues">Request Feature</a>
  </p>
</div>



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Plugint</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About This Plugin

[![Product Name Screen Shot][product-screenshot]](https://example.com)

This is a very simple plugin. It will reduce CPU usage to about 0-1% when no players are online on the server. This can be used for a lot of purposes.

*Note that this plugin will only be useful for small servers that tend to be empty at night or during schooldays. If you've got 500 players, the plugin will never "enable" and will not make any difference.*

Features:
* Save electricity (if you host the server)
* Let other software use the CPU when no one is playing Minecraft.
* Allow more servers to run on your machine
* Works for all versions of Craftbukkit or Spigot
* Very lightweight plugin. Does not cause any lag at all. :smile:

**How does this work?**
The plugin will intentionally "lag" your server while no players are connected. You can see it as freezing the server. It'll make it dormant until a player connects.

Command (since version 1.02)
You can use /hibernate to toggle the plugin off (and on again) this may be useful in situations where you have to generate a world while no players are online or something similar. Does not save over reload/restart! Enabled by default.
You have to be OP or have this permission to use the command:
`tasfiqunloader.toggle`

**Caveats:**
* The first player to join the server while it is hibernating might have to try to connect twice. In most cases this is not needed, but it is possible
* Since Hibernate "freezes" the server, plugins like Dynmap will stop working when no players are connected (for Dynmap specifically an advanced workaround could be to not use the built in webserver, but use your own)
* Plugin won't help if your server is never empty
* Because of the hibernation, can't keep up messages might show up in your console.
* # While the server is hibernating, your TPS will go down (intentionally). If you have plugins that monitor TPS, these might report issues during hibernation and for a few minutes after due to them using averages of the past X minutes.

<p align="right">(<a href="#top">back to top</a>)</p>



### Built With


* [Java](https://www.java.com/en/)

<p align="right">(<a href="#top">back to top</a>)</p>


<!-- GETTING STARTED -->
## Installation

Download this plugin from the [Releases](https://github.com/TasfiqSami/TasfiqUnloader/releases)

_After downloading it put it in your plugins folder and restart your server and you are done._

<p align="right">(<a href="#top">back to top</a>)</p>


<!-- CONTACT -->
## Contact

Tasfiq - [Discord](https://discord.gg/kKQd6PJe22) - tasfiq@programmer.net

Project Link: [https://github.com/TasfiqSami/TasfiqUnloader](https://github.com/TasfiqSami/TasfiqUnloader)

<p align="right">(<a href="#top">back to top</a>)</p>


<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/othneildrew/Best-README-Template.svg?style=for-the-badge
[contributors-url]: https://github.com/tasfiqsami/TasfiqUnloader/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/othneildrew/Best-README-Template.svg?style=for-the-badge
[forks-url]: https://github.com/tasfiqsami/TasfiqUnloader/network/members
[stars-shield]: https://img.shields.io/github/stars/othneildrew/Best-README-Template.svg?style=for-the-badge
[stars-url]: https://github.com/tasfiqsami/TasfiqUnloader/stargazers
[issues-shield]: https://img.shields.io/github/issues/othneildrew/Best-README-Template.svg?style=for-the-badge
[issues-url]: https://github.com/tasfiqsami/TasfiqUnloader/issues
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://bio.link/tasfiqah
[product-screenshot]: images/screenshot.png
