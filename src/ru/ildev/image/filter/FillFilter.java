/*
 *
 */
package ru.ildev.image.filter;

/**
 * Класс фильтра, заливающего изображения одним цветом.
 *
 * @author Shafigin Ilyas (Шафигин Ильяс) <Ilyas74>
 * @version 0.0.0
 */
public class FillFilter extends ColorFilter {

    /**
     * Цвет заливки.
     */
    private int color = 0x000000;

    /**
     * Стандартный конструктор.
     */
    public FillFilter() {
    }

    /**
     * Конструктор, устанавливающий цвет заливки.
     *
     * @param color цвет заливки.
     */
    public FillFilter(int color) {
        this.color = color;
    }

    /**
     * Получает цвет заливки.
     *
     * @return цвет заливки.
     */
    public int getColor() {
        return this.color;
    }

    /**
     * Устанавливает цвет заливки.
     *
     * @param color цвет заливки.
     */
    public void setColor(int color) {
        this.color = color;
    }

    @Override
    protected int apply(int x, int y, int color) {
        return this.color;
    }

}
