#!/bin/sh
echo "Building all procjects.."
./mvnw clean verify -DskipTests
