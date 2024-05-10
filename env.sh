#!/bin/bash
export PATH_TO_FX=openjfx-22_windows-x64_bin-sdk/javafx-sdk-22/lib
export PATH_TO_FX_MOD=javafx-jmods-22
export DIR=`pwd`
export JAVAC=javac
export JAVA=java
export JAR=jar
export M2=/c/Users/john/.m2/repository
export KEYSTORE=mykeystore
export CLASSPATH="${DIR}/target/Impact.jar:${DIR}/target/lircom-2.jar:${DIR}/m2/httpunit-1.7.3.jar:${DIR}/m2/nekohtml-1.9.22.jar:${DIR}/m2/rhino-1.7R4.jar:${M2}/com/fasterxml/jackson/core/jackson-annotations/2.17.1/jackson-annotations-2.17.1.jar:${M2}/com/fasterxml/jackson/core/jackson-core/2.17.1/jackson-core-2.17.1.jar:${M2}/com/fasterxml/jackson/core/jackson-databind/2.17.1/jackson-databind-2.17.1.jar:${M2}/com/fasterxml/jackson/jr/jackson-jr-objects/2.17.1/jackson-jr-objects-2.17.1.jar:${M2}/com/fasterxml/classmate/1.7.0/classmate-1.7.0.jar"
export DWN=/c/Users/john/Downloads
export LIB=${DWN}/jogamp-all-platforms/lib/windows-amd64
export MEMORY=-Xmx256m
export JAVA_OPTS="java ${MEMORY} -Djava.library.path=${LIB} --add-exports java.base/java.lang=ALL-UNNAMED --add-exports java.desktop/sun.awt=ALL-UNNAMED --add-exports java.desktop/sun.java2d=ALL-UNNAMED"

rm target/*.jar
mvn clean install; ant

${JAVA_OPTS} lircom.Peer 8180 &
echo $!
sleep 1
