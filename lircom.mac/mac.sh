wget64 https://coderextreme.net/lircom.zip
jar -xf lircom.zip
export DIR=`pwd`/lircom
export CLASSPATH="$DIR/target/lircom-1.jar:$DIR/m2/httpunit-1.7.2.jar:$DIR/m2/log4j-1.2.17.jar:$DIR/m2/nekohtml-0.9.5.jar:$DIR/m2/xercesImpl-2.11.0.jar:$DIR/m2/js-1.6R5.jar:$DIR/m2/json-20190722.jar:$DIR/m2/xml-apis-1.4.01.jar"

java -Xmx256m -classpath $CLASSPATH lircom.MainWindow lircom.Chat
