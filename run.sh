#!/bin/sh

./mvnw install

docker build -t dtb/lego-rest .


