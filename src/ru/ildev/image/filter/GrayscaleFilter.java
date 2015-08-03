/*
 *
 */
package ru.ildev.image.filter;

/**
 * Фильтр, изменяющий цвета изображения на градацию серого, серый цвет пикселя определяется его яркостью по NTSC схеме.
 *
 * @author Ilyas74
 * @version 0.0.0
 */
public class GrayscaleFilter extends ColorFilter {

    /**
     * Стандартный конструктор.
     */
    public GrayscaleFilter() {
    }

    @Override
    protected int apply(int x, int y, int color) {
        if (this.isMonochrome()) {
            return color;
        } else {
            int a = 0xff000000 & color;
            int r = 0xff & (color >> 16);
            int g = 0xff & (color >> 8);
            int b = 0xff & color;

            // Simple
            //int gray = (r + g + b) / 3;

            // NTSC
            int gray = (r * 77 + g * 151 + b * 28) >> 8;
            //int gray = Color.brightnessNTSC(r, g, b);

            if (this.isRedChannel()) r = gray;
            if (this.isGreenChannel()) g = gray;
            if (this.isBlueChannel()) b = gray;

            return a | (r << 16) | (g << 8) | b;
        }
    }
}
