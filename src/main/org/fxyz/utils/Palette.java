/*
 * Copyright (C) 2013-2015 F(X)yz,
 * Sean Phillips, Jason Pollastrini and Jose Pereda
 * All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fxyz.utils;

import java8.util.stream.*;
import javafx.scene.image.Image;

/**
 *
 * @author jpereda
 */
public class Palette {

    private final ColorPalette colorPalette;
    private final int numColors;
    private int width;
    private int height;
    private Image imgPalette;

    private final static ColorPalette DEFAULT_COLOR_PALETTE = DefaultColorPalette.HSB;
    private final static int DEFAULT_NUMCOLORS = 10000; // 100x100 palette image

    public Palette(){
        this(DEFAULT_NUMCOLORS, DEFAULT_COLOR_PALETTE);
    }

    public Palette(int numColors){
        this(numColors, DEFAULT_COLOR_PALETTE);
    }

    public Palette(int numColors, ColorPalette colorPalette){
        this.numColors=numColors;
        this.colorPalette = colorPalette;
    }

    public Image createPalette(boolean save){
        if(numColors<1){
            return null;
        }
        // try to create a square image
        width=(int)Math.sqrt(numColors);
        height=numColors/width;

        imgPalette = colorPalette.createImage(numColors, width, height);
        if(save){
            saveImage();
        }
        return imgPalette;
    }

    public DoubleStream getTextureLocation(int iPoint){
        if(width==0 || height==0){
            return DoubleStreams.of(0f,0f);
        }
        int y = iPoint/width;
        int x = iPoint-width*y;
        // add 0.5 to interpolate colors from the middle of the pixel
        return DoubleStreams.of((((float)x+0.5f)/((float)width)),(((float)y+0.5f)/((float)height)));
    }
    public float[] getTextureLocationPoint(int iPoint){
        if(width==0 || height==0){
        	return new float[]{0f, 0f};
        }
        int y = iPoint/width;
        int x = iPoint-width*y;
        // add 0.5 to interpolate colors from the middle of the pixel
//        return DoubleStream.of((((float)x+0.5f)/((float)width)),(((float)y+0.5f)/((float)height)));
        return new float[]{(((float)x+0.5f)/((float)width)),(((float)y+0.5f)/((float)height))};
    }


    public Image getPaletteImage() {
        return imgPalette;
    }

    private void saveImage(){
//        try {
//            ImageIO.write(SwingFXUtils.fromFXImage(imgPalette, null), "png", new File("palette_"+numColors+".png"));
//        } catch (IOException ex) {
//            System.out.println("Error saving image");
//        }
    }
}
