#!/bin/bash
# make target/lircom-2.jar
# mvn clean install
# make target/Impact.jar
# ant
export DIR=`pwd`
export DWN=/c/Users/john/Downloads
export LIB=${DWN}/jogamp-all-platforms/lib/windows-amd64

${DIR}/peer.sh 2> /dev/null 1> /dev/null &
sleep 1

java -Xmx256m -Djava.library.path=${LIB} --add-exports java.base/java.lang=ALL-UNNAMED --add-exports java.desktop/sun.awt=ALL-UNNAMED --add-exports java.desktop/sun.java2d=ALL-UNNAMED -Djava.library.path=${LIB} -Xmx256m -cp "./target/Impact.jar;./target/lircom-2.jar" impact.Impact3D
