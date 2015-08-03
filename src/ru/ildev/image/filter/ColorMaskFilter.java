/*
 *
 */
package ru.ildev.image.filter;

/**
 * Класс фильтра, применяющего фильтрируемым изображениям маску цвета, т.е. ключевой цвет будет исключаться в
 * изображениях.
 *
 * @author Shafigin Ilyas (Шафигин Ильяс) <Ilyas74>
 * @version 0.0.0
 */
public class ColorMaskFilter extends ColorFilter {

    /**
     * Ключевой цвет.
     */
    private int mask;

    /**
     * Стандартный конструктор.
     *
     * @param mask маска.
     */
    public ColorMaskFilter(int mask) {
        this.mask = mask;
    }

    /**
     * Получает ключевой цвет.
     *
     * @return ключевой цвет.
     */
    public int getMask() {
        return this.mask;
    }

    /**
     * Устанавливает ключевой цвет.
     *
     * @param mask ключевой цвет.
     */
    public void setMask(int mask) {
        this.mask = mask;
    }

    @Override
    protected int apply(int x, int y, int color) {
        return color & this.mask;
    }

}
