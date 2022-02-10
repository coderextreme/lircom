export DIR=`pwd`
export CLASSPATH="$DIR/target/lircom-2.jar:$DIR/m2/httpunit-1.7.3.jar:$DIR/m2/nekohtml-1.9.22.jar:$DIR/m2/rhino-1.7R4.jar:$DIR/m2/json-20190722.jar"

/c/openjdk-17.0.1_windows-x64_bin/jdk-17.0.1/bin/java -Xmx256m -cp $CLASSPATH lircom.Peer 8180
