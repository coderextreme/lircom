JAVAC=javac
JAVA=java
JAR=jar
KEYSTORE=../../../../mykeystore
CLASSPATH=../../../../target/lircom-2.jar:../../../../m2/httpclient-4.5.13.jar:../../../../m2/htmlunit-2.55.0.jar:../../../../m2/httpunit-1.7.3.jar:../../../../m2/log4j-api-2.16.0.jar:../../../../m2/log4j-core-2.16.0.jar:../../../../m2/nekohtml-1.9.22.jar:../../../../m2/xercesImpl-2.11.0.jar:../../../../m2/xml-apis-1.4.01.jar:../../../../m2/js-1.7R2.jar:../../../../m2/json-20190722.jar:..

BFCLASSPATH="..:../../../../m2/xercesImpl-2.11.0.jar:../../../../m2/xml-apis.1.4.01.jar:../../../../m2/js-1.7R2.jar"
CLASSPATH3="..:lircom.jar:../../../../target/lircom-2.jar:../../../../m2/log4j-api-2.16.0.jar:../../../../m2/log4j-core-2.16.0.jar"

lircom: lircom.jar
	(export JAVA_LIB_PATH=C:/Users/coderextreme/Downloads/jogamp-all-platforms/lib/windows-amd64/ ; $(JAVA) -Djava.library.path=$(JAVA_LIB_PATH) -Xmx256m -classpath "$(CLASSPATH)" lircom.MainWindow lircom.Chat belinda)

yottzumm2: lircom.jar
	(export JAVA_LIB_PATH=C:/Users/coderextreme/Downloads/jogamp-all-platforms/lib/windows-amd64/ ; $(JAVA) -Djava.library.path=$(JAVA_LIB_PATH) -Xmx256m -classpath "$(CLASSPATH)" lircom.MainWindow lircom.Chat Annie)

$(KEYSTORE):
	keytool -genkeypair -keyalg DSA -keysize 1024 -keystore $(KEYSTORE) -alias myself -storepass foobar 
clean:
	rm *.class lircom.jar

irc: lircom.jar
	$(JAVA) -classpath "$(CLASSPATH3)" lircom.IRCClient afternet.config symbion.config

wikispeak: lircom.jar
	$(JAVA) -classpath "$(CLASSPATH3)" lircom.IRCClient ircstorm.config

babelfish: lircom.jar
	$(JAVA) -classpath "$(BFCLASSPATH)" lircom.BabelFish "thank you"

runestone: lircom.jar
	$(JAVA) -Xmx256m -classpath "$(CLASSPATH)" lircom.Runestone

Heathens: lircom.jar
	$(JAVA) -Xmx256m -classpath "$(CLASSPATH)" lircom.Heathens


signall:
	(for i in ../../../../target/*jar ../../../../m2/*jar ../*/*.jar; do jarsigner -storepass foobar -keystore $(KEYSTORE) $$i myself; done)

speech: lircom.jar
	$(JAVA) -Xmx256m -classpath "$(CLASSPATH)" lircom.Synth


assist: lircom.jar
	$(JAVA) -Xmx256m -classpath "$(CLASSPATH)" lircom.ChatAssist lircom.Chat

channel: lircom.jar
	$(JAVA) -Xmx256m -classpath "$(CLASSPATH)" lircom.MainWindow lircom.Channel

kanal: lircom.jar
	$(JAVA) -Xmx256m -classpath "$(CLASSPATH)" lircom.MainWindow lircom.Kanal

peer: lircom.jar
	$(JAVA) -Xmx256m -classpath "$(CLASSPATH)" lircom.Peer 8180

peer2: lircom.jar
	$(JAVA) -Xmx256m -classpath "$(CLASSPATH)" lircom.Peer 7000

JAVACODE= BabelFish.java Bridge.java Channel.java Chat.java ClientConnect.java ClientException.java Client.java ClientOnServer.java Errors.java ExampleFileFilter.java Heathens.java Hyperactive.java Identd.java IRCBridge.java IRCClient.java Kanal.java Lircom.java MainApplet.java MainWindow.java Message.java Peer.java PossibleConnection.java ReceiveChatInterface.java Runestone.java SendChatInterface.java SendCommandInterface.java ServerAdvertise.java Smiley.java SVGFE.java Synth.java Trade.java WindowAspect.java

lircom.jar: $(JAVACODE)
	$(JAVAC) -encoding UTF-8 -target 18 -classpath "$(CLASSPATH)" $(JAVACODE)
	jar -cmf ../../../../lircom.manifest lircom.jar *.class  *.java
	jarsigner -storepass foobar -keystore $(KEYSTORE) lircom.jar myself
