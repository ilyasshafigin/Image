/*
 *
 */
package ru.ildev.image.filter;

import ru.ildev.math.MoreMath;

/**
 * Класс фильтра, осветляющего изображение.
 *
 * @author Ilyas74
 * @version 0.1.3
 */
public class BrighterFilter extends ColorFilter {

    /**
     * Фактор осветления, от 0 до 1.
     */
    private float factor;

    /**
     * Конструктор.
     *
     * @param factor фактор осветления.
     */
    public BrighterFilter(float factor) {
        this.factor = factor;
    }

    /**
     * Получает фактор осветления.
     *
     * @return фактор осветления.
     */
    public float getFactor() {
        return this.factor;
    }

    /**
     * Устанавливает фактор осветления.
     *
     * @param factor фактор осветления.
     */
    public void setFactor(float factor) {
        this.factor = factor;
    }

    @Override
    protected int apply(int x, int y, int color) {
        if (this.isMonochrome()) {
            if (color == 0) {
                return color;
            } else {
                int i = (int) (1.0f / (1.0f - this.factor));
                if (color > 0 && color < i) color = i;

                return MoreMath.min((int) (color / this.factor), 255);
            }
        } else {
            int a = 0xff000000 & color;
            int r = 0xff & (color >> 16);
            int g = 0xff & (color >> 8);
            int b = 0xff & color;

            if (r == 0 && g == 0 && b == 0) {
                return a | (r << 16) | (g << 8) | b;
            } else {
                int i = (int) (1.0f / (1.0f - this.factor));
                if (r > 0 && r < i) r = i;
                if (g > 0 && g < i) g = i;
                if (b > 0 && b < i) b = i;

                //if(this.isAlphaChannel()) a = a;
                if (this.isRedChannel()) r = MoreMath.min((int) (r / this.factor), 255);
                if (this.isGreenChannel()) g = MoreMath.min((int) (g / this.factor), 255);
                if (this.isBlueChannel()) b = MoreMath.min((int) (b / this.factor), 255);

                return a | (r << 16) | (g << 8) | b;
            }
        }
    }

}
