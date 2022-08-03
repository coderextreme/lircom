JAVAC=javac
JAVA=java
JAR=jar
KEYSTORE=../../../../mykeystore
M2=../../../../m2
CLASSPATH="../../../../target/lircom-2.jar;../../../../m2/httpclient-4.5.13.jar;../../../../m2/htmlunit-2.55.0.jar;../../../../m2/httpunit-1.7.3.jar;../../../../m2/nekohtml-1.9.22.jar;../../../../m2/xml-apis-1.4.01.jar;../../../../m2/js-1.7R2.jar;../../../../m2/json-20190722.jar;.."

BFCLASSPATH="..;../../../../m2/xml-apis.1.4.01.jar;../../../../m2/js-1.7R2.jar"
CLASSPATH3="..;lircom.jar;../../../../target/lircom-2.jar"

FXCLASSPATH=".;..;../../../../target/lircom-2.jar;C:/Users/john/Downloads/openjfx-18.0.2_windows-x64_bin-sdk/javafx-sdk-18.0.2/lib/javafx.base.jar;C:/Users/john/Downloads/openjfx-18.0.2_windows-x64_bin-sdk/javafx-sdk-18.0.2/lib/javafx.controls.jar;C:/Users/john/Downloads/openjfx-18.0.2_windows-x64_bin-sdk/javafx-sdk-18.0.2/lib/javafx.fxml.jar;C:/Users/john/Downloads/openjfx-18.0.2_windows-x64_bin-sdk/javafx-sdk-18.0.2/lib/javafx.graphics.jar;C:/Users/john/Downloads/openjfx-18.0.2_windows-x64_bin-sdk/javafx-sdk-18.0.2/lib/javafx.media.jar;C:/Users/john/Downloads/openjfx-18.0.2_windows-x64_bin-sdk/javafx-sdk-18.0.2/lib/javafx.swing.jar;C:/Users/john/Downloads/openjfx-18.0.2_windows-x64_bin-sdk/javafx-sdk-18.0.2/lib/javafx.web.jar;$(M2)/xmlParserAPIs-2.6.1.jar;$(M2)/nekohtml-1.9.22.jar;..;../../../../target/lircom-2.jar;$(M2)/httpunit-1.7.3.jar;$(M2)/servlet-api-2.4.jar;$(M2)/js-1.6R5.jar"

JAVA_LIB_PATH=C:/Users/coderextreme/Downloads/jogamp-all-platforms/lib/windows-amd64/ 

MODULE_CONFIG=--module-path C:/Users/john/Downloads/openjfx-18.0.2_windows-x64_bin-sdk/javafx-sdk-18.0.2/lib --add-modules javafx.controls,javafx.fxml --add-exports java.base/jdk.internal.misc=ALL-UNNAMED --add-exports javafx.controls/com.sun.javafx.scene.control.behavior=ALL-UNNAMED --add-exports javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED --add-exports javafx.controls/com.sun.javafx.scene.control.inputmap=ALL-UNNAMED --add-exports javafx.graphics/com.sun.javafx.sg.prism=ALL-UNNAMED --add-exports javafx.graphics/com.sun.javafx.scene=ALL-UNNAMED --add-exports javafx.graphics/com.sun.javafx.util=ALL-UNNAMED --add-exports javafx.base/com.sun.javafx.logging=ALL-UNNAMED --add-exports javafx.graphics/com.sun.prism=ALL-UNNAMED --add-exports javafx.graphics/com.sun.glass.ui=ALL-UNNAMED --add-exports javafx.graphics/com.sun.javafx.geom.transform=ALL-UNNAMED --add-exports javafx.graphics/com.sun.javafx.tk=ALL-UNNAMED --add-exports javafx.graphics/com.sun.javafx.font=ALL-UNNAMED --add-exports javafx.graphics/com.sun.glass.utils=ALL-UNNAMED --add-exports javafx.graphics/com.sun.javafx.text=ALL-UNNAMED --add-exports javafx.graphics/com.sun.javafx.scene.text=ALL-UNNAMED --add-exports javafx.graphics/com.sun.javafx.scene.input=ALL-UNNAMED --add-exports javafx.graphics/com.sun.javafx.application=ALL-UNNAMED --add-exports javafx.graphics/com.sun.javafx.scene.traversal=ALL-UNNAMED --add-exports javafx.graphics/com.sun.javafx.geom=ALL-UNNAMED --add-exports javafx.graphics/com.sun.prism.paint=ALL-UNNAMED --add-exports javafx.graphics/com.sun.scenario.effect=ALL-UNNAMED

JAVACODE=../lircom/*.java

fxlircom: lircom.jar
	$(JAVA) -Xmx256m -Djava.library.path=$(JAVA_LIB_PATH) $(MODULE_CONFIG) -cp $(FXCLASSPATH) lircom.MainApplet lircom.Chat belinda

fxyottzumm: lircom.jar
	$(JAVA) -Xmx256m -Djava.library.path=$(JAVA_LIB_PATH) $(MODULE_CONFIG) -cp $(FXCLASSPATH) lircom.MainApplet lircom.Chat Annie

fxchannel: lircom.jar
	$(JAVA) -Xmx256m -Djava.library.path=$(JAVA_LIB_PATH) $(MODULE_CONFIG) -cp $(FXCLASSPATH) lircom.MainApplet lircom.Channel

fxkanal: lircom.jar
	$(JAVA) -Xmx256m -Djava.library.path=$(JAVA_LIB_PATH) $(MODULE_CONFIG) -cp $(FXCLASSPATH) lircom.MainApplet lircom.Kanal

lircom.jar: $(JAVACODE)
	$(JAVAC) --module-path C:/Users/john/Downloads/openjfx-18.0.2_windows-x64_bin-sdk/javafx-sdk-18.0.2/lib --add-modules javafx.controls,javafx.fxml --add-exports java.base/jdk.internal.misc=ALL-UNNAMED -encoding UTF-8 -target 18 -cp $(FXCLASSPATH) $(JAVACODE)
	(cd ..; $(JAR) -cmf ../../../fxlircom.manifest lircom.jar lircom/*.class lircom/*.jpg lircom/*.css)
	cp ../lircom.jar .
	jarsigner -storepass foobar -keystore $(KEYSTORE) lircom.jar myself

lircom: lircom.jar
	$(JAVA) -Xmx256m -Djava.library.path=$(JAVA_LIB_PATH) $(MODULE_CONFIG) -cp $(FXCLASSPATH) lircom.MainWindow lircom.Chat belinda

yottzumm: lircom.jar
	$(JAVA) -Xmx256m -Djava.library.path=$(JAVA_LIB_PATH) $(MODULE_CONFIG) -cp $(FXCLASSPATH) lircom.MainWindow lircom.Chat Annie

channel: lircom.jar
	$(JAVA) -Xmx256m -Djava.library.path=$(JAVA_LIB_PATH) $(MODULE_CONFIG) -cp $(FXCLASSPATH) lircom.MainWindow lircom.Channel

kanal: lircom.jar
	$(JAVA) -Xmx256m -Djava.library.path=$(JAVA_LIB_PATH) $(MODULE_CONFIG) -cp $(FXCLASSPATH) lircom.MainWindow lircom.Kanal

$(KEYSTORE):
	keytool -genkeypair -keyalg DSA -keysize 1024 -keystore $(KEYSTORE) -alias myself -storepass foobar 
clean:
	rm *.class lircom.jar

irc: lircom.jar
	$(JAVA) -cp $(CLASSPATH3) lircom.IRCClient afternet.config symbion.config

wikispeak: lircom.jar
	$(JAVA) -cp $(CLASSPATH3) lircom.IRCClient ircstorm.config

babelfish: lircom.jar
	$(JAVA) -cp $(BFCLASSPATH) lircom.BabelFish "thank you"

runestone: lircom.jar
	$(JAVA) -Xmx256m -cp $(CLASSPATH) lircom.Runestone

Heathens: lircom.jar
	$(JAVA) -Xmx256m -cp $(CLASSPATH) lircom.Heathens


signall:
	(for i in ../../../../target/*jar ../../../../m2/*jar ../*/*.jar; do jarsigner -storepass foobar -keystore $(KEYSTORE) $$i myself; done)

speech: lircom.jar
	$(JAVA) -Xmx256m -cp $(CLASSPATH) lircom.Synth


assist: lircom.jar
	$(JAVA) -Xmx256m -cp $(CLASSPATH) lircom.ChatAssist lircom.Chat

peer: lircom.jar
	$(JAVA) -Xmx256m -cp $(CLASSPATH) lircom.Peer 8180

peer2: lircom.jar
	$(JAVA) -Xmx256m -cp $(CLASSPATH) lircom.Peer 7000
