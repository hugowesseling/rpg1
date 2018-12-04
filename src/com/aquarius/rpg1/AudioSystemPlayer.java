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
	public static int lastPlayedRandomIndex = 0;
	
	public static void playSound(String fileName, boolean looping) {
		if(clips.get(fileName) != null) {
			System.out.println("Replaying " + fileName);
			Clip clip = clips.get(fileName); 
			clip.stop();
			clip.setMicrosecondPosition(0);
			if(looping)
				clip.loop(Clip.LOOP_CONTINUOUSLY);
			else
				clip.start();
		}else {
			System.out.println("Playing " + fileName);
			try {
				Clip clip = AudioSystem.getClip();
				final AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(fileName));
				clip.open(inputStream);
				if(looping)
					clip.loop(Clip.LOOP_CONTINUOUSLY);
				else
					clip.start();
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
	
	private static int newRandomIndex(int max) {
		int randomIndex;
		do{
			randomIndex = (int )(Math.random() * max) + 1;
		}while(lastPlayedRandomIndex == randomIndex);
		lastPlayedRandomIndex = randomIndex;
		return randomIndex;
	}
	
	public static void playRandomExpression() {
		String audioFileName = String.format("D:\\download\\humble_bundle\\gamedev\\sfx\\prosoundcollection_audio\\prosoundcollection\\Gamemaster Audio - Pro Sound Collection v1.3 - 16bit 48k\\Voice\\Human Female A\\voice_female_a_expression_emote_%02d.wav",
				newRandomIndex(9));
		playSound(audioFileName, false);		
	}
	public static void playRandomChicken() {
		String audioFileName = String.format("D:\\download\\humble_bundle\\gamedev\\sfx\\prosoundcollection_audio\\prosoundcollection\\Gamemaster Audio - Pro Sound Collection v1.3 - 16bit 48k\\Animal_Impersonations\\chicken_2_bwak_%02d.wav",
				newRandomIndex(10));
		playSound(audioFileName, false);		
		
	}
	
	
}
