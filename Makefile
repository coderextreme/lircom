KEYSTORE=../mykeystore
SVGCLASSPATH=/Users/yottzumm/Applications/aspectj-1.5.3/lib/aspectjrt.jar;/Users/yottzumm/Applications/batik-1.7/lib/batik-svggen.jar;/Users/yottzumm/Applications/batik-1.7/lib/batik-dom.jar;/Users/yottzumm/Applications/batik-1.7/lib/batik-awt-util.jar;/Users/yottzumm/Applications/batik-1.7/lib/batik-util.jar;/Users/yottzumm/Applications/batik-1.7/lib/xalan-2.6.0.jar;/Users/yottzumm/Applications/batik-1.7/lib/xml-apis.jar;/Users/yottzumm/Applications/batik-1.7/batik-1.7/lib/batik-all.jar
CLASSPATH=target/lircom-1.jar;lib/cmu_time_awb.jar;lib/cmu_us_kal.jar;lib/cmudict04.jar;lib/cmulex.jar;lib/cmutimelex.jar;lib/en_us.jar;lib/jsapi.jar;lib/freetts-jsapi10.jar;lib/freetts.jar;lib/httpunit.jar;../.m2/repository/log4j/log4j/1.2.17/log4j-1.2.17.jar;lib/martyr.jar;lib/mbrola.jar;lib/nekohtml.jar;lib/xercesImpl.jar;lib/xml-apis.jar;lib/js.jar;../.m2/repository/org/json/json/20190722/json-20190722.jar
BFCLASSPATH=target/lircom-1.jar;lib/httpunit.jar;lib/nekohtml.jar;lib/xercesImpl.jar;lib/xml-apis.jar;lib/js.jar


CLASSPATH2=target/lircom-1.jar;/Users/yottzumm/Applications/jogl/build/jogl.jar;/Users/yottzumm/Applications/jogl-demos/build/jogl-demos-util.jar;/Users/yottzumm/Applications/jogl/www/webstart/jogl-natives-macosx-universal.jar;../.m2/repository/log4j/log4j/1.2.17/log4j-1.2.17.jar

CLASSPATH3=lib/martyr.jar;target/lircom-1.jar;../.m2/repository/log4j/log4j/1.2.17/log4j-1.2.17.jar
CLASSPATH4=target/lircom-1.jar;../.m2/repository/io/socket/engine.io-client/0.6.3/engine.io-client-0.6.3.jar;../.m2/repository/io/socket/socket.io-client/0.6.3/socket.io-client-0.6.3.jar;../.m2/repository/com/squareup/okhttp/okhttp/2.7.0/okhttp-2.7.0.jar;../.m2/repository/com/squareup/okhttp/okhttp-ws/2.7.0/okhttp-ws-2.7.0.jar;../.m2/repository/com/squareup/okio/okio/1.6.0/okio-1.6.0.jar
# JAVAC=/Users/yottzumm/Applications/aspectj-1.5.3/bin/ajc -1.5
JAVA=java
JAVAC=javac

lircom: target/lircom-1.jar
	(export DYLD_LIBRARY_PATH=/Users/yottzumm/Documents/workspace/jogl/build/obj; java -Dsun.java2d.opengl=true -Xmx256m -classpath "$(CLASSPATH)" lircom.MainWindow lircom.Chat yottzumm)

yottzumm2: target/lircom-1.jar
	(export DYLD_LIBRARY_PATH=/Users/yottzumm/Documents/workspace/jogl/build/obj; java -Dsun.java2d.opengl=true -Xmx256m -classpath "$(CLASSPATH)" lircom.MainWindow lircom.Chat yottzumm2)

socket:
	java -classpath "$(CLASSPATH4)" lircom.SocketIO

clean:
	mvn clean

scrape: target/lircom-1.jar
	java -classpath "$(CLASSPATH3)" lircom.IRCScrape config/afternet.config

irc: target/lircom-1.jar
	java -classpath "$(CLASSPATH3)" lircom.IRCClient config/afternet.config config/symbion.config

wikispeak: target/lircom-1.jar
	java -classpath "$(CLASSPATH3)" lircom.IRCClient config/ircstorm.config

babelfish: target/lircom-1.jar
	java -classpath "$(BFCLASSPATH)" lircom.BabelFish "thank you"

runestone: target/lircom-1.jar
	java -Xmx256m -classpath "$(CLASSPATH)" lircom.Runestone

Heathens: target/lircom-1.jar
	java -Xmx256m -classpath "$(CLASSPATH)" lircom.Heathens


signall:
	-(cp `echo "$(CLASSPATH)" | tr ";" '\n'` lib)
	(for i in lib/*jar; do jarsigner -storepass argus1 -keystore $(KEYSTORE) $$i myself; done)

speech: target/lircom-1.jar
	java -Xmx256m -classpath "$(CLASSPATH)" lircom.Synth


assist: target/lircom-1.jar
	java -Xmx256m -classpath "$(CLASSPATH)" lircom.ChatAssist lircom.Chat

channel: target/lircom-1.jar
	java -Xmx256m -classpath "$(CLASSPATH)" lircom.MainWindow lircom.Channel

kanal: target/lircom-1.jar
	java -Xmx256m -classpath "$(CLASSPATH)" lircom.MainWindow lircom.Kanal

peer: target/lircom-1.jar
	java -Xmx256m -classpath "$(CLASSPATH)" lircom.Peer 8180

#target/lircom-1.jar: Orbit2.java Animator.java
#	(cd target; jar -cmf lircom/lircom.manifest lircom-1.jar lircom/Orbit2*class lircom/Animator*class; jarsigner -storepass argus1 -keystore $(KEYSTORE) lircom-1.jar myself)
# JAVACODE2=Animator.java Synth.java
# JAVACODE= BabelFish.java Bridge.java Channel.java Chat.java Client.java ClientConnect.java ClientException.java ClientOnServer.java Errors.java ExampleFileFilter.java Hyperactive.java IRCBridge.java IRCClient.java IRCScrape.java Identd.java Kanal.java Lircom.java MainApplet.java MainWindow.java Message.java Peer.java PossibleConnection.java ReceiveChatInterface.java Runestone.java SendChatInterface.java SendCommandInterface.java ServerAdvertise.java Smiley.java Trade.java Heathens.java Synth.java
# OTHERJAVACODE= SVGFE.java WindowAspect.java
target/lircom-1.jar:
	mvn package

# target/chat3d.jar: $(JAVACODE)
#	$(JAVAC) -target 1.6 -classpath "$(CLASSPATH)" $(JAVACODE)
#	(cd target; jar -cmf lircom/lircom.manifest chat3d.jar lircom; jarsigner -keystore $(KEYSTORE) chat3d.jar myself)
#
#chat3d: target/chat3d.jar
#	java -Dsun.java2d.opengl=true -Xmx256m -classpath "target/chat3d.jar:$(CLASSPATH)" lircom.MainWindow lircom.Chat

SOLCLASSPATH=lib/xercesImpl-2.6.1.jar;lib/xmlParserAPIs-2.6.1.jar;lib/nekohtml-0.9.5.jar;target/lircom-1.jar;lib/httpunit.jar;lib/martyr.jar;../.m2/repository/log4j/log4j/1.2.17/log4j-1.2.17.jar;lib/servlet-api-2.4.jar;lib/js-1.6R5.jar;../.m2/repository/io/socket/engine.io-client/0.6.3/engine.io-client-0.6.3.jar;../.m2/repository/io/socket/socket.io-client/0.6.3/socket.io-client-0.6.3.jar;../.m2/repository/com/squareup/okhttp/okhttp/2.7.0/okhttp-2.7.0.jar;../.m2/repository/com/squareup/okhttp/okhttp-ws/2.7.0/okhttp-ws-2.7.0.jar;../.m2/repository/com/squareup/okio/okio/1.6.0/okio-1.6.0.jar

random: target/lircom-1.jar
	java -Xmx256m -classpath "$(SOLCLASSPATH)" solitaire.Game random localhost 8088

methodical: target/lircom-1.jar
	java -Xmx256m -classpath "$(SOLCLASSPATH)" solitaire.Game methodical localhost 8088

display: target/lircom-1.jar
	java -Xmx256m -classpath "$(SOLCLASSPATH)" solitaire.Game display localhost 8088

dealer: target/lircom-1.jar
	java -Xmx256m -classpath "$(SOLCLASSPATH)" solitaire.Game dealer localhost 8088

sign: target/lircom-1.jar
	jarsigner -keystore $(KEYSTORE) target/lircom-1.jar myself


GLCLASSPATH=target/lircom-1.jar;../.m2/repository/org/jogamp/jogl/jogl-all/2.3.2/jogl-all-2.3.2-natives-macosx-universal.jar;../.m2/repository/org/jogamp/jogl/jogl-all/2.3.2/jogl-all-2.3.2.jar;../.m2/repository/org/jogamp/gluegen/gluegen-rt/2.3.2/gluegen-rt-2.3.2-natives-macosx-universal.jar;../.m2/repository/org/jogamp/gluegen/gluegen-rt/2.3.2/gluegen-rt-2.3.2.jar;../.m2/repository/org/jogamp/gluegen/gluegen-rt-main/2.3.2/gluegen-rt-main-2.3.2.jar



smooth:
	$(JAVA) -Xmx256m -classpath "$(GLCLASSPATH)" smooth

picking:
	$(JAVA) -classpath "$(GLCLASSPATH)" impact.Picking

impact:
	$(JAVA) -classpath "$(GLCLASSPATH)" impact.Impact3D
