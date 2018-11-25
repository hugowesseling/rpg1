package com.aquarius.rpg1;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioSystemPlayer {
	public static HashMap<String, Clip> clips = new HashMap<>();
	
	public static void playSound(String fileName, boolean looping) {
		if(clips.get(fileName) != null) {
			System.out.println("Replaying " + fileName);
			Clip clip = clips.get(fileName); 
			clip.stop();
			clip.start();
			if(looping)
				clip.loop(Clip.LOOP_CONTINUOUSLY);
		}else {
			System.out.println("Playing " + fileName);
			try {
				Clip clip = AudioSystem.getClip();
				final AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(fileName));
				clip.open(inputStream);
				if(looping)
					clip.loop(Clip.LOOP_CONTINUOUSLY);
				clips.put(fileName, clip);
			} catch (LineUnavailableException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (UnsupportedAudioFileException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	public static void stopAll() {
		for(Clip clip:clips.values()) {
			clip.stop();
			clip.setMicrosecondPosition(0);
		}
	}
	
	
}
