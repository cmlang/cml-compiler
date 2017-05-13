# CML - Conceptual Modeling Language

This repository contains the source code for the CML compiler.

## Branches

Master

![Build Status](https://travis-ci.org/cmlang/cml-compiler.svg?branch=master)

## Semantic Versioning

The releases of the CML compiler follow Semantic Versioning: http://semver.org

Version format is MAJOR.MINOR.PATCH, where each number is incremented as follows:
- MAJOR: when we make an incompatible language change, or incompatible base library change.
- MINOR: when we add functionality in a backwards-compatible manner.
- PATCH: when we make backwards-compatible fixes.

## Installation with Homebrew

- Before you can install the CML compiler, please run:

```
brew tap cmlang/cml
```

After that, the CML compiler packages will be available to be installed.

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
but it supposed to always be backward-compatible.
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
