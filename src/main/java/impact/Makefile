DIR=../../../..
DWN=C:/Users/john/Downloads
LIB=${DWN}/jogamp-all-platforms/lib/windows-amd64
CLASSPATH="${DIR}/target/lircom-2.jar;${DIR}/target/Impact.jar"


all:
	(cd ../../../..; mvn clean install)
	(cd ../../../..; ant)
	(java -Xmx256m --add-exports java.base/java.lang=ALL-UNNAMED --add-exports java.desktop/sun.awt=ALL-UNNAMED --add-exports java.desktop/sun.java2d=ALL-UNNAMED -Djava.library.path=${LIB} -Xmx256m -cp ${CLASSPATH} lircom.Peer 8180 & echo $!)
	sleep 1
	java -Xmx256m -Djava.library.path=${LIB} --add-exports java.base/java.lang=ALL-UNNAMED --add-exports java.desktop/sun.awt=ALL-UNNAMED --add-exports java.desktop/sun.java2d=ALL-UNNAMED -Djava.library.path=${LIB} -Xmx256m -cp ${CLASSPATH} impact.Impact3D
