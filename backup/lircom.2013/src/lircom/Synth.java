package lircom;
/**
 * Copyright 2003 Sun Microsystems, Inc.
 *
 * See the file "license.terms" for information on usage and
 * redistribution of this file, and for a DISCLAIMER OF ALL
 * WARRANTIES.
 */

import com.sun.speech.freetts.jsapi.FreeTTSEngineCentral; 

import java.util.Locale;

import javax.speech.EngineList; 
import javax.speech.EngineCreate; 
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;

/**
 * A talking clock powered by FreeTTS.
 */
public class Synth {

    private Synthesizer synthesizer;


    /**
     * Creates the synthesizer, called by the constructor.
     */
    public Synth() {

        try {
	    javax.speech.synthesis.Voice kevinHQ = new javax.speech.synthesis.Voice("kevin16",
			javax.speech.synthesis.Voice.GENDER_DONT_CARE, javax.speech.synthesis.Voice.AGE_DONT_CARE, null);
            SynthesizerModeDesc desc = 
                new SynthesizerModeDesc(null, 
                                        "general",
                                        Locale.US, 
                                        Boolean.FALSE,
                                        new javax.speech.synthesis.Voice [] { kevinHQ });

            FreeTTSEngineCentral central = new FreeTTSEngineCentral();
            EngineList list = central.createEngineList(desc); 
            
            if (list.size() > 0) { 
                EngineCreate creator = (EngineCreate) list.get(0); 
                synthesizer = (Synthesizer) creator.createEngine(); 
            } 
            if (synthesizer == null) {
                System.err.println("Cannot create synthesizer");
                System.exit(1);
            }
            synthesizer.allocate();
            synthesizer.resume();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Speaks the given time in full text.
     *
     * @param time time in full text
     */
    public void speak(String saying) {
        String os = System.getProperty("os.name");
	System.err.println("On an "+os+" box.");
        if (os.startsWith("Mac")) {
                String speakingProcess = "/usr/bin/say";
		try {
			System.err.println(speakingProcess+" "+saying);
			Runtime.getRuntime().exec(new String[] {speakingProcess, saying} );
		} catch (Exception e) {
		    e.printStackTrace();
		}
        } else {
		synthesizer.speakPlainText(saying, null);
		try {
		    synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
    }
    public void deallocate() {
	try {
          synthesizer.deallocate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * main() method to run the Synth.
     */
    public static void main(String args[]) {
        Synth synth = new Synth();
	synth.speak("Hello, World!");
	synth.deallocate();

    }
}