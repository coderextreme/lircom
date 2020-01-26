package lircom;

import javax.sound.sampled.*;
import java.io.*;

public 	class SoundSender extends Thread {
	OutputStream os;
	public SoundSender(OutputStream os) {
		this.os = os;
	}
	public void run() {
		try {
			AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
			int channels = 1;
			boolean byteOrder = false;
			float rate = 8000.0f;
			int bitsPerSample = 16;
			int frameSize = (bitsPerSample / 8) * channels;
			AudioFormat af = new AudioFormat(encoding, rate, bitsPerSample, channels, frameSize, rate, byteOrder);
			DataLine.Info lineInfo = new DataLine.Info(TargetDataLine.class, af);
			DataLine.Info portInfo = new DataLine.Info(Port.class, af);
			System.err.println(portInfo.toString());

			TargetDataLine line = (TargetDataLine)AudioSystem.getLine(lineInfo);
			line.open(af);
			AudioInputStream ais = new AudioInputStream(line);

			AudioFormat.Encoding targetEnc = new AudioFormat.Encoding("GSM0610");
			// AudioFileFormat.Type type = AudioFileFormat.Type.WAVE;
			AudioInputStream gsmis = AudioSystem.getAudioInputStream(targetEnc, ais);
			AudioFileFormat.Type ftype = new AudioFileFormat.Type("GSM", ".gsm");
			line.start();
			int wrFrames = AudioSystem.write(ais, ftype, os);
			 
			/*
			byte [] buffer = new byte[8000];
			AudioFormat af2 = line.getFormat();
			frameSize = af2.getFrameSize();
			int bufFrames = buffer.length / frameSize;
			while (true) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int frames = line.read(buffer, 0, bufFrames);
				int writeBytes = frames * frameSize;
				baos.write(buffer, 0, writeBytes);
				baos.close();
				byte data [] = baos.toByteArray();
				ByteArrayInputStream bais = new ByteArrayInputStream(data);

				AudioInputStream ais2 = new AudioInputStream(bais, af2, data.length / af2.getFrameSize());
				AudioSystem.write(ais2, type, os);
			}
			*/
			line.stop();
			line.drain();
			line.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.err.println("Exiting sender");
	}
}
