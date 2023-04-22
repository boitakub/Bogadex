<h1 align="center">Bogadex
<p align="center">
<a href="https://github.com/boitakub/Bogadex"><img src="https://raw.githubusercontent.com/boitakub/Bogadex/main/app/src/main/res/mipmap-xxhdpi/ic_launcher_foreground.png" width="150" alt="Bogadex" /></a>
</p>
</h1>

[![Build](https://github.com/boitakub/Bogadex/actions/workflows/main.yml/badge.svg)](https://github.com/boitakub/Bogadex/actions/workflows/main.yml)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=boitakub_Bogadex&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=boitakub_Bogadex)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=boitakub_Bogadex&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=boitakub_Bogadex)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=boitakub_Bogadex&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=boitakub_Bogadex)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=boitakub_Bogadex&metric=bugs)](https://sonarcloud.io/summary/new_code?id=boitakub_Bogadex)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=boitakub_Bogadex&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=boitakub_Bogadex)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=boitakub_Bogadex&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=boitakub_Bogadex)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=boitakub_Bogadex&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=boitakub_Bogadex)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=boitakub_Bogadex&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=boitakub_Bogadex)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=boitakub_Bogadex&metric=coverage)](https://sonarcloud.io/summary/new_code?id=boitakub_Bogadex)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.8.10-blue.svg?style=flat)](https://kotlinlang.org)
[![API](https://img.shields.io/badge/API-23%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=23)
[![License](https://img.shields.io/github/license/boitakub/bogadex)](LICENSE.md)
[![gitmoji](https://img.shields.io/badge/gitmoji-%20😜%20😍-FFDD67.svg)](https://github.com/carloscuesta/gitmoji)
[![Twitter](https://img.shields.io/badge/Twitter-@jforatier-blue.svg?style=flat)](http://twitter.com/jforatier)

<p align="center">
Bogadex is a small demo and functionnal application based on modern Android application tech-stacks and MVVM architecture.<br>This project aim to regroup and present most of current practices and patterns.<br>
Also dealing with data (from <a href="https://www.boardgamegeek.com/">BoardGameGeek</a>) and presenting them in elegants ways.
</p>
</br>

<p align="center">
<img src="/docs/assets/capture_1.gif" height="500" alt="Bogadex - Screenshot"/>
</p>

## Download 📲

Go to the [Releases](https://github.com/boitakub/Bogadex/releases) to download the latest APK.

## Features ✨

* Free and open source
* List, Sort and Filter all you [BGG BoardGameGeekCollection](https://boardgamegeek.com/)

## Tech Stack & Libraries 🧬

This project takes advantage of best practices, and many popular libraries and tools in the Android ecosystem.
- [Kotlin](https://kotlinlang.org/) - 100% Kotlin - Code and Scripts
- [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous.
- Lifecycle - dispose of observing data when lifecycle state changes.
- ViewModel - UI related data holder, lifecycle aware.
- [Hilt-Dagger](https://dagger.dev/hilt/) for dependency injection.
- [JetPack](https://developer.android.com/jetpack)
  * [Compose](https://developer.android.com/jetpack/compose?hl=fr) - UI build 100% with Jetpack Compose
  * [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager) - Updating and maintaining data up-to-date periodically and asynchronous
  * [DataStore](https://developer.android.com/topic/libraries/architecture/datastore) - for shared preferences
  * [Room](https://developer.android.com/training/data-storage/room) - Database for cache / offline storage

- [Material 3](https://m3.material.io/) - Using Material Design 3
  * [Dark Mode](https://developer.android.com/guide/topics/ui/look-and-feel/darktheme) - Generate theme trough [Material Theme Builder](https://m3.material.io/theme-builder#/dynamic)
- [Retrofit2 & OkHttp3](https://github.com/square/retrofit) - construct the REST APIs and paging network data.
- [Coil](https://github.com/coil-kt/coil) - Async images management.


- [SonarCloud](https://sonarcloud.io/project/overview?id=boitakub_Bogadex) - Code quality and coverage analysis
- [Gradle optimizations](https://proandroiddev.com/how-we-reduced-our-gradle-build-times-by-over-80-51f2b6d6b05b) - Some gradle tips to optimize build time

## Flow & Delivery 🚚

### Triggering a Release

Release a build version triggers in the following scenarios:
- You push a version tag to the repository.
- You create a pull request targeting the main branch.
- Check on pre-commit [Article](https://medium.com/@anjani.kjoshi/android-lint-pre-commit-hook-for-clean-code-747edfe57abf)
  * lint - for Android critical issues
  * KtLint - for kotlin file quality
- Auto version from git tag [Article](https://dev.to/ychescale9/git-based-android-app-versioning-with-agp-4-0-24ip) [Plugin](https://github.com/ReactiveCircus/app-versioning)
- Auto delivery from git tag
  * on GitHub Release
  * on Firebase App Distribution
  * 🚧 on Google Play Store - Beta & Production
- Release signing [Article](https://proandroiddev.com/how-to-securely-build-and-sign-your-android-app-with-github-actions-ad5323452ce)

## Code formatting

The CI uses [Spotless](https://github.com/diffplug/spotless) to check if your code is formatted correctly and contains the right licenses.
Internally, Spotless uses [ktlint](https://github.com/pinterest/ktlint) to check the formatting of your code.
To set up ktlint correctly with Android Studio, follow one of the [listed setup options](https://github.com/pinterest/ktlint#-with-intellij-idea).

Before committing your code, run `./gradlew app:spotlessApply` to automatically format your code.

<!--- ## MAD Score

![summary](https://user-images.githubusercontent.com/24237865/102366914-84f6b000-3ffc-11eb-8d49-b20694239782.png)

![kotlin](https://user-images.githubusercontent.com/24237865/102366932-8a53fa80-3ffc-11eb-8131-fd6745a6f079.png)

-->

## Architecture 📐

Bogadex is based on MVVM architecture and a repository pattern with a modular approach.
- MVVM -> MVI Architecture (View - ViewModel - Model)
- Business oriented with UseCases pattern - [By layer or feature? Why not both?! Guide to Android app modularization](https://www.youtube.com/watch?v=16SwTvzDO0A)
- Repository pattern
- UI 100% Jetpack compose

Concerns are separate by modules containing feature
![Design by features](docs/assets/design_by_feature.png)

Each modules/feature share a common clean architecture pattern
![Clean architecture](docs/assets/clean_architecture.png)

<!---

![architecture](https://user-images.githubusercontent.com/24237865/77502018-f7d36000-6e9c-11ea-92b0-1097240c8689.png)

-->

## Credits & Inspiration 🎖️

Bogadex is inspired by the wonderful repositories :

- [Jaewoong Eum](https://github.com/skydoves), especially [Pokedex](https://github.com/skydoves/Pokedex).
- [Igor Wojda](https://github.com/igorwojda) and his great repo [android-showcase](https://github.com/igorwojda/android-showcase).
- [Lopez Mikhael](https://github.com/lopspower) with [Pokedex Compose](https://github.com/lopspower/PokeCardCompose)

Thank you for the great job.

## BoardGameGeek API 🎲

<img src="https://images.squarespace-cdn.com/content/v1/5902292fd482e9284cf47b8d/1567633051478-PRQ3UHYD6YFJSP80U3YV/BGG.jpeg?format=1500w" align="right" width="21%"/>

Pokedex using the [BoardGameGeekAPI](https://boardgamegeek.com/wiki/page/BGG_XML_API2/) for constructing RESTful-oriented API.<br>
BoardGameGeekAPI provides a XML API interface to highly detailed objects built from thousands of lines of data related to Boardgames.
