module lircom {
        requires gluegen.rt;
	requires jogl.all;
	requires java.desktop;
	requires java.base;
	requires java.logging;
	requires junit;
	requires java.net.http;
	requires com.fasterxml.jackson.databind;
	exports lircom;
	exports solitaire;
	exports impact;
}
