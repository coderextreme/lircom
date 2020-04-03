#!/bin/bash -x
make peer &
sleep 1
export JOGAMP_JARS=c:/Users/coderextreme/Downloads/jogamp-all-platforms/jar

java -Xmx256m -classpath "${JOGAMP_JARS}/jogl-all.jar;${JOGAMP_JARS}/jogl-all-natives-windows-amd64.jar;target/lircom-1.jar;.;${JOGAMP_JARS}/gluegen-rt.jar" impact.Impact3D
