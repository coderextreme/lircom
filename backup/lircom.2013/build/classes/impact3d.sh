#!/bin/bash -x
export DYLD_LIBRARY_PATH=/Users/johncarlson/dev/jogl-1.1.0-macosx-universal/lib/
java -Xmx256m -Djava.library.path=/Users/johncarlson/dev/jogl-1.1.0-macosx-universal/lib -classpath /Users/johncarlson/dev/jogl-1.1.0-macosx-universal/lib//jogl.jar:impact3d.jar:/Users/johncarlson/dev/jogl-1.1.0-macosx-universal/lib/gluegen-rt.jar:. impact.Impact3D
