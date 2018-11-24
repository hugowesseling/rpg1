package com.aquarius.common2dgraphics.util;

import java.awt.Color;
import java.util.Random;

public class Util
{
	public static Random rnd=new Random();
	public static void println(String string)
	{
		System.out.println(string);
	}
	public static void print(String string)
	{
		System.out.print(string);
	}
	public static void errorln(String string) 
	{
		System.err.println(string);
	}
	public static Color randomColor()
	{
		return new Color(rnd.nextInt(256),rnd.nextInt(256),rnd.nextInt(256));
	}
	public static int capInt(int x, int min, int max)
	{
		if(x<min)x=min;
		if(x>max)x=max;
		return x;
	}

}
