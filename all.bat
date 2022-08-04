set JAVAC=javac
set DIR=.
set DWN=C:/Users/john/Downloads
set JAVA=java
set JAR=jar
set KEYSTORE=mykeystore
set M2=m2
set CLASSPATH="%DIR%/target/lircom-2.jar;%DIR%/target/Impact.jar;%DIR%/m2/httpunit-1.7.3.jar;%DIR%/m2/nekohtml-1.9.22.jar;%DIR%/m2/rhino-1.7R4.jar;%DIR%/m2/json-20190722.jar"
set LIB=%DWN%/jogamp-all-platforms/lib/windows-amd64
set KEYSTORE=mykeystore

keytool -genkeypair -keyalg DSA -keysize 1024 -keystore %KEYSTORE% -alias myself -storepass foobar 

mvn install
ant

start %JAVA% -Xmx256m --add-exports java.base/java.lang=ALL-UNNAMED --add-exports java.desktop/sun.awt=ALL-UNNAMED --add-exports java.desktop/sun.java2d=ALL-UNNAMED -Djava.library.path=%LIB% -Xmx256m -cp %CLASSPATH% lircom.Peer 8180
sleep 1
start %JAVA% -Xmx256m -cp %CLASSPATH% lircom.MainWindow lircom.Chat &
start %JAVA% -Xmx256m -cp %CLASSPATH% solitaire.Game dealer &
start %JAVA% -Xmx256m -Djava.library.path=%LIB% --add-exports java.base/java.lang=ALL-UNNAMED --add-exports java.desktop/sun.awt=ALL-UNNAMED --add-exports java.desktop/sun.java2d=ALL-UNNAMED -Djava.library.path=%LIB% -Xmx256m -cp %CLASSPATH% impact.Impact3D &
