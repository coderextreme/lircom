$PATH_TO_FX='openjfx-22_windows-x64_bin-sdk\javafx-sdk-22\lib'
$PATH_TO_FX_MOD='javafx-jmods-22'
$KEYSTORE='mykeystore'
$env:CLASSPATH='.'
$env:CLASSPATH+=';C:/Users/john/lircom/target/Impact.jar'
$env:CLASSPATH+=';C:/Users/john/lircom/target/lircom-3.jar'
$env:CLASSPATH+=';C:/Users/john/lircom/m2/httpunit-1.7.3.jar'
$env:CLASSPATH+=';C:/Users/john/lircom/m2/nekohtml-1.9.22.jar'
$env:CLASSPATH+=';C:/Users/john/lircom/m2/rhino-1.7R4.jar'
$env:CLASSPATH+=';C:/Users/john/.m2/repository/com/fasterxml/jackson/core/jackson-annotations/2.17.1/jackson-annotations-2.17.1.jar'
$env:CLASSPATH+=';C:/Users/john/.m2/repository/com/fasterxml/jackson/core/jackson-core/2.17.1/jackson-core-2.17.1.jar'
$env:CLASSPATH+=';C:/Users/john/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.17.1/jackson-databind-2.17.1.jar'
$env:CLASSPATH+=';C:/Users/john/.m2/repository/com/fasterxml/jackson/jr/jackson-jr-objects/2.17.1/jackson-jr-objects-2.17.1.jar'
$env:CLASSPATH+=';C:/Users/john/.m2/repository/com/fasterxml/classmate/1.7.0/classmate-1.7.0.jar'

del target\*.jar
mvn clean install
& C:\apache-ant-1.10.12-bin\apache-ant-1.10.12\bin\ant.bat

Start-Process -NoNewWindow java -ArgumentList @( 'lircom.Peer', 8180 )
Start-Process -NoNewWindow java -ArgumentList @( '-Xmx256m', '--add-exports', 'java.base/java.lang=ALL-UNNAMED', '--add-exports', 'java.desktop/sun.awt=ALL-UNNAMED', '--add-exports', 'java.desktop/sun.java2d=ALL-UNNAMED', 'solitaire.Game', 'dealer', 'localhost:8180' )
Start-Process -NoNewWindow java -ArgumentList @( '-Xmx256m', '--add-exports', 'java.base/java.lang=ALL-UNNAMED', '--add-exports', 'java.desktop/sun.awt=ALL-UNNAMED', '--add-exports', 'java.desktop/sun.java2d=ALL-UNNAMED', 'solitaire.Game', 'random', 'localhost:8180' )
