package com.aquarius.rpg1;

import java.awt.image.BufferedImage;

import com.aquarius.common2dgraphics.Art;

public class ImageSet {
	public BufferedImage images[];
	public ImageSet(String filePattern, int startIndex, int endIndex) {
		images = new BufferedImage[endIndex - startIndex + 1];
		String fileName;
		for(int i = startIndex; i <= endIndex ; i++) {
			fileName = String.format(filePattern, i);
			System.out.println("Loading " + fileName);
			images[i - startIndex] = Art.load(fileName);
		}
	}
}
