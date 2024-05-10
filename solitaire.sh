#!/bin/bash
export PATH_TO_FX=openjfx-22_windows-x64_bin-sdk/javafx-sdk-22/lib
export PATH_TO_FX_MOD=javafx-jmods-22
export JAVAC=javac
export JAVA=java
export JAR=jar
export KEYSTORE=mykeystore
export M2=m2
export DIR=/c/Users/john/lircom
export CLASSPATH="${DIR}/target/lircom-2.jar:${DIR}/m2/httpunit-1.7.3.jar:${DIR}/m2/nekohtml-1.9.22.jar:${DIR}/m2/rhino-1.7R4.jar:/c/Users/john/.m2/repository/com/fasterxml/jackson/core/jackson-annotations/2.17.1/jackson-annotations-2.17.1.jar:/c/Users/john/.m2/repository/com/fasterxml/jackson/core/jackson-core/2.17.1/jackson-core-2.17.1.jar:/c/Users/john/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.17.1/jackson-databind-2.17.1.jar:/c/Users/john/.m2/repository/com/fasterxml/jackson/jr/jackson-jr-objects/2.17.1/jackson-jr-objects-2.17.1.jar:/c/Users/john/.m2/repository/com/fasterxml/classmate/1.7.0/classmate-1.7.0.jar"
${DIR}/peer.sh &

${JAVA} -Xmx256m -classpath ${CLASSPATH} solitaire.Game dealer localhost 8180 &
