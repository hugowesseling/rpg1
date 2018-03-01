package com.aquarius.common2dgraphics;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.aquarius.common2dgraphics.util.Util;

public class Art {
    public static BufferedImage loadLeftTopTransparent(String name)
    {
    	BufferedImage res=load(name);
    	int width=res.getWidth();
    	int height=res.getHeight();
    	int leftTopRgb=res.getRGB(0,0);
    	int rgb;
    	for(int y=0;y<height;y++)
    		for(int x=0;x<width;x++)
    		{
    			rgb=res.getRGB(x,y);
    			if(rgb==leftTopRgb)res.setRGB(x,y,0);
    		}
    	return res;
    }
    public static BufferedImage load(String name) {
        try {
            BufferedImage org = ImageIO.read(Art.class.getResource(name));
            BufferedImage res = new BufferedImage(org.getWidth(), org.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics g = res.getGraphics();
            g.drawImage(org, 0, 0, null, null);
            g.dispose();
            return res;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void speckle(BufferedImage img)
	{	
		int x,y,r,g,b,col;
		for(int i=0;i<1000;i++)
		{
			x=Util.rnd.nextInt(16);
			y=Util.rnd.nextInt(16);
			col=img.getRGB(x,y);
			r=(col>>16)&255;
			g=(col>>8)&255;
			b=col&255;
			r+=Util.rnd.nextInt(21)-10;
			g+=Util.rnd.nextInt(21)-10;
			b+=Util.rnd.nextInt(21)-10;
			r=Util.capInt(r,0,255);
			g=Util.capInt(g,0,255);
			b=Util.capInt(b,0,255);
			img.setRGB(x, y, (255<<24) | (r<<16) | (g<<8) | b);
		}
	}
	public static BufferedImage scale(BufferedImage src, int scale) {
        int w = src.getWidth() * scale;
        int h = src.getHeight() * scale;
        BufferedImage res = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics g = res.getGraphics();
        g.drawImage(src.getScaledInstance(w, h, Image.SCALE_AREA_AVERAGING), 0, 0, null);
        g.dispose();
        return res;
    }

	public static BufferedImage[][] mirrorsplit(BufferedImage src, int xs, int ys) {
        int xSlices = src.getWidth() / xs;
        int ySlices = src.getHeight() / ys;
        BufferedImage[][] res = new BufferedImage[xSlices][ySlices];
        for (int x = 0; x < xSlices; x++) {
            for (int y = 0; y < ySlices; y++) {
                res[x][y] = new BufferedImage(xs, ys, BufferedImage.TYPE_INT_ARGB);
                Graphics g = res[x][y].getGraphics();
                g.drawImage(src, xs, 0, 0, ys, x * xs, y * ys, (x + 1) * xs, (y + 1) * ys, null);
                g.dispose();
            }
        }
        return res;
    }

    public static BufferedImage[][] split(BufferedImage src, int tileWidth, int tileHeight) {
    	return split(src, tileWidth, tileHeight, 0, 0); 
    }
	
    public static BufferedImage[][] split(BufferedImage src, int tileWidth, int tileHeight, int marginWidth, int marginHeight) {
        int xSlices = ((src.getWidth()-1) / (tileWidth + marginWidth))+1;
        int ySlices = ((src.getHeight()-1) / (tileHeight + marginHeight))+1;
        BufferedImage[][] res = new BufferedImage[xSlices][ySlices];
        for (int x = 0; x < xSlices; x++) {
            for (int y = 0; y < ySlices; y++) {
                res[x][y] = new BufferedImage(tileWidth, tileHeight, BufferedImage.TYPE_INT_ARGB);
                Graphics g = res[x][y].getGraphics();
                g.drawImage(src, -x * (tileWidth + marginWidth), -y * (tileHeight + marginHeight), null);
                g.dispose();
            }
        }
        return res;
    }
}
