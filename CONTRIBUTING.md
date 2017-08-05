# Contributing to CML

## Development

### Pre-requisites

#### Java 8 & Maven

In order to compile CML, Java 8 and Maven 3.x are required.

You can install Maven with Homebrew / Linuxbrew:

```
brew install maven
```

The following variables must be available in your environment:

```
export JAVA_HOME="/Library/Java/JavaVirtualMachines/jdk1.8.0_20.jdk/Contents/Home"
export M2_HOME="/usr/local/Cellar/maven/3.2.1/libexec"
```

#### Python 3 & MyPy

In order to run all tests, including the ones that generate and execute Python code, both Python 3.5+ and MyPy are required.

You can install Python with Homebrew / Linuxbrew:

```
brew install python3
```

You can install MyPy with Pip:

```
pip3 install mypy
```

The following variable must be set in your environment:

```
export PYTHON_HOME="/usr/local/Cellar/python3/3.6.2"
```

### Running Tests

To run all tests, go to `cml-acceptance` directory and run Maven:

```
cd cml-acceptance
mvn clean install
```


