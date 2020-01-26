KEYSTORE=/Users/johncarlson/mykeystore
CLASSPATH=..:../solitaire.jar:../lircom.jar:/Users/johncarlson/Applications/jogl.jar:/Users/johncarlson/Applications/jogl-natives-macosx.jar:/Users/johncarlson/Applications/jogl-demos-data.jar:/Users/johncarlson/Applications/jogl-demos-util.jar:/Users/johncarlson/Applications/jogl-demos.jar:/Users/johncarlson/Applications/freetts-1/freetts-1.2.1/lib/cmu_time_awb.jar:/Users/johncarlson/Applications/freetts-1/freetts-1.2.1/lib/cmu_us_kal.jar:/Users/johncarlson/Applications/freetts-1/freetts-1.2.1/lib/cmutimelex.jar:/Users/johncarlson/Applications/freetts-1/freetts-1.2.1/lib/en_us.jar:/Users/johncarlson/Applications/freetts-1/freetts-1.2.1/lib/freetts.jar:/Users/johncarlson/Applications/freetts-1/freetts-1.2.1/lib/jsapi.jar:/Users/johncarlson/Applications/freetts-1/freetts-1.2.1/lib/cmudict04.jar:/Users/johncarlson/Applications/freetts-1/freetts-1.2.1/lib/cmulex.jar:/Users/johncarlson/Applications/httpunit-1.6/lib/httpunit.jar:/Users/johncarlson/Applications/httpunit-1.6/jars/nekohtml.jar:/Users/johncarlson/Applications/httpunit-1.6/jars/xercesImpl.jar:/Users/johncarlson/Applications/httpunit-1.6/jars/xmlParserAPIs.jar:/Users/johncarlson/Applications/martyr-0.3.9/martyr.jar


lircom: ../lircom.jar
	java -Dsun.java2d.opengl=True -Xmx256m -classpath $(CLASSPATH) lircom.MainWindow lircom.Chat

irc: ../lircom.jar
	java -classpath $(CLASSPATH) lircom.IRCClient

babelfish: ../lircom.jar
	java -classpath $(CLASSPATH) lircom.BabelFish "thank you"

runestone: ../lircom.jar
	java -Xmx256m -classpath $(CLASSPATH) lircom.Runestone

lircomsign:
	cp ../solitaire.jar ../../lib
	cp ../lircom.jar ../../lib
	cp /Users/johncarlson/Applications/jogl.jar ../../lib
	cp /Users/johncarlson/Applications/jogl-natives-macosx.jar ../../lib
	cp /Users/johncarlson/Applications/jogl-demos-data.jar ../../lib
	cp /Users/johncarlson/Applications/jogl-demos-util.jar ../../lib
	cp /Users/johncarlson/Applications/jogl-demos.jar ../../lib
	cp /Users/johncarlson/Applications/freetts-1/freetts-1.2.1/lib/cmu_time_awb.jar ../../lib
	cp /Users/johncarlson/Applications/freetts-1/freetts-1.2.1/lib/cmu_us_kal.jar ../../lib
	cp /Users/johncarlson/Applications/freetts-1/freetts-1.2.1/lib/cmutimelex.jar ../../lib
	cp /Users/johncarlson/Applications/freetts-1/freetts-1.2.1/lib/en_us.jar ../../lib
	cp /Users/johncarlson/Applications/freetts-1/freetts-1.2.1/lib/freetts.jar ../../lib
	cp /Users/johncarlson/Applications/freetts-1/freetts-1.2.1/lib/jsapi.jar ../../lib
	cp /Users/johncarlson/Applications/freetts-1/freetts-1.2.1/lib/cmudict04.jar ../../lib
	cp /Users/johncarlson/Applications/freetts-1/freetts-1.2.1/lib/cmulex.jar ../../lib
	cp /Users/johncarlson/Applications/httpunit-1.6/lib/httpunit.jar ../../lib
	cp /Users/johncarlson/Applications/httpunit-1.6/jars/nekohtml.jar ../../lib
	cp /Users/johncarlson/Applications/httpunit-1.6/jars/xercesImpl.jar ../../lib
	cp /Users/johncarlson/Applications/httpunit-1.6/jars/xmlParserAPIs.jar ../../lib
	cp /Users/johncarlson/Applications/martyr-0.3.9/martyr.jar ../../lib
	jarsigner -storepass finally -keystore $(KEYSTORE) ../../lib/cmu_time_awb.jar myself
	jarsigner -storepass finally -keystore $(KEYSTORE) ../../lib/cmu_us_kal.jar myself
	jarsigner -storepass finally -keystore $(KEYSTORE) ../../lib/cmudict04.jar myself
	jarsigner -storepass finally -keystore $(KEYSTORE) ../../lib/cmulex.jar myself
	jarsigner -storepass finally -keystore $(KEYSTORE) ../../lib/cmutimelex.jar myself
	jarsigner -storepass finally -keystore $(KEYSTORE) ../../lib/en_us.jar myself
	jarsigner -storepass finally -keystore $(KEYSTORE) ../../lib/freetts.jar myself
	jarsigner -storepass finally -keystore $(KEYSTORE) ../../lib/httpunit.jar myself
	jarsigner -storepass finally -keystore $(KEYSTORE) ../../lib/jogl-demos-data.jar myself
	jarsigner -storepass finally -keystore $(KEYSTORE) ../../lib/jogl-demos-util.jar myself
	jarsigner -storepass finally -keystore $(KEYSTORE) ../../lib/jogl-demos.jar myself
	jarsigner -storepass finally -keystore $(KEYSTORE) ../../lib/jogl.jar myself
	jarsigner -storepass finally -keystore $(KEYSTORE) ../../lib/jsapi.jar myself
	jarsigner -storepass finally -keystore $(KEYSTORE) ../../lib/martyr.jar myself
	jarsigner -storepass finally -keystore $(KEYSTORE) ../../lib/nekohtml.jar myself
	jarsigner -storepass finally -keystore $(KEYSTORE) ../../lib/xercesImpl.jar myself
	jarsigner -storepass finally -keystore $(KEYSTORE) ../../lib/xmlParserAPIs.jar myself
	jarsigner -storepass finally -keystore $(KEYSTORE) ../../lib/lircom.jar myself
	jarsigner -storepass finally -keystore $(KEYSTORE) ../../lib/solitaire.jar myself


speech: ../lircom.jar
	java -Xmx256m -classpath $(CLASSPATH) lircom.Synth


assist: ../lircom.jar
	java -Xmx256m -classpath $(CLASSPATH) lircom.ChatAssist lircom.Chat

channel: ../lircom.jar
	java -Xmx256m -classpath $(CLASSPATH) lircom.MainWindow lircom.Channel

kanal: ../lircom.jar
	java -Xmx256m -classpath $(CLASSPATH) lircom.MainWindow lircom.Kanal

peer: ../lircom.jar
	java -Xmx256m -classpath $(CLASSPATH) lircom.Peer 5891

peer2: ../lircom.jar
	java -Xmx256m -classpath $(CLASSPATH) lircom.Peer 7000

../lircom.jar: *.java
	javac -classpath $(CLASSPATH) *.java
	jar -cmf ../lircom/lircom.manifest ../lircom.jar ../lircom
	(cd ..; jar -cmf lircom/lircom.manifest lircom.jar lircom; jarsigner -storepass finally -keystore $(KEYSTORE) lircom.jar myself)

../chat3d.jar: *.java
	javac -classpath $(CLASSPATH) *.java
	(cd ..; jar -cmf lircom/lircom.manifest chat3d.jar lircom; jarsigner -keystore $(KEYSTORE) chat3d.jar myself)

chat3d: ../chat3d.jar
	java -Dsun.java2d.opengl=True -Xmx256m -classpath ../chat3d.jar:$(CLASSPATH) lircom.MainWindow lircom.Chat
