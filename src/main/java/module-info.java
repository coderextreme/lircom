module lircom {
        requires gluegen.rt;
	requires jogl.all;
	requires java.desktop;
	requires java.base;
	requires java.logging;
	requires junit;
	requires java.net.http;
	requires com.fasterxml.jackson.jr.ob;
	requires com.fasterxml.jackson.annotation;
	requires com.fasterxml.jackson.core;
	requires com.fasterxml.jackson.databind;
	requires com.fasterxml.classmate;
	exports lircom;
	exports solitaire;
	exports impact;
	exports impactVL;
}
