#!/bin/bash
export PATH_TO_FX=openjfx-22_windows-x64_bin-sdk/javafx-sdk-22/lib
export PATH_TO_FX_MOD=javafx-jmods-22
export JAVAC=javac
export DIR=.
export DWN=C:/Users/john/Downloads
export JAVA=java
export JAR=jar
export KEYSTORE=mykeystore
export M2=m2
export CLASSPATH="${DIR}/target/lircom-2.jar;${DIR}/target/Impact.jar;${DIR}/m2/httpunit-1.7.3.jar;${DIR}/m2/nekohtml-1.9.22.jar;${DIR}/m2/rhino-1.7R4.jar;${DIR}/m2/json-20190722.jar"
export LIB=${DWN}/jogamp-all-platforms/lib/windows-amd64
export  KEYSTORE=mykeystore

keytool -genkeypair -keyalg DSA -keysize 1024 -keystore ${KEYSTORE} -alias myself -storepass foobar 

mvn clean install
ant

${JAVA} -Xmx256m --add-exports java.base/java.lang=ALL-UNNAMED --add-exports java.desktop/sun.awt=ALL-UNNAMED --add-exports java.desktop/sun.java2d=ALL-UNNAMED -Djava.library.path=${LIB} -Xmx256m -cp ${CLASSPATH} lircom.Peer 8180 &
echo $!
sleep 1
${JAVA} -Xmx256m -cp ${CLASSPATH} lircom.MainWindow lircom.Chat Christoph &
${JAVA} -Xmx256m -cp ${CLASSPATH} lircom.MainWindow lircom.Chat John &
${JAVA} -Xmx256m -cp ${CLASSPATH} solitaire.Game dealer &
${JAVA} -Xmx256m -Djava.library.path=${LIB} --add-exports java.base/java.lang=ALL-UNNAMED --add-exports java.desktop/sun.awt=ALL-UNNAMED --add-exports java.desktop/sun.java2d=ALL-UNNAMED -Djava.library.path=${LIB} -Xmx256m -cp ${CLASSPATH} impact.Impact3D &
${JAVA} -Xmx256m -Djava.library.path=${LIB} --add-exports java.base/java.lang=ALL-UNNAMED --add-exports java.desktop/sun.awt=ALL-UNNAMED --add-exports java.desktop/sun.java2d=ALL-UNNAMED -Djava.library.path=${LIB} -Xmx256m -cp ${CLASSPATH} impact.Impact3D &
