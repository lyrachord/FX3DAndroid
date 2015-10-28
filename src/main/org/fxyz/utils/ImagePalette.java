package org.fxyz.utils;

import com.jx.ui.fx.FXUtil;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

public class ImagePalette implements ColorPalette {
	private final Image image;

	public ImagePalette(String image) {
		this(FXUtil.loadImage(image));
	}

	public ImagePalette(Image image) {
		this.image = image;
	}

	@Override
	public Image createImage(int numberColors, int width, int height) {
		return image;
	}

	/**
	 *
	 * @param imageFileName
	 * @param opacity
	 *            range 0-1,
	 * @return
	 */
	static public ImagePalette create(String imageFileName, double opacity) {
		return create(imageFileName, (int) (255 * opacity));
	}

	/**
	 *
	 * @param imageFileName
	 * @param opacity
	 *            range 0-255, NOTE: the opacity write 1 easy to lead to 1.0=>1 ERROR
	 * @return
	 */
	static public ImagePalette create(String imageFileName, int opacity) {
		// Image image=FXUtil.loadImage("/images/water3.jpg");
		return create(FXUtil.loadImage(imageFileName), opacity);
	}

	static public ImagePalette create(Image image, double opacity) {
		return create(image, (int) (255 * opacity));
	}

	static public ImagePalette create(Image image, int opacity) {
		return new ImagePalette(changeOpacity(image, opacity));
	}

	/**
	 * @param image
	 * @param opacity
	 *            value range from 0 to 255
	 * @return
	 */
	static public WritableImage changeOpacity(Image image, int opacity) {
		int height = (int) image.getHeight();
		int width = (int) image.getWidth();
		WritableImage im2 = new WritableImage(width, height);
		PixelReader reader = image.getPixelReader();
		opacity = ((opacity & 0xFF) << 24);
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++) {
				int argb = reader.getArgb(x, y);
				argb = opacity | (argb & 0xFFFFFF);
				im2.getPixelWriter().setArgb(x, y, argb);
			}
		return im2;
	}
}
