#!/bin/bash -x
make peer &
sleep 1
export DIR=`pwd`
export JARS="${DIR}/m2"

java -Xmx256m -cp "${DIR}/target/lircom-2.jar:${JARS}/joal-android-natives-linux-amd64.jar:${JARS}/joal-natives-linux-amd64.jar:${JARS}/jocl-android-natives-linux-amd64.jar:${JARS}/jocl-natives-linux-amd64.jar:${JARS}/jogl-all-android-natives-linux-amd64.jar:${JARS}/jogl-all.jar:${JARS}/jogl-all-mobile-natives-linux-amd64.jar:${JARS}/jogl-all-natives-linux-amd64.jar:${JARS}/jogl-all-noawt-natives-linux-amd64.jar:${JARS}/jogl-cg-natives-linux-amd64.jar:${JARS}/jogl-natives-linux-amd64.jar:${JARS}/gluegen-rt-android-natives-linux-amd64.jar:${JARS}/gluegen-rt.jar:${JARS}/gluegen-rt-natives-linux-amd64.jar" impact.Impact3D

