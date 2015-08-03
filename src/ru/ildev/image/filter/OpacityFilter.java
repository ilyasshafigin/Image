/*
 *
 */
package ru.ildev.image.filter;

/**
 * Класс фильтра, устанавливающий прозрачность изображениям.
 *
 * @author Shafigin Ilyas (Шафигин Ильяс) <Ilyas74>
 * @version 0.0.0
 */
public class OpacityFilter extends ColorFilter {

    /**
     * Непрозрачность.
     */
    private int opacity = 255;

    /**
     * Стандартный конструктор.
     *
     * @param opacity непрозрачность.
     */
    public OpacityFilter(int opacity) {
        this.opacity = opacity & 0xff;
    }

    /**
     * Получает значение непрозрачности.
     *
     * @return непрозрачность.
     */
    public int getOpacity() {
        return this.opacity;
    }

    /**
     * Устанавливает непрозрачность.
     *
     * @param opacity непрозрачность
     */
    public void setOpacity(int opacity) {
        this.opacity = opacity & 0xff;
    }

    @Override
    protected int apply(int x, int y, int color) {
        if (this.isMonochrome() || (color & 0xff000000) == 0) {
            return color;
        } else {
            return (color & 0xffffff) | (this.opacity << 24);
        }
    }

}
