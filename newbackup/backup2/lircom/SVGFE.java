//package lircom;
//import java.awt.*;
//import java.awt.event.*;
//import java.io.*;
//import java.util.*;
//
//import org.apache.batik.dom.GenericDOMImplementation;
//import org.apache.batik.svggen.*;
//
//import org.w3c.dom.Document;
//import org.w3c.dom.DOMImplementation;
//
//public class SVGFE extends Thread {
//    private Window w = null;
//    private static GenericImageHandler ihandler = null;
//    private boolean running = false;
//    private static java.util.List<Window> managedWindows = new Vector<Window>();
//    static {
//	try {
//		ihandler = new CachedImageHandlerPNGEncoder("/Users/johncarlson/Applications/apache-tomcat-6.0.14/webapps/web/", ".");
//	} catch (Exception e) {
//		e.printStackTrace();
//	}
//    }
//
//    public SVGFE(Window w) {
//	this.w = w;
//	managedWindows.add(this.w);
//    }
//    public static java.util.List<Window> getWindows() {
//	return managedWindows;
//    }
//    public void run() {
//	running = true;
//	w.addWindowListener(new WindowAdapter() {
//		public void windowClosing(WindowEvent ev) {
//			SVGFE.this.running = false;
//		}
//	});
//	for (int i = 0;  running; i++) {
//		// Get a DOMImplementation.
//		DOMImplementation domImpl =
//		    GenericDOMImplementation.getDOMImplementation();
//
//		// Create an instance of org.w3c.dom.Document.
//		String svgNS = "http://www.w3.org/2000/svg";
//		Document document = domImpl.createDocument(svgNS, "svg", null);
//		SVGGeneratorContext ctx = SVGGeneratorContext.createDefault(document);
//		//ctx.setComment("Generated from a Swing Application");
//		//ctx.setEmbeddedFontsOn(true);
//		ctx.setGenericImageHandler(ihandler);
//
//
//		// Create an instance of the SVG Generator.
//		SVGGraphics2D svgGenerator = new SVGGraphics2D(ctx, false);
//
//		// Ask the test to render into the SVG Graphics2D implementation.
//		w.paintAll(svgGenerator);
//
//		// Finally, stream out SVG to the standard output using
//		// UTF-8 encoding.
//		boolean useCSS = false; // we want to use CSS style attributes
//		try {
//			Writer out = new FileWriter("/Users/johncarlson/Applications/apache-tomcat-6.0.14/webapps/web/"+Integer.toString(w.hashCode())+"_"+i+".svg");
//			svgGenerator.stream(out, useCSS);
//			out.close();
//			Thread.sleep(500);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//    }
//}
