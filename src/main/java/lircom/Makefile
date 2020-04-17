JAVAC=javac
JAVA=java
JAR=jar
KEYSTORE=../../../mykeystore
SVGCLASSPATH="/Users/yottzumm/Applications/aspectj-1.5.3/lib/aspectjrt.jar;/Users/yottzumm/Applications/batik-1.7/lib/batik-svggen.jar;/Users/yottzumm/Applications/batik-1.7/lib/batik-dom.jar;/Users/yottzumm/Applications/batik-1.7/lib/batik-awt-util.jar;/Users/yottzumm/Applications/batik-1.7/lib/batik-util.jar;/Users/yottzumm/Applications/batik-1.7/lib/xalan-2.6.0.jar;/Users/yottzumm/Applications/batik-1.7/lib/xml-apis.jar;/Users/yottzumm/Applications/batik-1.7/batik-1.7/lib/batik-all.jar"
# CLASSPATH="..;../solitaire.jar;../lircom.jar;../../lib/cmu_time_awb.jar;../../lib/cmu_us_kal.jar;../../lib/cmudict04.jar;../../lib/cmulex.jar;../../lib/cmutimelex.jar;../../lib/en_us.jar;../../lib/jsapi.jar;../../lib/freetts-jsapi10.jar;../../lib/freetts.jar;../../lib/httpunit.jar;../../lib/log4j-1.2.14.jar;../../lib/martyr.jar;../../lib/mbrola.jar;../../lib/nekohtml.jar;../../lib/xercesImpl.jar;../../lib/xml-apis.jar;../../lib/js.jar"
# CLASSPATH="..:../solitaire.jar:../lircom.jar:../../lib/cmu_time_awb.jar:../../lib/cmu_us_kal.jar:../../lib/cmudict04.jar:../../lib/cmulex.jar:../../lib/cmutimelex.jar:../../lib/en_us.jar:../../lib/jsapi.jar:../../lib/freetts-jsapi10.jar:../../lib/freetts.jar:../../lib/httpunit.jar:../../lib/log4j-1.2.14.jar:../../lib/martyr.jar:../../lib/mbrola.jar:../../lib/nekohtml.jar:../../lib/xercesImpl.jar:../../lib/xml-apis.jar:../../lib/js.jar"
CLASSPATH=target/lircom-1.jar;C:/Users/coderextreme/.m2/repository/org/httpunit/httpunit/1.7.2/httpunit-1.7.2.jar;C:/Users/coderextreme/.m2/repository/log4j/log4j/1.2.17/log4j-1.2.17.jar;lib/martyr.jar;C:/Users/coderextreme/.m2/repository/nekohtml/nekohtml/0.9.5/nekohtml-0.9.5.jar;C:/Users/coderextreme/.m2/repository/xerces/xercesImpl/2.11.0/xercesImpl-2.11.0.jar;C:/Users/coderextreme/.m2/repository/xml-apis/xml-apis/1.4.01/xml-apis-1.4.01.jar;C:/Users/coderextreme/.m2/repository/rhino/js/1.6R5/js-1.6R5.jar;C:/Users/coderextreme/.m2/repository/org/json/json/20190722/json-20190722.jar
BFCLASSPATH="..;../../lib/httpunit.jar;../../lib/nekohtml.jar;../../lib/xercesImpl.jar;../../lib/xml-apis.jar;../../lib/js.jar"



CLASSPATH2="..;../orbit.jar;/Users/yottzumm/Applications/jogl/build/jogl.jar;/Users/yottzumm/Applications/jogl-demos/build/jogl-demos-util.jar;/Users/yottzumm/Applications/jogl/www/webstart/jogl-natives-macosx-universal.jar;/Users/yottzumm/dev/martyr/trunk/lib/log4j-1.2.14.jar"

CLASSPATH3="../../lib/martyr.jar;../lircom.jar;../../lib/log4j-1.2.14.jar"

lircom: ../lircom.jar
	(export JAVA_LIB_PATH=../../jogamp/jogamp-windows-amd64/lib/; $(JAVA) -Djava.library.path=$(JAVA_LIB_PATH) -Xmx256m -classpath $(CLASSPATH) lircom.MainWindow lircom.Chat yottzumm)

yottzumm2: ../lircom.jar
	(export JAVA_LIB_PATH=../../jogamp/jogamp-windows-amd64/lib/; $(JAVA) -Djava.library.path=$(JAVA_LIB_PATH) -Xmx256m -classpath $(CLASSPATH) lircom.MainWindow lircom.Chat yottzumm2)

$(KEYSTORE):
	keytool -genkeypair -keystore $(KEYSTORE) -alias myself -storepass argus1 
clean:
	rm *.class ../lircom.jar

orbit: ../orbit.jar
	(export JAVA_LIB_PATH=../../jogamp/jogamp-windows-amd64/lib/; $(JAVA) -Djava.library.path=$(JAVA_LIB_PATH) -Xmx256m -classpath $(CLASSPATH2) lircom.Orbit2)

irc: ../lircom.jar
	$(JAVA) -classpath $(CLASSPATH3) lircom.IRCClient afternet.config symbion.config

wikispeak: ../lircom.jar
	$(JAVA) -classpath $(CLASSPATH3) lircom.IRCClient ircstorm.config

babelfish: ../lircom.jar
	$(JAVA) -classpath $(BFCLASSPATH) lircom.BabelFish "thank you"

runestone: ../lircom.jar
	$(JAVA) -Xmx256m -classpath $(CLASSPATH) lircom.Runestone

Heathens: ../lircom.jar
	$(JAVA) -Xmx256m -classpath $(CLASSPATH) lircom.Heathens


signall:
	-(cp `echo $(CLASSPATH) | tr : '\n'` ../../lib)
	(for i in ../../lib/*jar; do jarsigner -storepass argus1 -keystore $(KEYSTORE) $$i myself; done)

speech: ../lircom.jar
	$(JAVA) -Xmx256m -classpath $(CLASSPATH) lircom.Synth


assist: ../lircom.jar
	$(JAVA) -Xmx256m -classpath $(CLASSPATH) lircom.ChatAssist lircom.Chat

channel: ../lircom.jar
	$(JAVA) -Xmx256m -classpath $(CLASSPATH) lircom.MainWindow lircom.Channel

kanal: ../lircom.jar
	$(JAVA) -Xmx256m -classpath $(CLASSPATH) lircom.MainWindow lircom.Kanal

peer: ../lircom.jar
	$(JAVA) -Xmx256m -classpath $(CLASSPATH) lircom.Peer 8180

peer2: ../lircom.jar
	$(JAVA) -Xmx256m -classpath $(CLASSPATH) lircom.Peer 7000

../orbit.jar: Orbit2.java Animator.java
	$(JAVAC) -encoding UTF-8 -target 14 -classpath $(CLASSPATH2) Orbit2.java Animator.java
	(cd ..; jar -cmf lircom/lircom.manifest orbit.jar lircom/Orbit2*class lircom/Animator*class; jarsigner -storepass argus1 -keystore $(KEYSTORE) orbit.jar myself)
JAVACODE2=Animator.java Synth.java
JAVACODE= BabelFish.java Bridge.java Channel.java Chat.java Client.java ClientConnect.java ClientException.java ClientOnServer.java Errors.java ExampleFileFilter.java Hyperactive.java IRCBridge.java IRCClient.java Identd.java Kanal.java Lircom.java MainApplet.java MainWindow.java Message.java Peer.java PossibleConnection.java ReceiveChatInterface.java Runestone.java SendChatInterface.java SendCommandInterface.java ServerAdvertise.java Smiley.java Trade.java Heathens.java
OTHERJAVACODE= SVGFE.java WindowAspect.java
../lircom.jar: $(JAVACODE)
	$(JAVAC) -encoding UTF-8 -target 14 -classpath $(CLASSPATH) $(JAVACODE)
	jar -cmf ../lircom/lircom.manifest ../lircom.jar ../lircom
	(cd ..; jar -cmf lircom/lircom.manifest lircom.jar lircom; jarsigner -storepass argus1 -keystore $(KEYSTORE) lircom.jar myself)
	# cp ../lircom.jar /Users/yottzumm/Applications/apache-tomcat-6.0.14/webapps/web/WEB-INF/lib/

../chat3d.jar: $(JAVACODE)
	$(JAVAC) -encoding UTF-8 -target 14 -classpath $(CLASSPATH) $(JAVACODE)
	(cd ..; jar -cmf lircom/lircom.manifest chat3d.jar lircom; jarsigner -keystore $(KEYSTORE) chat3d.jar myself)

chat3d: ../chat3d.jar
	$(JAVA) -Xmx256m -classpath ../chat3d.jar:$(CLASSPATH) lircom.MainWindow lircom.Chat
