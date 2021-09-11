<h1 align="center">Bogadex
<p align="center">
  <a href="https://github.com/boitakub/Bogadex"><img src="./app/src/main/res/mipmap-xxhdpi/ic_launcher_foreground.png" width="150" alt="Bogadex" /></a>
</p>
</h1>

<p align="center">
  <a href="https://github.com/boitakub/Bogadex/actions"><img alt="Build Status" src="https://github.com/boitakub/Bogadex/actions/workflows/main.yml/badge.svg"/></a> 
  <a href="https://github.com/marketplace/actions/super-linter"><img alt="Super-Linter" src="https://github.com/boitakub/Bogadex/workflows/Lint%20Code%20Base/badge.svg"/></a>
  <a href="https://www.codacy.com/gh/boitakub/Bogadex/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=boitakub/Bogadex&amp;utm_campaign=Badge_Grade"><img src="https://app.codacy.com/project/badge/Grade/92ff47411c2a4d65b4389f4924ea8f0d"/></a>
  <a href="https://www.codacy.com/gh/boitakub/Bogadex/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=boitakub/Bogadex&amp;utm_campaign=Badge_Coverage"><img src="https://app.codacy.com/project/badge/Coverage/92ff47411c2a4d65b4389f4924ea8f0d"/></a>
  <a href="https://android-arsenal.com/api?level=23"><img alt="API" src="https://img.shields.io/badge/API-23%2B-brightgreen.svg?style=flat"/></a>
  <a href="https://opensource.org/licenses/GPL-3.0"><img alt="License" src="https://img.shields.io/github/license/boitakub/bogadex"/></a>
  <a href="https://github.com/carloscuesta/gitmoji"><img alt="gitmoji badge" src="https://img.shields.io/badge/gitmoji-%20😜%20😍-FFDD67.svg"/></a>
</p>

<p align="center">  
Bogadex is a small demo and functionnal application based on modern Android application tech-stacks and MVVM architecture.<br>This project aim to regroup and present most of current practices and patterns.<br>
Also dealing with data (from <a href="https://www.boardgamegeek.com/">BoardGameGeek</a>) and presenting them in elegants ways.
</p>
</br>

<p align="center">
<img src="/previews/screenshot.png"/>
</p>

## Download 📲

Go to the [Releases](https://github.com/boitakub/Bogadex/releases) to download the latest APK.

## Features ✨

* Free and open source
* List all you BoardGameGeekCollection

## Tech stack & Open-source libraries 🧬

* Minimum SDK level 23
* [Kotlin](https://kotlinlang.org/) based, [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous.
* [Hilt](https://dagger.dev/hilt/) for dependency injection.
* [JetPack](https://developer.android.com/jetpack)
  + Lifecycle - dispose of observing data when lifecycle state changes.
  + ViewModel - UI related data holder, lifecycle aware.
  + Room - construct a database using the abstract layer.
  + Worker -
* Architecture
  + MVVM Architecture (View - DataBinding - ViewModel - Model)
  + [Bindables](https://github.com/skydoves/bindables) - Android DataBinding kit for notifying data changes to UI layers.
  + Business oriented with UseCases pattern
  + Repository pattern
* [Retrofit2 & OkHttp3](https://github.com/square/retrofit) - construct the REST APIs and paging network data.
* [Coil](https://github.com/coil-kt/coil) - loading images.
* [Material-Components](https://github.com/material-components/material-components-android) - Material design components like ripple animation, cardView.
* [Codacy](https://codacy.com/) - Code quality and coverage analysis

## Flow & Delivery 🚚

* Check on pre-commit [Article](https://medium.com/@anjani.kjoshi/android-lint-pre-commit-hook-for-clean-code-747edfe57abf)
  + lint - for Android critical issues
  + KtLint - for kotlin file quality
* Auto version from git tag [Article](https://dev.to/ychescale9/git-based-android-app-versioning-with-agp-4-0-24ip) [Plugin](https://github.com/ReactiveCircus/app-versioning)
* Auto delivery from git tag
  + on GitHub Release
  + on Firebase App Distribution
  + on Google Play Store - Beta & Production -

<!--- ## MAD Score

![summary](https://user-images.githubusercontent.com/24237865/102366914-84f6b000-3ffc-11eb-8d49-b20694239782.png)

![kotlin](https://user-images.githubusercontent.com/24237865/102366932-8a53fa80-3ffc-11eb-8131-fd6745a6f079.png)

 -->

## Architecture 📐

Bogadex is based on MVVM architecture and a repository pattern with a modular approach.

<!---  

![architecture](https://user-images.githubusercontent.com/24237865/77502018-f7d36000-6e9c-11ea-92b0-1097240c8689.png)

 -->

## Thanks & Praise 🎖️

Bogadex is inspired by the wonderful repositories of ⭐[skydoves](https://github.com/skydoves), especially [Pokedex](https://github.com/skydoves/Pokedex). Thank you for the great job.

## BoardGameGeek API 🎲

<img src="https://images.squarespace-cdn.com/content/v1/5902292fd482e9284cf47b8d/1567633051478-PRQ3UHYD6YFJSP80U3YV/BGG.jpeg?format=1500w" align="right" width="21%"/>

Pokedex using the [BoardGameGeekAPI](https://boardgamegeek.com/wiki/page/BGG_XML_API2/) for constructing RESTful-oriented API.<br>
BoardGameGeekAPI provides a XML API interface to highly detailed objects built from thousands of lines of data related to Boardgames.

## License ⚖️

Designed and developed by 2021 Boitakub (Julien Foratier)

This project is licensed under the GNU General Public License v3.0. See the
[LICENSE](LICENSE) file for details.
