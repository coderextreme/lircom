export CLASSPATH="$HOME/lircom/target/lircom-1.jar:$HOME/m2/httpunit-1.7.2.jar:$HOME/m2/log4j-1.2.17.jar:$HOME/m2/nekohtml-0.9.5.jar:$HOME/m2/xercesImpl-2.11.0.jar:$HOME/m2/js-1.6R5.jar:$HOME/m2/json-20190722.jar:$HOME/m2/xml-apis-1.4.01.jar"

java -Xmx256m -classpath $CLASSPATH lircom.MainWindow lircom.Chat
