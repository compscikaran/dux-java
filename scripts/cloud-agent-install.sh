#!/usr/bin/env bash
set -euo pipefail

export JAVA_HOME="${JAVA_HOME:-/usr/lib/jvm/java-21-openjdk-amd64}"
export PATH="${JAVA_HOME}/bin:${PATH}"

mvn -f library-core/pom.xml install -DskipTests -Dmaven.javadoc.skip=true -Dgpg.skip=true -q
mvn -f spring-boot-starter/pom.xml package -DskipTests -q
