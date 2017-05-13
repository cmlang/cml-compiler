# CML - Conceptual Modeling Language

This repository contains the source code for the CML compiler.

## Branches

Master

![Build Status](https://travis-ci.org/cmlang/cml-compiler.svg?branch=master)

## Pre-requisites

### Homebrew / Linuxbrew

One of the following is required to install the CML compiler:
- Homebrew (on macOS - at https://brew.sh)
- or Linuxbrew (on Linux - at http://linuxbrew.sh)

NOTE for Linux users: We have not yet tested the installation on Linux,
but we expect you should be able to use Linuxbrew on Linux
when following the installation instructions below.

Please report an issue if you cannot get the CML compiler installed using Homebrew or Linuxbrew.

### Java 8

The only pre-requisite to run the CML compiler is Java 8.

The `cml` command should just work after installation if Java 8 is available on the environment. Please report an issue, otherwise.

## Quick, Two-Step Install with Homebrew / Linuxbrew

```
$ brew tap cmlang/cml
$ brew install cml-compiler
```

## Release Notes

To read the release notes of a specific version, please go to: https://github.com/cmlang/cml-releases/tree/master/cml-compiler

Look for the files ending with '.zip.notes.md'. For example: cml-compiler-1.0.zip.notes.md

## Semantic Versioning

The releases of the CML compiler follow Semantic Versioning: http://semver.org

Version format is MAJOR.MINOR.PATCH, where each number is incremented as follows:
- MAJOR: when we make an incompatible language change, or incompatible base library change.
- MINOR: when we add functionality in a backwards-compatible manner.
- PATCH: when we make backwards-compatible fixes.

## Specific Version Installation with Homebrew / Linuxbrew

- Before you can install the CML compiler, please run:

```
$ brew tap cmlang/cml
```

After that, the CML compiler packages will be available to be installed.

- To find the versions of the CML compiler available to install:

```
$ brew search cml-compiler
```

- To read the release notes of a specific version, please go to (look for the '.zip.notes.md' files): https://github.com/cmlang/cml-releases/tree/master/cml-compiler

- If you'd like to always have the latest version of the CML compiler:

```
$ brew install cml-compiler
```

Later on, to get the latest:

```
$ brew upgrade cml-compiler
```

You may get a new major version,
and thus backward-incompatible version,
of the CML language and compiler.
You may also get a new minor version, or a patch.

- If you'd like to pin your environment to a specific major version:

```
$ brew install cml-compiler@MAJOR
```

Later on, to get the latest of the specific major version:

```
$ brew upgrade cml-compiler@MAJOR
```

You may get a new minor version,
which will have new features,
but it is supposed to always be backward-compatible.
(Please report an issue, otherwise.)
You may also get a new patch under the major version.

- If you'd like to pin your environment to a specific minor version:

```
$ brew install cml-compiler@MAJOR.MINOR
```

Later on, to get the latest of the specific minor version:

```
$ brew upgrade cml-compiler@MAJOR.MINOR
```

You only get patches for the specific minor version.
Patches only have fixes.
They should never have new functionality,
and should always be backward-compatible.
(Please report an issue, otherwise.)
