KEYSTORE=/home/carlsonj/mykeystore
CLASSPATH="..:../solitaire.jar:../lircom.jar:/home/carlsonj/other/jogl/www/webstart/jogl.jar:/home/carlsonj/other/jogl/www/webstart/jogl-natives-linux.jar:/home/carlsonj/other/jogl-demos/www/webstart/jogl-demos-data.jar:/home/carlsonj/other/jogl-demos/www/webstart/jogl-demos-util.jar:/home/carlsonj/other/jogl-demos/www/webstart/jogl-demos.jar:/home/carlsonj/javassist-3.0/javassist.jar:../../lib/cmu_time_awb.jar:../../lib/cmu_us_kal.jar:../../lib/cmutimelex.jar:../../lib/en_us.jar:../../lib/freetts.jar:../../lib/jsapi.jar:../../lib/cmudict04.jar:../../lib/cmulex.jar:../../lib/httpunit.jar:../../lib/nekohtml.jar:../../lib/xercesImpl.jar:../../lib/xmlParserAPIs.jar"

lircom: ../lircom.jar
	java -Dsun.java2d.opengl=True -Xmx256m -classpath $(CLASSPATH) lircom.MainWindow lircom.Chat

babelfish: ../lircom.jar
	java -classpath $(CLASSPATH) lircom.BabelFish "thank you"

runestone: ../lircom.jar
	java -Xmx256m -classpath $(CLASSPATH) lircom.Runestone

babelsign:
	jarsigner -keystore $(KEYSTORE) ../../lib/nekohtml.jar myself
	jarsigner -keystore $(KEYSTORE) ../../lib/xercesImpl.jar myself
	jarsigner -keystore $(KEYSTORE) ../../lib/xmlParserAPIs.jar myself

runesign:
	jarsigner -keystore $(KEYSTORE) ../httpunit.jar myself

speech: ../lircom.jar
	java -Xmx256m -classpath $(CLASSPATH) lircom.Synth

joglsign:
	jarsigner -keystore $(KEYSTORE) ../jogl-demos-data.jar myself
	jarsigner -keystore $(KEYSTORE) ../jogl-demos-util.jar myself
	jarsigner -keystore $(KEYSTORE) ../jogl-demos.jar myself

speechsign:
	jarsigner -keystore $(KEYSTORE) ../cmulex.jar myself
	jarsigner -keystore $(KEYSTORE) ../cmudict04.jar myself
	jarsigner -keystore $(KEYSTORE) ../cmu_us_kal.jar myself
	jarsigner -keystore $(KEYSTORE) ../cmu_time_awb.jar myself
	jarsigner -keystore $(KEYSTORE) ../cmutimelex.jar myself
	jarsigner -keystore $(KEYSTORE) ../en_us.jar myself
	jarsigner -keystore $(KEYSTORE) ../freetts.jar myself
	jarsigner -keystore $(KEYSTORE) ../jsapi.jar myself

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
	(cd ..; jar -cmf lircom/lircom.manifest lircom.jar lircom; jarsigner -keystore $(KEYSTORE) lircom.jar myself)

../chat3d.jar: *.java
	javac -classpath $(CLASSPATH) *.java
	(cd ..; jar -cmf lircom/lircom.manifest chat3d.jar lircom; jarsigner -keystore $(KEYSTORE) chat3d.jar myself)

chat3d:
	# /home/carlsonj/jdk1.6.0/bin/java -Dsun.java2d.opengl=True -Xmx256m -classpath ../chat3d.jar:$(CLASSPATH) lircom.MainWindow lircom.Chat
	java -Dsun.java2d.opengl=True -Xmx256m -classpath ../chat3d.jar:$(CLASSPATH) lircom.MainWindow lircom.Chat
