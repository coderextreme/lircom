#!/bin/bash
export PATH_TO_FX=openjfx-22_windows-x64_bin-sdk/javafx-sdk-22/lib
export PATH_TO_FX_MOD=javafx-jmods-22
export DIR=`pwd`
export JAVAC=javac
export JAVA=java
export JAR=jar
export M2=/c/Users/jcarl/.m2/repository
export KEYSTORE=mykeystore
export CLASSPATH="${DIR}/target/Impact.jar:${DIR}/target/lircom-3.jar:${DIR}/m2/httpunit-1.7.3.jar:${DIR}/m2/nekohtml-1.9.22.jar:${M2}/com/squareup/okhttp3/okhttp/4.9.3/okhttp-4.9.3.jar:${M2}/org/mozilla/rhino/1.9.1/rhino-1.9.1.jar:${M2}/com/fasterxml/jackson/core/jackson-annotations/2.18.6/jackson-annotations-2.18.6.jar:${M2}/com/fasterxml/jackson/core/jackson-core/2.18.6/jackson-core-2.18.6.jar:${M2}/com/fasterxml/jackson/core/jackson-databind/2.18.6/jackson-databind-2.18.6.jar:${M2}/com/fasterxml/jackson/jr/jackson-jr-objects/2.18.6/jackson-jr-objects-2.18.6.jar:${M2}/com/fasterxml/classmate/1.7.0/classmate-1.7.0.jar"
export DWN=/c/Users/jcarl/Downloads
export LIB=${DWN}/jogamp-all-platforms/lib/windows-amd64
export MEMORY=-Xmx256m
export JAVA_OPTS="java ${MEMORY} -javaagent:C:/Users/jcarl/lircom/patch-agent/target/patch-agent-1.0.jar -Djava.library.path=${LIB} --add-exports java.base/java.lang=ALL-UNNAMED --add-exports java.desktop/sun.awt=ALL-UNNAMED --add-exports java.desktop/sun.java2d=ALL-UNNAMED --enable-final-field-mutation=ALL-UNNAMED --enable-native-access=ALL-UNNAMED"
export PORTS="8180 8181 8182 8183"
export NUM_PORTS=4


rm target/*.jar
mvn clean install; ant
