#!/bin/bash
export PATH_TO_FX=openjfx-22_windows-x64_bin-sdk/javafx-sdk-22/lib
export PATH_TO_FX_MOD=javafx-jmods-22
export JAVAC=javac
export JAVA=java
export JAR=jar
export KEYSTORE=mykeystore
export M2=m2
export CLASSPATH="target/lircom-2.jar"

${JAVA} -Xmx256m -classpath ${CLASSPATH} solitaire.Game dealer localhost 8180
