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
	public static final String AUDIO_FOLDER = "D:\\download\\humble_bundle\\gamedev\\sfx\\prosoundcollection_audio\\prosoundcollection\\Gamemaster Audio - Pro Sound Collection v1.3 - 16bit 48k\\";
	public static HashMap<String, Clip> clips = new HashMap<>();
	public static int lastPlayedRandomIndex = 0;
	public enum RandomSound{
		WHOOSH("Whooshes\\whoosh_low_deep_soft_",3), 
		ROCK_IMPACT("Impacts_Smashable\\rock_impact_small_hit_",3),
		ROCK_BREAK("Impacts_Smashable\\rock_smashable_hit_impact_large_", 3),
		ROCK_HEAVY_SLAM("Impacts_Smashable\\rock_impact_heavy_slam_", 4),
		EXPRESSION("Voice\\Human Female A\\voice_female_a_expression_emote_",9),
		CHICKEN("Animal_Impersonations\\chicken_2_bwak_",10),
		SWISH("Whooshes\\whoosh_weapon_knife_swing_", 4),
		FLESH_IMPACT("Guns_Weapons\\Bullets\\bullet_impact_body_flesh_", 7),
		DOOR_LOCK_FAIL("Doors\\door_lock_fail_", 5), 
		MALE_GRUNT_PAIN("Voice\\Human Male A\\voice_male_grunt_pain_", 13),
		CREATURE_EMOTE("Voice\\Fun Creatures\\voice_fun_creature_small_mutant_voice_emotes_",10),
		MALE_DEATH("Voice\\Human Male A\\voice_male_grunt_pain_death_",10);
		
		public String path;
		public int count;
		RandomSound(String path, int count){
			this.path = path;
			this.count = count;
		}
	}
	
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
	
	public static void playRandom(RandomSound randomSound)
	{
		String audioFileName = String.format(AUDIO_FOLDER + randomSound.path + "%02d.wav",
				newRandomIndex(randomSound.count));
		playSound(audioFileName, false);		
		
	}
	/*
	public static void playRandomExpression() {
		String audioFileName = String.format(AUDIO_FOLDER + "Voice\\Human Female A\\voice_female_a_expression_emote_%02d.wav",
				newRandomIndex(9));
		playSound(audioFileName, false);		
	}
	public static void playRandomChicken() {
		String audioFileName = String.format(AUDIO_FOLDER + "Animal_Impersonations\\chicken_2_bwak_%02d.wav",
				newRandomIndex(10));
		playSound(audioFileName, false);		
		
	}
	public static void playRandomSwish() {
		String audioFileName = String.format(AUDIO_FOLDER +"Whooshes\\whoosh_weapon_knife_swing_%02d.wav",
				newRandomIndex(4));
		playSound(audioFileName, false);		
				
	}
	public static void playRandomWhoosh() {
		String audioFileName = String.format(AUDIO_FOLDER +"Whooshes\\whoosh_low_deep_soft_%02d.wav",
				newRandomIndex(3));
		playSound(audioFileName, false);		
				
	}
	
	public static void playRandomImpact() {
		String audioFileName = String.format(AUDIO_FOLDER +"Guns_Weapons\\Bullets\\bullet_impact_body_flesh_%02d.wav",
				newRandomIndex(7));
		playSound(audioFileName, false);		
	}*/
}
