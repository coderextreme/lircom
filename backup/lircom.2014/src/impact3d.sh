#!/bin/bash -x
export CLASSPATH="lircom.jar;impact3d.jar;../../../jogamp/jogamp-windows-amd64/jar/jogl.all.jar;../../../jogamp/jogamp-windows-amd64/jar/gluegen-rt.jar"
export JAVA_LIB_PATH=../../../jogamp/jogamp-windows-amd64/lib/
/cygdrive/c/Program\ Files/Java/jdk1.7.0_02/bin/java -Xmx256m -Djava.library.path=${JAVA_LIB_PATH} -classpath ${CLASSPATH} impact.Impact3D
