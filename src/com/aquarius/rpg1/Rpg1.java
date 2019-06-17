package com.aquarius.rpg1;

import java.util.ArrayList;

import com.aquarius.common2dgraphics.util.Input;

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
	private EditConfiguration editConfiguration;
	public Rpg1()
	{
		allViews = new ArrayList<>();
		editConfiguration = new EditConfiguration();
		tileSelectorFrame = new TileSelectorFrame("TileSet", editConfiguration);

		System.out.println("Character sub classes: " + String.join(", ", Resources.characterSubClasses.keySet()));
		
		//levelState.allCharacters.add(new HenryCharacter(CharacterPosition.createFromTilePosition(new Int2d(10, 15)), new CharacterTileSet(new Int2d(3,0)), Direction.SOUTH));
		//levelState.allCharacters.add(new HenryCharacter(CharacterPosition.createFromTilePosition(new Int2d(15, 10)), new CharacterTileSet(new Int2d(3,0)), Direction.SOUTH));

		levelState = new LevelState();
		levelState.deleteFilesInUserFolder();
		levelState.resetToBeginOfGame();
		playerView = new PlayerView(levelState, editConfiguration);
		allViews.add(playerView);
		
		exampleLevelState = new LevelState();
		exampleLevelState.loadLevel("house1_500_500.rpg1");
		exampleLevelView = new LevelView(exampleLevelState, editConfiguration);
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
				Thread.sleep(30);
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
