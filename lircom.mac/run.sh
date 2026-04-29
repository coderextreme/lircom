#!/bin/bash
export CLASSPATH="../target/lircom-3-jar-with-dependencies.jar;../target/lircom-3.jar;target/Impact.jar;C:/Users/jcarl/.m2/repository/net/sf/sociaal/freetts/1.2.2/freetts-1.2.2.jar"


java -classpath ${CLASSPATH} lircom.MainWindow lircom.Chat
