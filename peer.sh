#!/bin/bash -x
export PATH_TO_FX=openjfx-22_windows-x64_bin-sdk/javafx-sdk-22/lib
export PATH_TO_FX_MOD=javafx-jmods-22
export DIR=`pwd`
# export CLASSPATH="${DIR}/target/lircom-2.jar;${DIR}/m2/httpunit-1.7.3.jar;${DIR}/m2/nekohtml-1.9.22.jar;${DIR}/m2/rhino-1.7R4.jar;${DIR}/m2/json-20190722.jar"
export CLASSPATH="lircom.jar;${DIR}/target/Impact.jar;${DIR}/target/lircom-2.jar"

export DIR=`pwd`
export CLASSPATH="${DIR}/target/lircom-2.jar:${DIR}/target/Impact.jar"
java -Xmx256m --add-exports java.base/java.lang=ALL-UNNAMED --add-exports java.desktop/sun.awt=ALL-UNNAMED --add-exports java.desktop/sun.java2d=ALL-UNNAMED -Djava.library.path=${LIB} -Xmx256m -cp ${CLASSPATH} lircom.Peer 8180 &
echo $!
