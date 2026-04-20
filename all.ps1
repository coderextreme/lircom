$PATH_TO_FX='openjfx-22_windows-x64_bin-sdk\javafx-sdk-22\lib'
$PATH_TO_FX_MOD='javafx-jmods-22'
$KEYSTORE='mykeystore'
$PORT_NUM='8180'
$HOST_PORT='localhost:8180'
$env:CLASSPATH='.'
$env:CLASSPATH+=';C:/Users/jcarl/lircom/target/Impact.jar'
$env:CLASSPATH+=';C:/Users/jcarl/lircom/target/lircom-3.jar'
$env:CLASSPATH+=';C:/Users/jcarl/.m2/repository/org/httpunit/httpunit/1.7.3/httpunit-1.7.3.jar'
$env:CLASSPATH+=';C:/Users/jcarl/.m2/repository/org/htmlunit/neko-htmlunit/4.21.0/neko-htmlunit-4.21.0.jar'
$env:CLASSPATH+=';C:/Users/jcarl/.m2/repository/org/mozilla/rhino/1.9.1/rhino-1.9.1.jar'
$env:CLASSPATH+=';C:/Users/jcarl/.m2/repository/com/fasterxml/jackson/core/jackson-annotations/2.18.6/jackson-annotations-2.18.6.jar'
$env:CLASSPATH+=';C:/Users/jcarl/.m2/repository/com/fasterxml/jackson/core/jackson-core/2.18.6/jackson-core-2.18.6.jar'
$env:CLASSPATH+=';C:/Users/jcarl/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.18.6/jackson-databind-2.18.6.jar'
$env:CLASSPATH+=';C:/Users/jcarl/.m2/repository/com/fasterxml/jackson/jr/jackson-jr-objects/2.18.6/jackson-jr-objects-2.18.6.jar'
$env:CLASSPATH+=';C:/Users/jcarl/.m2/repository/com/squareup/okhttp3/okhttp/4.9.3/okhttp-4.9.3.jar'
$env:CLASSPATH+=';C:/Users/jcarl/.m2/repository/org/jetbrains/kotlin/kotlin-stdlib/1.6.0/kotlin-stdlib-1.6.0.jar'
$env:CLASSPATH+=';C:/Users/jcarl/.m2/repository/com/squareup/okio/okio/2.8.0/okio-2.8.0.jar'

del target\*.jar
mvn clean install
& C:\Users\jcarl\Downloads\apache-ant-1.10.14-bin\apache-ant-1.10.14\bin\ant.bat

& keytool '-genkeypair' '-keyalg' 'DSA' '-keysize' '1024' '-keystore' $KEYSTORE '-alias' 'myself' '-storepass' 'foobar'
Start-Process -NoNewWindow java -ArgumentList @( 'lircom.Peer', $PORT_NUM )
sleep 1
Start-Process -NoNewWindow java -ArgumentList @( '-Xmx256m', '--add-exports', 'java.base/java.lang=ALL-UNNAMED', '--add-exports', 'java.desktop/sun.awt=ALL-UNNAMED', '--add-exports', 'java.desktop/sun.java2d=ALL-UNNAMED', '-jar', 'target/lircom-3-jar-with-dependencies.jar', 'lircom.Chat', 'Christoph', $HOST_PORT )
Start-Process -NoNewWindow java -ArgumentList @( '-Xmx256m', '--add-exports', 'java.base/java.lang=ALL-UNNAMED', '--add-exports', 'java.desktop/sun.awt=ALL-UNNAMED', '--add-exports', 'java.desktop/sun.java2d=ALL-UNNAMED', '-jar', 'target/lircom-3-jar-with-dependencies.jar', 'lircom.Chat', 'John', $HOST_PORT )

Start-Process -NoNewWindow java -ArgumentList @( '-Xmx256m', '--add-exports', 'java.base/java.lang=ALL-UNNAMED', '--add-exports', 'java.desktop/sun.awt=ALL-UNNAMED', '--add-exports', 'java.desktop/sun.java2d=ALL-UNNAMED', 'solitaire.Game', 'dealer', $HOST_PORT )
Start-Process -NoNewWindow java -ArgumentList @( '-Xmx256m', '--add-exports', 'java.base/java.lang=ALL-UNNAMED', '--add-exports', 'java.desktop/sun.awt=ALL-UNNAMED', '--add-exports', 'java.desktop/sun.java2d=ALL-UNNAMED', 'solitaire.Game', 'random', $HOST_PORT )

Start-Process -NoNewWindow java -ArgumentList @( '-Xmx256m', '--add-exports', 'java.base/java.lang=ALL-UNNAMED', '--add-exports', 'java.desktop/sun.awt=ALL-UNNAMED', '--add-exports', 'java.desktop/sun.java2d=ALL-UNNAMED', '-Djava.library.path=C:\Users\jcarl\Downloads\jogamp-all-platforms\lib\windows-amd64', '-javaagent:C:/Users/jcarl/lircom/patch-agent/target/patch-agent-1.0.jar', 'impact.Impact3D', $HOST_PORT )
Start-Process -NoNewWindow java -ArgumentList @( '-Xmx256m', '--add-exports', 'java.base/java.lang=ALL-UNNAMED', '--add-exports', 'java.desktop/sun.awt=ALL-UNNAMED', '--add-exports', 'java.desktop/sun.java2d=ALL-UNNAMED', '-Djava.library.path=C:\Users\jcarl\Downloads\jogamp-all-platforms\lib\windows-amd64', '-javaagent:C:/Users/jcarl/lircom/patch-agent/target/patch-agent-1.0.jar', 'impact.Impact3D', $HOST_PORT )
