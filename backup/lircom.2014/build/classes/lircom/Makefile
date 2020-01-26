KEYSTORE=/Users/johncarlson/mykeystore
SVGCLASSPATH=/Users/johncarlson/Applications/aspectj-1.5.3/lib/aspectjrt.jar:/Users/johncarlson/Applications/batik-1.7/lib/batik-svggen.jar:/Users/johncarlson/Applications/batik-1.7/lib/batik-dom.jar:/Users/johncarlson/Applications/batik-1.7/lib/batik-awt-util.jar:/Users/johncarlson/Applications/batik-1.7/lib/batik-util.jar:/Users/johncarlson/Applications/batik-1.7/lib/xalan-2.6.0.jar:/Users/johncarlson/Applications/batik-1.7/lib/xml-apis.jar:/Users/johncarlson/Applications/batik-1.7/batik-1.7/lib/batik-all.jar
#CLASSPATH=..:../solitaire.jar:../lircom.jar:/Users/johncarlson/Applications/jogl-1_0_0-macosx-universal/lib/jogl.jar:/Users/johncarlson/Applications/jogl-1_0_0-demos/lib/jogl-demos-data.jar:/Users/johncarlson/Applications/jogl-1_0_0-demos/lib/jogl-demos-util.jar:/Users/johncarlson/Applications/jogl-1_0_0-demos/lib/jogl-demos.jar:../../lib/cmu_time_awb.jar:../../lib/cmu_us_kal.jar:../../lib/cmutimelex.jar:../../lib/en_us.jar:../../lib/freetts.jar:../../lib/jsapi.jar:../../lib/cmudict04.jar:../../lib/cmulex.jar:/Users/johncarlson/dev/httpunit/lib/httpunit.jar:../../lib/nekohtml.jar:../../lib/xercesImpl.jar:../../lib/xmlParserAPIs.jar:/Users/johncarlson/dev/martyr/trunk/martyr.jar
CLASSPATH=..:../solitaire.jar:../lircom.jar:/Users/johncarlson/dev/httpunit/lib/httpunit.jar:/Users/johncarlson/dev/martyr/trunk/martyr.jar

CLASSPATH2=..:../orbit.jar:/Users/johncarlson/Applications/jogl/build/jogl.jar:/Users/johncarlson/Applications/jogl-demos/build/jogl-demos-util.jar:/Users/johncarlson/Applications/jogl/www/webstart/jogl-natives-macosx-universal.jar

CLASSPATH3=/Users/johncarlson/dev/martyr/trunk/martyr.jar:../lircom.jar
# JAVAC=/Users/johncarlson/Applications/aspectj-1.5.3/bin/ajc -1.5
JAVAC=javac

lircom: ../lircom.jar
	(export DYLD_LIBRARY_PATH=/Users/johncarlson/Documents/workspace/jogl/build/obj; java -Dsun.java2d.opengl=true -Xmx256m -classpath $(CLASSPATH) lircom.MainWindow lircom.Chat yottzumm)

yottzumm2: ../lircom.jar
	(export DYLD_LIBRARY_PATH=/Users/johncarlson/Documents/workspace/jogl/build/obj; java -Dsun.java2d.opengl=true -Xmx256m -classpath $(CLASSPATH) lircom.MainWindow lircom.Chat yottzumm2)

clean:
	rm *.class ../lircom.jar

orbit: ../orbit.jar
	(export DYLD_LIBRARY_PATH=/Users/johncarlson/Documents/workspace/jogl/build/obj; java -Dsun.java2d.opengl=true -Xmx256m -classpath $(CLASSPATH2) lircom.Orbit2)

irc: ../lircom.jar
	java -classpath $(CLASSPATH3) lircom.IRCClient afternet.config symbion.config

babelfish: ../lircom.jar
	java -classpath $(CLASSPATH) lircom.BabelFish "thank you"

runestone: ../lircom.jar
	java -Xmx256m -classpath $(CLASSPATH) lircom.Runestone

Heathens: ../lircom.jar
	java -Xmx256m -classpath $(CLASSPATH) lircom.Heathens


signall:
	-(cp `echo $(CLASSPATH) | tr : '\n'` ../../lib)
	(for i in ../../lib/*jar; do jarsigner -storepass argus1 -keystore $(KEYSTORE) $$i myself; done)

speech: ../lircom.jar
	java -Xmx256m -classpath $(CLASSPATH) lircom.Synth


assist: ../lircom.jar
	java -Xmx256m -classpath $(CLASSPATH) lircom.ChatAssist lircom.Chat

channel: ../lircom.jar
	java -Xmx256m -classpath $(CLASSPATH) lircom.MainWindow lircom.Channel

kanal: ../lircom.jar
	java -Xmx256m -classpath $(CLASSPATH) lircom.MainWindow lircom.Kanal

peer: ../lircom.jar
	java -Xmx256m -classpath $(CLASSPATH) lircom.Peer 8180

peer2: ../lircom.jar
	java -Xmx256m -classpath $(CLASSPATH) lircom.Peer 7000

../orbit.jar: Orbit2.java Animator.java
	$(JAVAC) -target 1.5 -classpath $(CLASSPATH2) Orbit2.java Animator.java
	(cd ..; jar -cmf lircom/lircom.manifest orbit.jar lircom/Orbit2*class lircom/Animator*class; jarsigner -storepass argus1 -keystore $(KEYSTORE) orbit.jar myself)
JAVACODE2=Animator.java Synth.java
JAVACODE= BabelFish.java Bridge.java Channel.java Chat.java Client.java ClientConnect.java ClientException.java ClientOnServer.java Errors.java ExampleFileFilter.java Hyperactive.java IRCBridge.java IRCClient.java Identd.java Kanal.java Lircom.java MainApplet.java MainWindow.java Message.java Peer.java PossibleConnection.java ReceiveChatInterface.java Runestone.java SendChatInterface.java SendCommandInterface.java ServerAdvertise.java Smiley.java Trade.java Heathens.java
OTHERJAVACODE= SVGFE.java WindowAspect.java
../lircom.jar: $(JAVACODE)
	$(JAVAC) -target 1.5 -classpath $(CLASSPATH) $(JAVACODE)
	jar -cmf ../lircom/lircom.manifest ../lircom.jar ../lircom
	(cd ..; jar -cmf lircom/lircom.manifest lircom.jar lircom; jarsigner -storepass argus1 -keystore $(KEYSTORE) lircom.jar myself)
	# cp ../lircom.jar /Users/johncarlson/Applications/apache-tomcat-6.0.14/webapps/web/WEB-INF/lib/

../chat3d.jar: $(JAVACODE)
	$(JAVAC) -target 1.5 -classpath $(CLASSPATH) $(JAVACODE)
	(cd ..; jar -cmf lircom/lircom.manifest chat3d.jar lircom; jarsigner -keystore $(KEYSTORE) chat3d.jar myself)

chat3d: ../chat3d.jar
	java -Dsun.java2d.opengl=true -Xmx256m -classpath ../chat3d.jar:$(CLASSPATH) lircom.MainWindow lircom.Chat
