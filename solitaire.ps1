$PATH_TO_FX='openjfx-22_windows-x64_bin-sdk\javafx-sdk-22\lib'
$PATH_TO_FX_MOD='javafx-jmods-22'
$KEYSTORE='mykeystore'
$PORT_NUM='8180'
$HOST_PORT='localhost:8180'
$env:CLASSPATH='.'
$env:CLASSPATH+=';C:/Users/jcarl/lircom/target/Impact.jar'
$env:CLASSPATH+=';C:/Users/jcarl/lircom/target/lircom-3.jar'
$env:CLASSPATH+=';C:/Users/jcarl/lircom/m2/httpunit-1.7.3.jar'
$env:CLASSPATH+=';C:/Users/jcarl/lircom/m2/nekohtml-1.9.22.jar'
$env:CLASSPATH+=';C:/Users/jcarl/lircom/m2/rhino-1.7R4.jar'
$env:CLASSPATH+=';C:/Users/jcarl/.m2/repository/com/fasterxml/jackson/core/jackson-annotations/2.18.6/jackson-annotations-2.18.6.jar'
$env:CLASSPATH+=';C:/Users/jcarl/.m2/repository/com/fasterxml/jackson/core/jackson-core/2.18.6/jackson-core-2.18.6.jar'
$env:CLASSPATH+=';C:/Users/jcarl/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.18.6/jackson-databind-2.18.6.jar'
$env:CLASSPATH+=';C:/Users/jcarl/.m2/repository/com/fasterxml/jackson/jr/jackson-jr-objects/2.18.6/jackson-jr-objects-2.18.6.jar'
$env:CLASSPATH+=';C:/Users/jcarl/.m2/repository/com/fasterxml/classmate/1.7.0/classmate-1.7.0.jar'

del target\*.jar
mvn clean install
& C:\Users\jcarl\Downloads\apache-ant-1.10.14-bin\apache-ant-1.10.14\bin\ant.bat

Start-Process -NoNewWindow java -ArgumentList @( 'lircom.Peer', 8180 )
Start-Process -NoNewWindow java -ArgumentList @( '-Xmx256m', '--add-exports', 'java.base/java.lang=ALL-UNNAMED', '--add-exports', 'java.desktop/sun.awt=ALL-UNNAMED', '--add-exports', 'java.desktop/sun.java2d=ALL-UNNAMED', 'solitaire.Game', 'display', $HOST_PORT )
Start-Process -NoNewWindow java -ArgumentList @( '-Xmx256m', '--add-exports', 'java.base/java.lang=ALL-UNNAMED', '--add-exports', 'java.desktop/sun.awt=ALL-UNNAMED', '--add-exports', 'java.desktop/sun.java2d=ALL-UNNAMED', 'solitaire.Game', 'random', $HOST_PORT )
