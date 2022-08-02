#!/bin/bash -x
# make target/lircom-2.jar
mvn clean install
# make target/Impact.jar
ant
sleep 1
export DIR=`pwd`
export DWN=/C/Users/john/Downloads
export LIB=${DWN}/jogamp-all-platforms/lib/windows-amd64

${DIR}/peer.sh &

java -Xmx256m --add-exports java.base/java.lang=ALL-UNNAMED --add-exports java.desktop/sun.awt=ALL-UNNAMED --add-exports java.desktop/sun.java2d=ALL-UNNAMED -Djava.library.path=${LIB} -Xmx256m -cp ${DIR}/target/Impact.jar impact.Impact3D
