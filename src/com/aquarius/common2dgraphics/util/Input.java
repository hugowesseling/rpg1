package com.aquarius.common2dgraphics.util;

import java.awt.event.KeyEvent;

public class Input {
	public static Input instance;

	public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;

    public static final int JUMP= 4;

    public static final int ESCAPE = 5;
    public static final int CONTROL = 6;
	public static final int SHIFT = 7;
	public static final int ALT = 8;
	public static final int ZOOM_IN = 9;
	public static final int ZOOM_OUT = 10;
	public static final int ACTIVATE = 11;

    public static final int K_LEFT = 12;
    public static final int K_RIGHT = 13;

    public boolean[] buttons = new boolean[64];
    public boolean[] oldButtons = new boolean[64];

    public void set(int key, boolean down)
    {
        int button = -1;

        switch(key)
        {
        	case KeyEvent.VK_UP: button=UP; break;
        	case KeyEvent.VK_LEFT: button=LEFT; break;
        	case KeyEvent.VK_DOWN: button=DOWN; break;
        	case KeyEvent.VK_RIGHT: button=RIGHT; break;

        	case KeyEvent.VK_NUMPAD8: button=UP; break;
        	case KeyEvent.VK_NUMPAD4: button=LEFT; break;
        	case KeyEvent.VK_NUMPAD2: button=DOWN; break;
        	case KeyEvent.VK_NUMPAD6: button=RIGHT; break;

        	case KeyEvent.VK_A: button=K_LEFT; break;
        	//case KeyEvent.VK_S: button=DOWN; break;
        	case KeyEvent.VK_D: button=K_RIGHT; break;

        	case KeyEvent.VK_CONTROL: button=CONTROL; break;
        	case KeyEvent.VK_SHIFT: button=SHIFT; break;
        	case KeyEvent.VK_ALT: button=ALT; break;        	
        	
        	case KeyEvent.VK_SPACE: button=JUMP; break;
        	case KeyEvent.VK_ESCAPE: button=ESCAPE; break;

        	case KeyEvent.VK_EQUALS: button=ZOOM_IN; break;
        	case KeyEvent.VK_MINUS: button=ZOOM_OUT; break;

        	case KeyEvent.VK_E: button=ACTIVATE; break;
        }
        if (button != -1) buttons[button] = down;
    }

    public boolean isPressed(int key)
    {
    	if(buttons[key] && !oldButtons[key])return true;
    	return false;
    }
    public void tick() {
        for (int i = 0; i < buttons.length; i++)
        {
            oldButtons[i] = buttons[i];
        }
    }

    public void releaseAllKeys() {
        for (int i = 0; i < buttons.length; i++)
        {
            buttons[i] = false;
            oldButtons[i] = false;
        }
    }
    public Input()
    {
    	releaseAllKeys();
    	instance = this;
    }
}
