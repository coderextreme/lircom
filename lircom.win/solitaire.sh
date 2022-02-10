#!/bin/bash
JAVAC=/c/openjdk-17.0.1_windows-x64_bin/jdk-17.0.1/bin/javac
JAVA=/c/openjdk-17.0.1_windows-x64_bin/jdk-17.0.1/bin/java
JAR=/c/openjdk-17.0.1_windows-x64_bin/jdk-17.0.1/bin/jar
KEYSTORE=mykeystore
M2=../m2
CLASSPATH="${M2}/xmlParserAPIs-2.6.1.jar;${M2}/nekohtml-0.9.5.jar;../target/lircom-2.jar;${M2}/httpunit.jar;${M2}/martyr.jar;${M2}/servlet-api-2.4.jar;${M2}/js-1.6R5.jar;.."

${JAVA} -Xmx256m -classpath ${CLASSPATH} solitaire.Game dealer localhost 8180
