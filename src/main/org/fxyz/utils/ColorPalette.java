package org.fxyz.utils;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public interface ColorPalette {
	/**
	 * @param numberColors
	 *            it's just a hint, size of image to create can bigger then it
	 * @param width
	 * @param height
	 * @return
	 */
	default Image createImage(int numberColors, int width, int height) {
		WritableImage image = new WritableImage(width, height);
		PixelWriter pw = image.getPixelWriter();
		createImage(numberColors, pw, width, height);
		return image;
	}

	default void createImage(int numberColors, PixelWriter pw, int width, int height) {
		int i=0;
		for(int y=0; y<height; y++){
			for(int x=0; x<width; x++){
				pw.setColor(x, y, getColor(i++, numberColors));
			}
		}
//		AtomicInteger count = new AtomicInteger();
//		IntStream.range(0, height).boxed().forEach(y -> IntStream.range(0, width).boxed()
//				.forEach(x -> pw.setColor(x, y, getColor(count.getAndIncrement(), numberColors))));
	}

	default Color getColor(int index, int numberColors) {
		return Color.WHITE;
	}
}
