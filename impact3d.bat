set DIR=C:\Users\jcarl\lircom
set JARS=%DIR%\m2
set DWN=C:\Users\jcarl\Downloads
set M2=m2
set LIB=C:\Users\jcarl\Downloads\jogamp-all-platforms\lib\windows-amd64

start %DIR%\peer.bat

java -Xmx256m --add-exports java.base/java.lang=ALL-UNNAMED --add-exports java.desktop/sun.awt=ALL-UNNAMED --add-exports java.desktop/sun.java2d=ALL-UNNAMED -Djava.library.path=%LIB% -Xmx256m -cp %DIR%\target\Impact.jar impact.Impact3D
