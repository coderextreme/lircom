#!/bin/bash
JAVAC=javac
JAVA=java
JAR=jar
KEYSTORE=mykeystore
M2=m2
CLASSPATH="${M2}/xercesImpl-2.6.1.jar:${M2}/xmlParserAPIs-2.6.1.jar:${M2}/nekohtml-0.9.5.jar:target/lircom-2.jar:${M2}/httpunit.jar:${M2}/martyr.jar:${M2}/log4j-api-2.16.0.jar:${M2}/log4j-core-2.16.0.jar:${M2}/servlet-api-2.4.jar:${M2}/js-1.6R5.jar:.."

${JAVA} -Xmx256m -classpath ${CLASSPATH} solitaire.Game dealer localhost 8180
