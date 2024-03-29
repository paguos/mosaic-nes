# mosaic-nes
Eclipse MOSAIC integration with NebulaStream.

## Project

This repository is part of the [Enabling Moving Range Queries in Stream Processing Systems](https://github.com/users/paguos/projects/1) project.
## Requirements

- [Docker](https://www.docker.com/)
- [Java JDK](https://adoptopenjdk.net/) 8
- [Gradle](https://gradle.org)
- [Maven](https://maven.apache.org) (3.1.x or higher)
- [Sumo](https://www.eclipse.org/sumo/) 1.10

**Note:** Currently only support for linux

## Setup

Install the NES Java Client:

```sh
gradle --project-dir libs/nebulastream-java-client install
```

Build the maven module:

```
mvn clean install
```

Integration tests:

```sh
mvn test -fae -P integration-tests
```

## Run Simulations

Package the applications and scenarios with mosaic:

```shell
scripts/pacakge.sh
```

Run a simulation scenario:

```shell
cd mosaic
./mosaic.sh -s <scenario-name> -b 1
```

To find the scenario name you visit the [list of available scenarios](scenarios/README.md).
