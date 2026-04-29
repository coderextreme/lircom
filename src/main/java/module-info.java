module lircom {
    requires transitive gluegen.rt;              // automatic — suppressed via -Xlint
    requires transitive jogl.all;                // automatic — suppressed via -Xlint
    requires transitive java.desktop;
    requires java.base;
    requires java.logging;
    requires java.net.http;
    requires com.fasterxml.jackson.jr.ob;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.classmate;
    requires okhttp3;                 // automatic — suppressed via -Xlint
    exports lircom;
    exports solitaire;
    exports impact;
    exports impactVL;
}
