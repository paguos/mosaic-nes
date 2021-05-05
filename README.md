# mosaic-nes
Eclipse MOSAIC integration with NebulaStream.

## Project

This repository is part of the [Enabling Moving Range Queries in Stream Processing Systems](https://github.com/users/paguos/projects/1) project, alongside the following repositories:

- [Mobility Aware NES](https://github.com/paguos/nes-mobility)

## Requirements

- [Java JDK](https://adoptopenjdk.net/) 8
- [Gradle](https://gradle.org)
- [Maven](https://maven.apache.org) (3.1.x or higher)
- [Sumo](https://www.eclipse.org/sumo/) 1.8

## Development

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
