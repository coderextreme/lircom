#!/bin/bash

. env.sh

export CLASSPATH="${CLASSPATH}:src/main/resources"

pushd src/main/java
ant impact
ant Cell
ant Cell4x4
popd
