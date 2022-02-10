#!/bin/bash -x
export CLASSPATH="../target/lircom-2.jar;../m2/jogl-all.jar;../m2/gluegen-rt.jar;../m2/jogl-all-natives-windows-amd64.jar;../m2/jogl-all-noawt-natives-windows-amd64.jar"

/c/openjdk-17.0.1_windows-x64_bin/jdk-17.0.1/bin/java --add-exports java.base/java.lang=ALL-UNNAMED --add-exports java.desktop/sun.awt=ALL-UNNAMED --add-exports java.desktop/sun.java2d=ALL-UNNAMED -Xmx256m -classpath ${CLASSPATH} impact.Impact3D

