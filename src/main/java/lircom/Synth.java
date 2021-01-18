package lircom;

import java.io.*;

/**
 * Copyright 2003 Sun Microsystems, Inc.
 *
 * See the file "license.terms" for information on usage and
 * redistribution of this file, and for a DISCLAIMER OF ALL
 * WARRANTIES.
 */


/**
 * A talking clock powered by FreeTTS.
 */
public class Synth {
	java.util.Hashtable<String, String> nickToVoice = new java.util.Hashtable<String, String>();
	public final String [] voices = {
		"Alex",
		"Bruce",
		"Fred",
		"Junior",
		"Ralph",
		"Agnes",
		"Kathy",
		"Princess",
		"Vicki",
		"Victoria"
	};



    /**
     * Creates the synthesizer, called by the constructor.
     */
    public Synth() {
	    
    }


    /**
     * Speaks the given time in full text.
     *
     * @param time time in full text
     */
    java.util.Random r = new java.util.Random();
    public void speak(String saying) {
        String os = System.getProperty("os.name");
	String nick = "System";
	if (saying.indexOf(" ") >= 0) {
		nick = saying.substring(0, saying.indexOf(" "));
	}
 	String voice = nickToVoice.get(nick);
	if (voice == null) {
		voice = voices[r.nextInt(voices.length)];
		nickToVoice.put(nick, voice);
	}
	try {
		 if (os.startsWith("Win")) {
			System.err.println("PowerShell "+saying);
			String[] commandList = {"PowerShell.exe", "-Command", "Add-Type -AssemblyName System.Speech; $ss = New-Object -TypeName System.Speech.Synthesis.SpeechSynthesizer; $ss.Speak('"+(saying.replace("'", ""))+"');"};
			Process powerShellProcess = Runtime.getRuntime().exec(commandList);
			powerShellProcess.getOutputStream().close();
			String line;
			System.out.println("Output:");
			BufferedReader stdout = new BufferedReader(new InputStreamReader(
			powerShellProcess.getInputStream()));
			while ((line = stdout.readLine()) != null) {
				System.out.println(line);
			}
			stdout.close();
			System.out.println("Error:");
			BufferedReader stderr = new BufferedReader(new InputStreamReader(
			powerShellProcess.getErrorStream()));
			while ((line = stderr.readLine()) != null) {
				System.out.println(line);
			}
			stderr.close();
			System.out.println("Done");
		} else if (os.startsWith("Lin")) {
			String speakingProcess = "/usr/bin/espeak";
			voice = "!v/"+nick;
			System.err.println(speakingProcess+" -v "+voice+" '"+saying+"'");
			Runtime.getRuntime().exec(new String[] {speakingProcess, "-v", voice, saying} );
	 
	 
		} else {
			String speakingProcess = "/usr/bin/say";
			System.err.println(speakingProcess+" -v "+voice+" "+saying);
			Runtime.getRuntime().exec(new String[] {speakingProcess, "-v", voice, saying} );
		}
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    public void deallocate() {
    }

    /**
     * main() method to run the Synth.
     */
    public static void main(String args[]) {
	try {
		String[] commandList = {"PowerShell.exe", "-Command", "Add-Type -AssemblyName System.Speech; $ss = New-Object -TypeName System.Speech.Synthesis.SpeechSynthesizer; $ss.Speak('this is really simple, just type in what you want the computer to say');"};
		Process powerShellProcess = Runtime.getRuntime().exec(commandList);
		powerShellProcess.getOutputStream().close();
		String line;
		System.out.println("Output:");
		BufferedReader stdout = new BufferedReader(new InputStreamReader(
		powerShellProcess.getInputStream()));
		while ((line = stdout.readLine()) != null) {
			System.out.println(line);
		}
		stdout.close();
		System.out.println("Error:");
		BufferedReader stderr = new BufferedReader(new InputStreamReader(
		powerShellProcess.getErrorStream()));
		while ((line = stderr.readLine()) != null) {
			System.out.println(line);
		}
		stderr.close();
		System.out.println("Done");
 
 
	} catch (Exception e) {
		e.printStackTrace();
	}
/*
        Synth synth = new Synth();
	synth.speak("'Hello, World!'");

*/
    }
}
