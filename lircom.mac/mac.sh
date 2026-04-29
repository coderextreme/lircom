#!/bin/zsh
rm -r ./lircom*
wget64 https://coderextreme.net/lircom.zip
jar -xf lircom.zip
export DIR=`pwd`/lircom
export CLASSPATH="$DIR/target/lircom-3.jar:$DIR/target/Impact.jar:$DIR/target/lircom-3-jar-with-dependencies.jar"
java -Xmx256m -classpath $CLASSPATH lircom.MainWindow lircom.Chat
