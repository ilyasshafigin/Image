/*
 *
 */
package ru.ildev.image.filter;

import ru.ildev.math.MoreMath;

/**
 * Класс фильтра, утемняющего изображение.
 *
 * @author Ilyas74
 * @version 0.1.2
 */
public class DarkerFilter extends ColorFilter {

    /**
     * Фактор утемнения, от 0 до 1.
     */
    private float factor;

    /**
     * Конструктор.
     *
     * @param factor фактор утемнения.
     */
    public DarkerFilter(float factor) {
        this.factor = factor;
    }

    /**
     * Получает фактор утемнения.
     *
     * @return фактор утемнения.
     */
    public float getFactor() {
        return this.factor;
    }

    /**
     * Устанавливает фактор утемнения.
     *
     * @param factor фактор утемнения.
     */
    public void setFactor(float factor) {
        this.factor = factor;
    }

    @Override
    protected int apply(int x, int y, int color) {
        if (this.isMonochrome()) {
            return MoreMath.max((int) (color * this.factor), 0);
        } else {
            int a = 0xff000000 & color;
            int r = 0xff & (color >> 16);
            int g = 0xff & (color >> 8);
            int b = 0xff & color;

            //if(this.isAlphaChannel()) a = a;
            if (this.isRedChannel()) r = MoreMath.max((int) (r * this.factor), 0);
            if (this.isGreenChannel()) g = MoreMath.max((int) (g * this.factor), 0);
            if (this.isBlueChannel()) b = MoreMath.max((int) (b * this.factor), 0);

            return a | (r << 16) | (g << 8) | b;
        }
    }

}
