#!/bin/bash -x
make peer &
sleep 1
export DIR=`pwd`
export JARS="${DIR}/m2"


java -Xmx256m -cp "${DIR}/target/lircom-1.jar:${JARS}/gluegen-rt-android-natives-linux-amd64.jar:${JARS}/gluegen-rt.jar:${JARS}/gluegen-rt-natives-linux-amd64.jar:${JARS}/httpunit-1.7.3.jar:${JARS}/joal-android-natives-linux-amd64.jar:${JARS}/joal-natives-linux-amd64.jar:${JARS}/jocl-android-natives-linux-amd64.jar:${JARS}/jocl-natives-linux-amd64.jar:${JARS}/jogl-all-android-natives-linux-amd64.jar:${JARS}/jogl-all.jar:${JARS}/jogl-all-mobile-natives-linux-amd64.jar:${JARS}/jogl-all-natives-linux-amd64.jar:${JARS}/jogl-all-noawt-natives-linux-amd64.jar:${JARS}/jogl-cg-natives-linux-amd64.jar:${JARS}/jogl-natives-linux-amd64.jar:${JARS}/js-1.7R2.jar:${JARS}/json-20190722.jar:${JARS}/log4j-1.2.17.jar:${JARS}/nativewindow-natives-linux-amd64.jar:${JARS}/nekohtml-1.9.22.jar:${JARS}/newt-natives-linux-amd64.jar:${JARS}/oculusvr-natives-linux-amd64.jar:${JARS}/rhino-1.7R4.jar:${JARS}/xercesImpl-2.11.0.jar:${JARS}/xml-apis-1.4.01.jar" impact.Impact3D
