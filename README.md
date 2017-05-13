# CML - Conceptual Modeling Language

This repository contains the source code for the CML compiler.

## Branches

Master

![Build Status](https://travis-ci.org/cmlang/cml-compiler.svg?branch=master)

## Installation with Homebrew

The CML compiler releases follow Semantic Versioning: http://semver.org

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
$ brew install cml-compiler@MAJOR_VERSION
```

Later on, to get the latest of the specific major version:

```
$ brew upgrade cml-compiler@MAJOR_VERSION
```

You may get a new minor version,
which will have new features,
but it supposed to always be backward-compatible.
(Please report an issue, otherwise.)
You may also get a new patch.

- If you'd like to pin your environment to a specific minor version:

```
$ brew install cml-compiler@MAJOR_VERSION.MINOR_VERSION
```

Later on, to get the latest of the specific minor version:

```
$ brew upgrade cml-compiler@MAJOR_VERSION.MINOR_VERSION
```

You only get patches for the specific minor version.
Patches only have fixes.
They should never have new functionality,
and should always be backward-compatible.
(Please report an issue, otherwise.)
