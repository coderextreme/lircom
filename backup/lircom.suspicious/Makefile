KEYSTORE=/home/carlsonj/mykeystore
CLASSPATH="lib/solitaire.jar:lib/lircom.jar:lib/jogl.jar:lib/jogl-natives-linux.jar:lib/jogl-demos-data.jar:lib/jogl-demos-util.jar:lib/jogl-demos.jar:lib/javassist.jar:lib/cmu_time_awb.jar:lib/cmu_us_kal.jar:lib/cmutimelex.jar:lib/en_us.jar:lib/freetts.jar:lib/jsapi.jar:lib/cmudict04.jar:lib/cmulex.jar:lib/httpunit.jar"

lircom: lib/lircom.jar
	java -Dsun.java2d.opengl=True -Xmx256m -classpath $(CLASSPATH) lircom.MainWindow lircom.Chat

runestone: lib/lircom.jar
	java -Xmx256m -classpath $(CLASSPATH) lircom.Runestone

runesign:
	jarsigner -keystore $(KEYSTORE) lib/httpunit.jar myself

speech: lib/lircom.jar
	java -Xmx256m -classpath $(CLASSPATH) lircom.Synth

joglsign:
	jarsigner -keystore $(KEYSTORE) lib/jogl-demos-data.jar myself
	jarsigner -keystore $(KEYSTORE) lib/jogl-demos-util.jar myself
	jarsigner -keystore $(KEYSTORE) lib/jogl-demos.jar myself

speechsign:
	jarsigner -keystore $(KEYSTORE) lib/cmulex.jar myself
	jarsigner -keystore $(KEYSTORE) lib/cmudict04.jar myself
	jarsigner -keystore $(KEYSTORE) lib/cmu_us_kal.jar myself
	jarsigner -keystore $(KEYSTORE) lib/cmu_time_awb.jar myself
	jarsigner -keystore $(KEYSTORE) lib/cmutimelex.jar myself
	jarsigner -keystore $(KEYSTORE) lib/en_us.jar myself
	jarsigner -keystore $(KEYSTORE) lib/freetts.jar myself
	jarsigner -keystore $(KEYSTORE) lib/jsapi.jar myself

assist: lib/lircom.jar
	java -Xmx256m -classpath $(CLASSPATH) lircom.ChatAssist lircom.Chat

channel: lib/lircom.jar
	java -Xmx256m -classpath $(CLASSPATH) lircom.MainWindow lircom.Channel

kanal: lib/lircom.jar
	java -Xmx256m -classpath $(CLASSPATH) lircom.MainWindow lircom.Kanal

peer: lib/lircom.jar
	java -Xmx256m -classpath $(CLASSPATH) lircom.Peer 5891

peer2: lib/lircom.jar
	java -Xmx256m -classpath $(CLASSPATH) lircom.Peer 7000

lib/lircom.jar: src/lircom/*.java
	jarsigner -keystore $(KEYSTORE) lib/lircom.jar myself

lib/chat3d.jar: src/lircom/*.java
	jarsigner -keystore $(KEYSTORE) lib/chat3d.jar myself

chat3d:
	java -Xmx256m -classpath lib/chat3d.jar:$(CLASSPATH) lircom.MainWindow lircom.Chat
################################################################################
# solitaire
random: lib/solitaire.jar
	java -Xmx256m -classpath ${CLASSPATH} solitaire.Game random localhost 5891

methodical: lib/solitaire.jar
	java -Xmx256m -classpath ${CLASSPATH} solitaire.Game methodical localhost 5891

display: lib/solitaire.jar
	java -Xmx256m -classpath ${CLASSPATH} solitaire.Game display localhost 5891

dealer: lib/solitaire.jar
	java -Xmx256m -classpath ${CLASSPATH} solitaire.Game dealer localhost 5891

lib/solitaire.jar: src/solitaire/*.java
	jarsigner -keystore $(KEYSTORE) solitaire.jar myself

clean:
	rm *.class
