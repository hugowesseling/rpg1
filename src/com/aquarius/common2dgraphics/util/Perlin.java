package com.aquarius.common2dgraphics.util;

import java.util.Random;

import com.aquarius.common2dgraphics.util.Util;

public class Perlin
{
	final static int Number_Of_Octaves=4;
	final static float Persistence=0.4f;
	public final static int FIXED_RND=Util.rnd.nextInt();

	//1d noise

	static float Interpolate(float a,float b,float x)  //cosine interpolation
	{
		float ft = x * 3.1415927f;
		float f = (float) ((1 - Math.cos(ft)) * 0.5f);
		return  a*(1-f) + b*f;
	}

	/*static float Interpolate(float a, float b, float x)
	{
		return  a*(1-x) + b*x;
	}*/
	static float Linear_Interpolate(float a, float b, float x)
	{
		return  a*(1-x) + b*x;
	}

	static float Cosine_Interpolate(float a,float b,float x)
	{
		float ft = x * 3.1415927f;
		float f =  ((1 - (float)Math.cos(ft)) * 0.5f);
		return  a*(1-f) + b*f;
	}

	static float Cubic_Interpolate(float v0, float v1, float v2, float v3,float x)
	{
		float P = (v3 - v2) - (v0 - v1);
		float Q = (v0 - v1) - P;
		float R = v2 - v0;
		float S = v1;
		return P*x*x*x + Q*x*x + R*x + S;
	}
	// e>=0 !!
	static float powfi(float m,int e)
	{
		float r=1;
		while(e--!=0)r*=m;
		return r;
	}

	//2d noise

	// returns in range -1..1
	static float Noise2D(int x, int y)
	{
		int n = x + y * 57+FIXED_RND;
		n = (n<<13) ^ n;
		return ( 1.0f - ( (n * (n * n * 15731 + 789221) + 1376312589) & 0x7fffffff) / 1073741824.0f);    
	}

	// returns in range -1..1
	static float SmoothNoise2D(int x, int y)
	{
		float corners = ( Noise2D(x-1, y-1)+Noise2D(x+1, y-1)+Noise2D(x-1, y+1)+Noise2D(x+1, y+1) ) *0.0625f;
		float sides   = ( Noise2D(x-1, y)  +Noise2D(x+1, y)  +Noise2D(x, y-1)  +Noise2D(x, y+1) ) *0.125f;
		float center  =  Noise2D(x, y)*0.25f;
		return corners + sides + center;
	}

	// returns in range -1..1
	static float InterpolatedNoise2D(float x, float y)
	{
		int integer_X    = (int)x;
		float fractional_X = x - integer_X;
		
		int integer_Y    = (int)y;
		float fractional_Y = y - integer_Y;
		
		float v1 = SmoothNoise2D(integer_X,     integer_Y);
		float v2 = SmoothNoise2D(integer_X + 1, integer_Y);
		float v3 = SmoothNoise2D(integer_X,     integer_Y + 1);
		float v4 = SmoothNoise2D(integer_X + 1, integer_Y + 1);
		
		float i1 = Interpolate(v1 , v2 , fractional_X);
		float i2 = Interpolate(v3 , v4 , fractional_X);
		
		return Interpolate(i1 , i2 , fractional_Y);
	}

	// returns in range -1..1
	public static float PerlinNoise2D(float x, float y)
	{
		float total = 0;
		for(int i=0;i<Number_Of_Octaves;i++)
		{
			total = total + InterpolatedNoise2D(x * (1<<i), y * (1<<i)) * powfi(Persistence,i);
		}
		return total;
	}

}
