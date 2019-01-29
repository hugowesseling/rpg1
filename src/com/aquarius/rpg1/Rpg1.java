package com.aquarius.rpg1;

/*
Need a simple story:
- Go find a person in another town and talk to some people that then tell different things.
	- Retrieve object after searching

Minimal additions:
- Collision with objects and other characters
	- Check if colliding
		- for each object, check world for colliding objects
			- define what are colliding objects:
				xMmark with tile editor special mode
					- houses except roofs
					- bottom of trees
				- Save all TileSet.tileCollision's into config
					Add to Rpg.saveConfigToFile()
					- First split up the tilePatterns to different tilesets
				
	- push both objects back until not colliding, position on pixel basis
- Random walking around midpoint behavior
- world state dependent dialogs
- Objects in the world:
	- Needs new drawing routine: Make the Actions responsible for deciding which image to draw and which collision model to use
- Stored objects:
	- Are only object types. Stored in a hashmap of {String:Object Behavior Class instance} for behavior definition.
	- Multiple object types can share behavior, maybe differ on parameters (appearance, strength, duration,...)
	- Objects stored can also appear in the world, then the objecttype is part of a StorableObject, which uses the objecttype for drawing
- Wandering person in village lost a special ring (with engraving of dead mother), only knows location where lost
	- At that location, find a person running around, catch him, but he will only give ring back if you help him
		-> He needs something to give to his girlfriend
		-> Find a nice flower and tell him that if she does not accept this, then it was not meant to be
	- Find girlfriend, she keeps hiding behind trees, does not want to meet him. Ask him to stay behind and tell her that he's sorry, he can then approach and make things up to her
	- you get the ring and bring it back to person in village,that gives you soup in his house (his mother's recipe)

	*Needed additions:
	TODO: following behavior
	DONE straightline, hiding
	DONE: Entering house
	DONE: Object storage, behavior and pickup
	DONE: Dialogue based on stored object
	TODO: Worldstate containing everything including player?

Then:
- World changing events:
	- Doors opening
		-> Needs storage of layer animations:
			-> Go in animation edit mode, control frame.
				-> Change some tiles
				-> Go to next frame, change some tiles
				-> Store entire sequence as animation
				-> Only store forward, reverse editing (to t-1) by reloading full map for only affected tiles and playing animation until point t-1
				-> Storage: anim:[{top:[x,y, tileIndex],bottom:[x,y, tileIndex]}]  (anim[0] has first frame 
	- Rocks moving
	- Characters/objects appearing
 

Possible game objects:
- Level push-on-stack and teleport (to go into house for instance)
	Should show possible levels to choose from, or create a new one (text field with name)
	Levels to jump to are in context of the current map coordinates
		-> What about jumping towards another point (actual warp?)
			- Maybe use separate teleport object
- treasure chest
- box of treasure
- Switch that opens door
- Block that can be pushed
- Switch with two states (red/green) 

Behavior:
The object should also do something with its contents

Should it be possible to have different images for the same type object? Almost no game does this!
-> hardcoding images used is no problem!

Each object type has a function:
Object[] getObjectSettings():
	{"Specify object settings", comboBox, comboBox}

When starting to add an object: Show the object type selection, then specify parameters, then create with those parameters and keep moving the object around with mouse
A left-click them simply does not control the object anymore, keeping it in its last place.

*Create level generator:
features:
- Tilepattern for any two materials
	-> Only thing needed is to assign two materials to a tilepattern, one for red, one for green
		-> Assign using other tiles
- Needs tile patterns over multiple tilesets...
	-> Major refactor..

*First create small forest scene for crystal quest


SFX:
Music:
	Town peaceful: https://musopen.org/music/85-berceuse-op-57/
	Town bit playful: https://musopen.org/music/2491-2-arabesques-l-66/ (first 2 mins or so)
	Sacred forest hideout: https://musopen.org/music/4478-danse-sacree-et-danse-profane/
	
	Other music:  D:\download\humble_bundle\gamedev\sfx\prosoundcollection_audio\prosoundcollection\Gamemaster Audio - Pro Sound Collection v1.3 - 16bit 48k\zzzBonus_Music_16bit44kOnlyzzz
		town: music_harp_peaceful_loop.wav
	forest: "D:\download\humble_bundle\gamedev\sfx\prosoundcollection_audio\prosoundcollection\Gamemaster Audio - Pro Sound Collection v1.3 - 16bit 48k\Animals_Nature_Ambiences\swamp_ambience_frogs_03_loop.wav"
	
Creation of mountains:
Two operations to create mountains:
- extrude upwards
- flatten sideways

Needs a graph with possible connections on each side..
Maybe make one example including all connections:
	Program can learn connections like wang-tiles

New tiling method:
- analyze example:
	outcome: index -> top/bottom/left/right -> list of possible indices
	categorize with colors
		display transparent overlay
- painting:
	select color and paint size
	1x1: check all indices in color category and choose random from best fitting (-1 for each mismatching)
	3x3: for all possible indices in category, try out all possible surroundings
		only check connections to outside of 3x3

 */

// DONE: health & dying
// TODO: particles: bouncing & flying
// DONE: Add house interiors: Walking into another level and out into previous level again
// DONE: Multi tileset tile patterns
// DONE: Dual material tile patterns
// TODO: Level generator using fancy tile patterns
// DONE: Level stack
// TODO: Optimize graphics2 (See https://stackoverflow.com/questions/658059/graphics-drawimage-in-java-is-extremely-slow-on-some-computers-yet-much-faster)
// TODO: Add shield
// TODO: Add clipboard window (to copy paste level parts on and off) 


// DONE: Add collision with objects
// DONE: Optimize graphics1: only draw when needed
// DONE: Add sword fighting
// DONE: Add character editing dialog upon adding a character
// DONE: stitching levels together
// DONE: Automatic path routing
//		 Create boolean map from where there is the path type and where there isn't, create a 8 boolean (8 directions) to one index (0-28) mapping to choose the correct tile
// DONE: View screen resizing
//		 Would have to change to JFrame instead of Applet
// DONE: larger selection in tileselector
// DONE: Undo
// DONE: 2 layers
// DONE: Copy / paste of selection rectangle
// DONE: Scroll when being close to the border

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;
import java.util.regex.Pattern;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.aquarius.common2dgraphics.util.Input;
import com.aquarius.rpg1.AudioSystemPlayer.RandomSound;
import com.aquarius.rpg1.Resources.CharacterCreatorFunction;
import com.aquarius.rpg1.Resources.ObjectCreatorFunction;
import com.aquarius.rpg1.behavior.hateno.HenryCharacter;
import com.aquarius.rpg1.behavior.hateno.HoppingCharacter;
import com.aquarius.rpg1.behavior.hateno.ProximityRunCharacter;
import com.aquarius.rpg1.behavior.hateno.RunningCharacter;
import com.aquarius.rpg1.behavior.hateno.SoupCharacter;
import com.aquarius.rpg1.drawers.CharacterDrawer;
import com.aquarius.rpg1.drawers.TileDrawer;
import com.aquarius.rpg1.objects.GameObject;
import com.aquarius.rpg1.objects.StorableObjectType;
import com.aquarius.rpg1.weapons.BeamWeapon;
import com.aquarius.rpg1.weapons.Hammer;

public class Rpg1 implements Runnable {
	/*
	 * Should not contain anything but initialization and structuring code
	 */
	private boolean running = false;

	private TileSelectorFrame tileSelectorFrame;
	private PlayerView playerView;
	private LevelState levelState;
	private ArrayList<LevelView> allViews;

	private LevelState exampleLevelState;
	private LevelView exampleLevelView;
	public Rpg1()
	{
		allViews = new ArrayList<>();
		tileSelectorFrame = new TileSelectorFrame("TileSet");

		System.out.println("Character sub classes: " + String.join(", ", Resources.characterSubClasses.keySet()));
		
		//levelState.allCharacters.add(new HenryCharacter(CharacterPosition.createFromTilePosition(new Int2d(10, 15)), new CharacterTileSet(new Int2d(3,0)), Direction.SOUTH));
		//levelState.allCharacters.add(new HenryCharacter(CharacterPosition.createFromTilePosition(new Int2d(15, 10)), new CharacterTileSet(new Int2d(3,0)), Direction.SOUTH));

		levelState = new LevelState();
		levelState.deleteFilesInUserFolder();
		levelState.resetToBeginOfGame();
		playerView = new PlayerView(levelState, tileSelectorFrame);
		allViews.add(playerView);
		
		exampleLevelState = new LevelState();
		exampleLevelState.loadLevel("house1_500_500.rpg1");
		exampleLevelView = new LevelView(exampleLevelState, tileSelectorFrame);
		allViews.add(exampleLevelView);
	}
	public void start()
	{
		running=true;
		new Thread(this).start();
	}
	public void stop()
	{
		running=false;
	}

	@Override
	public void run()
	{
		while(running)
		{
			for(LevelView levelView:allViews) {
				levelView.repaint();
			}
			//playerView.repaint();
			tileSelectorFrame.updateFrame();
			try
			{
				Thread.sleep(10);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args)
	{
		Rpg1 rpg1=new Rpg1();
		rpg1.start();
		new Input();
	}
}
