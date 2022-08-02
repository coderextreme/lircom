#!/bin/bash
export JAVAC=javac
export JAVA=java
export JAR=jar
export KEYSTORE=mykeystore
export M2=m2
export CLASSPATH="target/lircom-2.jar"

${JAVA} -Xmx256m -classpath ${CLASSPATH} solitaire.Game dealer localhost 8180
