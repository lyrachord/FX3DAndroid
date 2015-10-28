package org.fxyz.utils;

import java.util.Arrays;
import java.util.List;

import javafx.scene.paint.Color;

public enum DefaultColorPalette implements ColorPalette {
	HSB {
		@Override
		protected Color getColor(double fact) {
			// There are 6*255=1530 distinct pure colors, 255 colors every 60º, with 100% saturation and value
			return Color.hsb(360d * fact, 1d, 1d, 0.21);

		}
	},
	GREEN {
		@Override
		public Color getColor(int index, int amountColors) {
			return GREEN_COLORS.get((index * GREEN_COLORS.size() / amountColors));
		}
	},
	RGB {
		@Override
		public Color getColor(int index, int amountColors) {
			return Color.rgb((index >> 16) & 0xFF, (index >> 8) & 0xFF, index & 0xFF);
		}
	},
	BLUEWHITE {
		@Override
		protected Color getColor(double fact) {
			return Color.hsb(210, fact * 0.7 + 0.3, 0.5);
		}
	};

	static private final List<Color> GREEN_COLORS = Arrays.asList(Color.rgb(0, 0, 0, 1), Color.rgb(30, 49, 29, 1),
			Color.rgb(35, 80, 33, 1), Color.rgb(56, 122, 54, 1), Color.rgb(45, 187, 40, 1), Color.rgb(8, 231, 0, 1));

	public Color getColor(int index, int amountColors) {
		return getColor(index / (double) amountColors);
	}

	protected Color getColor(double fact) {
		return Color.WHITE;
	}
}
