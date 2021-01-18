#!/bin/bash -x
# make peer &
# sleep 1
export DIR=`pwd`
export JOGAMP_JARS="${DIR}/m2"
export JOGAMP_NATIVES="${DIR}/natives/linux-amd64"


java -Xmx256m -cp "${DIR}/target/lircom-1.jar:${JOGAMP_JARS}/gluegen-rt.jar:${JOGAMP_JARS}/gluegen-rt-natives-android-aarch64.jar:${JOGAMP_JARS}/gluegen-rt-natives-android-armv6.jar:${JOGAMP_JARS}/gluegen-rt-natives-linux-amd64.jar:${JOGAMP_JARS}/gluegen-rt-natives-linux-armv6hf.jar:${JOGAMP_JARS}/gluegen-rt-natives-linux-armv6.jar:${JOGAMP_JARS}/gluegen-rt-natives-linux-i586.jar:${JOGAMP_JARS}/gluegen-rt-natives-macosx-universal.jar:${JOGAMP_JARS}/gluegen-rt-natives-solaris-amd64.jar:${JOGAMP_JARS}/gluegen-rt-natives-solaris-i586.jar:${JOGAMP_JARS}/gluegen-rt-natives-windows-amd64.jar:${JOGAMP_JARS}/gluegen-rt-natives-windows-i586.jar:${JOGAMP_JARS}/jogl-all.jar:${JOGAMP_JARS}/jogl-all-natives-android-aarch64.jar:${JOGAMP_JARS}/jogl-all-natives-android-armv6.jar:${JOGAMP_JARS}/jogl-all-natives-linux-amd64.jar:${JOGAMP_JARS}/jogl-all-natives-linux-armv6hf.jar:${JOGAMP_JARS}/jogl-all-natives-linux-armv6.jar:${JOGAMP_JARS}/jogl-all-natives-linux-i586.jar:${JOGAMP_JARS}/jogl-all-natives-macosx-universal.jar:${JOGAMP_JARS}/jogl-all-natives-solaris-amd64.jar:${JOGAMP_JARS}/jogl-all-natives-solaris-i586.jar:${JOGAMP_JARS}/jogl-all-natives-windows-amd64.jar:${JOGAMP_JARS}/jogl-all-natives-windows-i586.jar" impact.Impact3D
