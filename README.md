# CML - Conceptual Modeling Language

This repository contains the source code for the CML compiler.

## Branches

Master

![Build Status](https://travis-ci.org/cmlang/cml-compiler.svg?branch=master)

## Pre-requisites

### Homebrew / Linuxbrew

One of the following is required to install the CML compiler:
- Homebrew (on macOS - at https://brew.sh)
- Linuxbrew (on Linux - at http://linuxbrew.sh)

Please report an issue if you cannot get the CML compiler installed using Homebrew or Linuxbrew.

### Java 8

The only pre-requisite to run the CML compiler is Java 8.

The `cml` command should just work after installation if Java 8 is available on the environment.

Please report an issue, otherwise.

## Quick, Two-Step Install with Homebrew / Linuxbrew

```
$ brew tap cmlang/cml # To make the CML compiler packages available.
$ brew install cml-compiler@stable # To install the latest stable release.
```

## Release Notes

To read the release notes of a specific version, please go to: https://github.com/cmlang/cml-releases/tree/master/cml-compiler

Look for the files ending with '.zip.notes.md'. For example: cml-compiler-1.0.zip.notes.md

## Release Versioning

The release version format is YEAR.MONTH.DAY-CHANNEL, where:
- YEAR/MONTH/DAY: the year/month/day the version was released.
- CHANNEL: which channel the release was published in:
-- _stable_: backward-compatible, high-quality releases; low-risk upgrade.
-- _beta_: candidate releases with increasing quality/compatibility; medium-risk upgrade.
-- _alpha_: development releases; no quality/compatibility guarantees; high-risk upgrade.

All release channels may include bug fixes and new features.
We strive to keep releases backward-compatible and high-quality;
especially the stable releases.

## Installing Specific Version with Homebrew / Linuxbrew

### Finding Packages

Before you can install the CML compiler, please run:

```
$ brew tap cmlang/cml
```

After that, the CML compiler packages will be available to be installed.

### Finding Versions

To find the versions of the CML compiler available to install:

```
$ brew update # To fetch all available versions.
$ brew search cml-compiler # To list available versions.
```

To read the release notes of a specific version, please go to (look for the '.zip.notes.md' files): https://github.com/cmlang/cml-releases/tree/master/cml-compiler

### The Very Latest

If you'd like to always have the latest version of the CML compiler,
regardless of the channel where it was released:

```
$ brew update # To fetch all available versions.
$ brew install cml-compiler
```

Later on, to upgrade to the latest version:

```
$ brew update # To fetch all available versions.
$ brew upgrade cml-compiler
```

You may get a new stable version,
and thus backward-compatible/high-quality version,
but you may also get a beta or an alpha release.

### Specific Channel

If you'd like to get the latest release of a specific channel:

```
$ brew update # To fetch all available versions.
$ brew install cml-compiler@CHANNEL
```

Once you've installed it, you can upgrade to the latest release of the channel with:

```
$ brew update # To fetch all available versions.
$ brew upgrade cml-compiler@CHANNEL
```

### Specific Day Version

You can also get the release of a specific day:

```
$ brew update # To fetch all available versions.
$ brew install cml-compiler@YEAR.MONTH.DAY
```

### Specific Year Version

You can even get the latest release of a specific year:

```
$ brew update # To fetch all available versions.
$ brew install cml-compiler@YEAR
```

If you've installed the current year, you can upgrade to the latest release of the current year with:

```
$ brew update # To fetch all available versions.
$ brew upgrade cml-compiler@CURRENT_YEAR
```

## Contributing / Development

For information on helping us to develop CML, go to: [CONTRIBUTING.md](https://github.com/cmlang/cml-compiler/blob/master/CONTRIBUTING.md)
