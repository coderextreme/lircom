KEYSTORE=/Users/johncarlson/mykeystore
CLASSPATH=..:../solitaire.jar:../lircom.jar:/Users/johncarlson/Applications/jogl-1_0_0-macosx-universal/lib/jogl.jar:/Users/johncarlson/Applications/jogl-1_0_0-demos/lib/jogl-demos-data.jar:/Users/johncarlson/Applications/jogl-1_0_0-demos/lib/jogl-demos-util.jar:/Users/johncarlson/Applications/jogl-1_0_0-demos/lib/jogl-demos.jar:../../lib/cmu_time_awb.jar:../../lib/cmu_us_kal.jar:../../lib/cmutimelex.jar:../../lib/en_us.jar:../../lib/freetts.jar:../../lib/jsapi.jar:../../lib/cmudict04.jar:../../lib/cmulex.jar:../../lib/httpunit.jar:../../lib/nekohtml.jar:../../lib/xercesImpl.jar:../../lib/xmlParserAPIs.jar:../../lib/martyr.jar
CLASSPATH2=..:../orbit.jar:/Users/johncarlson/Applications/jogl/build/jogl.jar:/Users/johncarlson/Applications/jogl-demos/build/jogl-demos-util.jar:/Users/johncarlson/Applications/jogl/www/webstart/jogl-natives-macosx-universal.jar
CLASSPATH3=../../lib/martyr.jar:../lircom.jar

lircom: ../lircom.jar
	(export DYLD_LIBRARY_PATH=/Users/johncarlson/Documents/workspace/jogl/build/obj; java -Dsun.java2d.opengl=true -Xmx256m -classpath $(CLASSPATH) lircom.MainWindow lircom.Chat yottzumm)

orbit: ../orbit.jar
	(export DYLD_LIBRARY_PATH=/Users/johncarlson/Documents/workspace/jogl/build/obj; java -Dsun.java2d.opengl=true -Xmx256m -classpath $(CLASSPATH2) lircom.Orbit2)

irc: ../lircom.jar
	java -classpath $(CLASSPATH3) lircom.IRCClient afternet.config symbion.config

babelfish: ../lircom.jar
	java -classpath $(CLASSPATH) lircom.BabelFish "thank you"

runestone: ../lircom.jar
	java -Xmx256m -classpath $(CLASSPATH) lircom.Runestone

joglsign:
	cp /Users/johncarlson/Applications/jogl/www/webstart/jogl-natives-macosx-universal.jar ../../lib
	cp /Users/johncarlson/Applications/jogl/www/webstart/jogl-natives-windows-i586.jar ../../lib
	cp /Users/johncarlson/Applications/jogl/www/webstart/jogl-natives-linux-i586.jar ../../lib
	cp /Users/johncarlson/Applications/jogl/www/webstart/jogl.jar ../../lib
	jarsigner -storepass argus1 -keystore $(KEYSTORE) ../../lib/jogl-natives-macosx-universal.jar myself
	jarsigner -storepass argus1 -keystore $(KEYSTORE) ../../lib/jogl-natives-windows-i586.jar myself
	jarsigner -storepass argus1 -keystore $(KEYSTORE) ../../lib/jogl-natives-linux-i586.jar myself
	jarsigner -storepass argus1 -keystore $(KEYSTORE) ../../lib/jogl.jar myself

signprepare:
	cp ../solitaire.jar ../../lib
	cp ../lircom.jar ../../lib
	cp /Users/johncarlson/Applications/jogl/build/jogl.jar ../../lib
	cp /Users/johncarlson/Applications/jogl-demos/build/jogl-demos-data.jar ../../lib
	cp /Users/johncarlson/Applications/jogl-demos/build/jogl-demos-util.jar ../../lib
	cp /Users/johncarlson/Applications/jogl-demos/build/jogl-demos.jar ../../lib
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

sign:
	jarsigner -storepass argus1 -keystore $(KEYSTORE) ../../lib/jnlp.jar myself
	jarsigner -storepass argus1 -keystore $(KEYSTORE) ../../lib/cmu_time_awb.jar myself
	jarsigner -storepass argus1 -keystore $(KEYSTORE) ../../lib/cmu_us_kal.jar myself
	jarsigner -storepass argus1 -keystore $(KEYSTORE) ../../lib/cmudict04.jar myself
	jarsigner -storepass argus1 -keystore $(KEYSTORE) ../../lib/cmulex.jar myself
	jarsigner -storepass argus1 -keystore $(KEYSTORE) ../../lib/cmutimelex.jar myself
	jarsigner -storepass argus1 -keystore $(KEYSTORE) ../../lib/en_us.jar myself
	jarsigner -storepass argus1 -keystore $(KEYSTORE) ../../lib/freetts.jar myself
	jarsigner -storepass argus1 -keystore $(KEYSTORE) ../../lib/httpunit.jar myself
	jarsigner -storepass argus1 -keystore $(KEYSTORE) ../../lib/jogl.jar myself
	jarsigner -storepass argus1 -keystore $(KEYSTORE) ../../lib/jogl-demos-data.jar myself
	jarsigner -storepass argus1 -keystore $(KEYSTORE) ../../lib/jogl-demos-util.jar myself
	jarsigner -storepass argus1 -keystore $(KEYSTORE) ../../lib/jogl-demos.jar myself
	jarsigner -storepass argus1 -keystore $(KEYSTORE) ../../lib/jsapi.jar myself
	jarsigner -storepass argus1 -keystore $(KEYSTORE) ../../lib/martyr.jar myself
	jarsigner -storepass argus1 -keystore $(KEYSTORE) ../../lib/nekohtml.jar myself
	jarsigner -storepass argus1 -keystore $(KEYSTORE) ../../lib/xercesImpl.jar myself
	jarsigner -storepass argus1 -keystore $(KEYSTORE) ../../lib/xmlParserAPIs.jar myself
	jarsigner -storepass argus1 -keystore $(KEYSTORE) ../../lib/lircom.jar myself
	jarsigner -storepass argus1 -keystore $(KEYSTORE) ../../lib/solitaire.jar myself


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

../orbit.jar: Orbit2.java Animator.java
	javac -target 1.5 -classpath $(CLASSPATH2) Orbit2.java Animator.java
	(cd ..; jar -cmf lircom/lircom.manifest orbit.jar lircom/Orbit2*class lircom/Animator*class; jarsigner -storepass argus1 -keystore $(KEYSTORE) orbit.jar myself)
../lircom.jar: *.java
	javac -target 1.5 -classpath $(CLASSPATH) *.java
	jar -cmf ../lircom/lircom.manifest ../lircom.jar ../lircom
	(cd ..; jar -cmf lircom/lircom.manifest lircom.jar lircom; jarsigner -storepass argus1 -keystore $(KEYSTORE) lircom.jar myself)

../chat3d.jar: *.java
	javac -classpath $(CLASSPATH) *.java
	(cd ..; jar -cmf lircom/lircom.manifest chat3d.jar lircom; jarsigner -keystore $(KEYSTORE) chat3d.jar myself)

chat3d: ../chat3d.jar
	java -Dsun.java2d.opengl=true -Xmx256m -classpath ../chat3d.jar:$(CLASSPATH) lircom.MainWindow lircom.Chat
