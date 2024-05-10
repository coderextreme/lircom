#!/bin/bash
export PATH_TO_FX=openjfx-22_windows-x64_bin-sdk/javafx-sdk-22/lib
export PATH_TO_FX_MOD=javafx-jmods-22
mvn clean install; ant
export DIR=`pwd`
export CLASSPATH="${DIR}/target/Impact.jar:${DIR}/target/lircom-2.jar:${DIR}/m2/httpunit-1.7.3.jar:${DIR}/m2/nekohtml-1.9.22.jar:${DIR}/m2/rhino-1.7R4.jar:/c/Users/john/.m2/repository/com/fasterxml/jackson/core/jackson-annotations/2.17.1/jackson-annotations-2.17.1.jar:/c/Users/john/.m2/repository/com/fasterxml/jackson/core/jackson-core/2.17.1/jackson-core-2.17.1.jar:/c/Users/john/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.17.1/jackson-databind-2.17.1.jar:/c/Users/john/.m2/repository/com/fasterxml/jackson/jr/jackson-jr-objects/2.17.1/jackson-jr-objects-2.17.1.jar:/c/Users/john/.m2/repository/com/fasterxml/classmate/1.7.0/classmate-1.7.0.jar"

export DWN=/c/Users/john/Downloads
export LIB=${DWN}/jogamp-all-platforms/lib/windows-amd64

${DIR}/peer.sh 2> /dev/null 1> /dev/null &
sleep 1

java -Xmx256m -Djava.library.path=${LIB} --add-exports java.base/java.lang=ALL-UNNAMED --add-exports java.desktop/sun.awt=ALL-UNNAMED --add-exports java.desktop/sun.java2d=ALL-UNNAMED impact.Impact3D
