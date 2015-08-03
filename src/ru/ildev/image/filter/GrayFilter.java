/*
 *
 */
package ru.ildev.image.filter;

import ru.ildev.math.MoreMath;

/**
 * Фильтр, изменяющий цвета изображения на градацию серого, серый цвет пикселя определяется его яркостью.
 *
 * @author Ilyas74
 * @version 0.1.0
 */
public class GrayFilter extends ColorFilter {

    /**
     * Стандартный конструктор.
     */
    public GrayFilter() {
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
            int gray = MoreMath.max(MoreMath.max(r, g), b);//Color.brightness(r, g, b);

            if (this.isRedChannel()) r = gray;
            if (this.isGreenChannel()) g = gray;
            if (this.isBlueChannel()) b = gray;

            return a | (r << 16) | (g << 8) | b;
        }
    }
}
