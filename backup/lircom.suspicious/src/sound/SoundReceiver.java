package lircom;

import java.net.*;
import java.io.*;
import javax.sound.sampled.*;

public class SoundReceiver extends Thread {
	InputStream is;
	public SoundReceiver(InputStream is) {
		this.is = is;
	}
	public void run() {
		try {
			int c;
			BufferedInputStream bis = new BufferedInputStream(is);
			AudioInputStream ais = AudioSystem.getAudioInputStream(bis);
			AudioFormat af = ais.getFormat();
			DataLine.Info sourceInfo = new DataLine.Info(SourceDataLine.class, af);
			SourceDataLine source = (SourceDataLine) AudioSystem.getLine(sourceInfo);
			source.open(af);
			source.start();
			int bytes = 0;
			byte buffer [] = new byte[8000];
			while (true) {
				bytes = ais.read(buffer, 0, buffer.length);
				if (bytes > 0) {
					source.write(buffer, 0, bytes);
				}
			}
			/*
			source.drain();
			source.close();
			*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.err.println("Exiting receiver");
	}
}
