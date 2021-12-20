KEYSTORE=../mykeystore
CLASSPATH=target/lircom-2.jar:../.m2/repository/org/httpunit/httpunit/1.7.2/httpunit-1.7.2.jar:lib/martyr.jar:../.m2/repository/nekohtml/nekohtml/0.9.5/nekohtml-0.9.5.jar:../.m2/repository/xerces/xercesImpl/2.11.0/xercesImpl-2.11.0.jar:lib/xml-apis.jar:../.m2/repository/rhino/js/1.6R5/js-1.6R5.jar:../.m2/repository/org/json/json/20190722/json-20190722.jar
BFCLASSPATH=target/lircom-2.jar:../.m2/repository/org/httpunit/httpunit/1.7.2/httpunit-1.7.2.jar:../.m2/repository/nekohtml/nekohtml/0.9.5/nekohtml-0.9.5.jar:../.m2/repository/xerces/xercesImpl/2.11.0/xercesImpl-2.11.0.jar:lib/xml-apis.jar:../.m2/repository/rhino/js/1.6R5/js-1.6R5.jar


CLASSPATH2=target/lircom-2.jar:/Users/yottzumm/Applications/jogl/build/jogl.jar:/Users/yottzumm/Applications/jogl-demos/build/jogl-demos-util.jar:/Users/yottzumm/Applications/jogl/www/webstart/jogl-natives-macosx-universal.jar

CLASSPATH3=lib/martyr.jar:target/lircom-2.jar
CLASSPATH4=target/lircom-2.jar:../.m2/repository/io/socket/engine.io-client/0.6.3/engine.io-client-0.6.3.jar:../.m2/repository/io/socket/socket.io-client/0.6.3/socket.io-client-0.6.3.jar:../.m2/repository/com/squareup/okhttp/okhttp/2.7.0/okhttp-2.7.0.jar:../.m2/repository/com/squareup/okhttp/okhttp-ws/2.7.0/okhttp-ws-2.7.0.jar:../.m2/repository/com/squareup/okio/okio/1.6.0/okio-1.6.0.jar:../.m2/repository/org/json/json/20190722/json-20190722.jar
# JAVAC=/Users/yottzumm/Applications/aspectj-1.5.3/bin/ajc -1.5
JAVA=java
JAVAC=javac

lircom: target/lircom-2.jar
	(export LD_LIBRARY_PATH=/Users/yottzumm/Documents/workspace/jogl/build/obj; java -Dsun.java2d.opengl=true -Xmx256m -classpath "$(CLASSPATH)" lircom.MainWindow lircom.Chat belinda)

yottzumm2: target/lircom-2.jar
	(export LD_LIBRARY_PATH=/Users/yottzumm/Documents/workspace/jogl/build/obj; java -Dsun.java2d.opengl=true -Xmx256m -classpath "$(CLASSPATH)" lircom.MainWindow lircom.Chat Annie)

# socket:
# 	java -classpath "$(CLASSPATH4)" lircom.SocketIO

clean:
	mvn clean

scrape: target/lircom-2.jar
	java -classpath "$(CLASSPATH3)" lircom.IRCScrape config/afternet.config

irc: target/lircom-2.jar
	java -classpath "$(CLASSPATH3)" lircom.IRCClient config/afternet.config config/symbion.config

wikispeak: target/lircom-2.jar
	java -classpath "$(CLASSPATH3)" lircom.IRCClient config/ircstorm.config

babelfish: target/lircom-2.jar
	java -classpath "$(BFCLASSPATH)" lircom.BabelFish "thank you"

runestone: target/lircom-2.jar
	java -Xmx256m -classpath "$(CLASSPATH)" lircom.Runestone

Heathens: target/lircom-2.jar
	java -Xmx256m -classpath "$(CLASSPATH)" lircom.Heathens


signall:
	-(cp `echo "$(CLASSPATH)" | tr ";" '\n'` lib)
	(for i in lib/*jar; do jarsigner -storepass foobar -keystore $(KEYSTORE) $$i myself; done)

speech: target/lircom-2.jar
	java -Xmx256m -classpath "$(CLASSPATH)" lircom.Synth

channel: target/lircom-2.jar
	java -Xmx256m -classpath "$(CLASSPATH)" lircom.MainWindow lircom.Channel

kanal: target/lircom-2.jar
	java -Xmx256m -classpath "$(CLASSPATH)" lircom.MainWindow lircom.Kanal

peer: target/lircom-2.jar
	java -Xmx256m -classpath "$(CLASSPATH)" lircom.Peer 8180 &


target/lircom-2.jar:
	mvn package

SOLCLASSPATH=../.m2/repository/xerces/xercesImpl/2.11.0/xercesImpl-2.11.0.jar:lib/xmlParserAPIs-2.6.1.jar:../.m2/repository/nekohtml/nekohtml/0.9.5/nekohtml-0.9.5.jar:target/lircom-2.jar:../.m2/repository/org/httpunit/httpunit/1.7.2/httpunit-1.7.2.jar:lib/martyr.jar:lib/servlet-api-2.4.jar:../.m2/repository/rhino/js/1.6R5/js-1.6R5.jar:../.m2/repository/io/socket/engine.io-client/0.6.3/engine.io-client-0.6.3.jar:../.m2/repository/io/socket/socket.io-client/0.6.3/socket.io-client-0.6.3.jar:../.m2/repository/com/squareup/okhttp/okhttp/2.7.0/okhttp-2.7.0.jar:../.m2/repository/com/squareup/okhttp/okhttp-ws/2.7.0/okhttp-ws-2.7.0.jar:../.m2/repository/com/squareup/okio/okio/1.6.0/okio-1.6.0.jar:../.m2/repository/org/json/json/20190722/json-20190722.jar

random: target/lircom-2.jar
	java -Xmx256m -classpath "$(SOLCLASSPATH)" solitaire.Game random localhost 8180

methodical: target/lircom-2.jar
	java -Xmx256m -classpath "$(SOLCLASSPATH)" solitaire.Game methodical localhost 8180

display: target/lircom-2.jar
	java -Xmx256m -classpath "$(SOLCLASSPATH)" solitaire.Game display localhost 8180

dealer: target/lircom-2.jar
	java -Xmx256m -classpath "$(SOLCLASSPATH)" solitaire.Game dealer localhost 8180

sign: target/lircom-2.jar
	jarsigner -keystore $(KEYSTORE) target/lircom-2.jar myself

# JOGAMP_JARS=c:/Users/coderextreme/Downloads/jogamp-all-platforms/jar
JOGAMP_JARS=m2


# GLCLASSPATH=target/lircom-2.jar:${JOGAMP_JARS}/jogl-all.jar:${JOGAMP_JARS}/jogl-all-natives-windows-amd64.jar:.:${JOGAMP_JARS}/gluegen-rt.jar
GLCLASSPATH=target/lircom-2.jar:${JOGAMP_JARS}/jogl-all.jar:${JOGAMP_JARS}/jogl-all-natives-linux-amd64.jar:.:${JOGAMP_JARS}/gluegen-rt.jar



smooth:
	$(JAVA) -Xmx256m -classpath "$(GLCLASSPATH)" impact.smooth

picking:
	$(JAVA) -classpath "$(GLCLASSPATH)" impact.Picking

impact:
	$(JAVA) -classpath "$(GLCLASSPATH)" impact.Impact3D Annie
