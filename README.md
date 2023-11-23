# Event Service

This project contains a service that fetches events from a [Kafka](https://kafka.apache.org/) topic and stores them to a [Mongo](https://www.mongodb.com/) database. It also provides a Rest API to fetch events from the database.

The project uses [Quarkus](https://quarkus.io/) framework and takes advantage of Java 21 virtual threads.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

`./mvnw compile quarkus:dev`

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Prerequisites for running the application outside of dev mode

* Mongo instance running. An instance can be started with `docker/db/start.sh` which uses [Docker Compose](https://docs.docker.com/compose/) to start a pre-configured container. Please note that a docker secret (mongo_root_password) needs to be created before starting. Location of it is set in docker-compose.yml.
* Kafka instance running. An instance can be started with `docker/runtime/messaging/start-messaging.sh` in `common-services` repository which uses Docker Compose to start a pre-configured container

## Packaging and running the application

The application can be packaged using:

```
./mvnw clean package -Dquarkus.profile=local
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an *über-jar* as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using 

```
java -Dquarkus.profile=local -DEVENT_PASSWORD=events -jar target/quarkus-app/quarkus-run.jar
```

If you want to build an *über-jar*, execute the following command:

```
./mvnw clean package -Dquarkus.package.type=uber-jar -Dquarkus.profile=local
```

The application, packaged as an *über-jar*, is now runnable using 

```
java -Dquarkus.profile=local -DEVENT_PASSWORD=events -jar target/*-runner.jar
```

## Creating a native executable

You can create a native executable using:

```
./mvnw clean package -Dnative -Dquarkus.profile=local
```

You can then execute your native executable with:

```
./target/events-1.0.0-SNAPSHOT-runner -Dquarkus.profile=local -DEVENT_PASSWORD=events
```

Or you can create a native executable running in a container using:

```
./mvnw clean package -Dnative -Dquarkus.native.container-build=true -Dquarkus.container-image.build=true -Dquarkus.profile=docker
```

You can then start the container with: `start.sh` in `./docker/app`

## Running integration tests

You can run integration tests the following ways:

Test the service running as a:

local jar file: 

```
./mvnw verify -Dskip.surefire.tests=true
```

local native executable: 

```
./mvnw verify -Dnative -Dskip.surefire.tests=true
```

native executable running in a container: 

```
./mvnw verify -Dnative -Dquarkus.native.container-build=true -Dquarkus.container-image.build=true -Dskip.surefire.tests=true -Dquarkus.profile=docker
```
