#!/bin/bash -x
# make target/lircom-2.jar
mvn clean install
# make target/Impact.jar
ant
export DIR=`pwd`
export DWN=C:/Users/john/Downloads
export LIB=${DWN}/jogamp-all-platforms/lib/windows-amd64
export CLASSPATH="${DIR}/target/lircom-2.jar:${DIR}/target/Impact.jar"

${DIR}/peer.sh &
sleep 1

java -Xmx256m -Djava.library.path=${LIB} --add-exports java.base/java.lang=ALL-UNNAMED --add-exports java.desktop/sun.awt=ALL-UNNAMED --add-exports java.desktop/sun.java2d=ALL-UNNAMED -Djava.library.path=${LIB} -Xmx256m -cp ${CLASSPATH} impact.Impact3D
