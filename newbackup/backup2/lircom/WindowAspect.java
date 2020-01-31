package lircom;
import java.awt.Graphics;
/*

aspect WindowAspect {
	pointcut windowConstruct(javax.swing.JFrame w) : target(w) && call(public * javax.swing.JFrame.setVisible(..));
	after(javax.swing.JFrame w) returning: windowConstruct(w) {
		if (w.isVisible()) {
			SVGFE svgfe = new SVGFE(w);
			svgfe.start();
		}
	}
}
*/
