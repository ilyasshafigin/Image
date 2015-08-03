/*
 *
 */
package ru.ildev.image.filter;

/**
 * Фильтр, изменяющий размеры изображений.
 *
 * @author Shafigin Ilyas (Шафигин Ильяс) <Ilyas74>
 * @version 0.0.0
 */
public class ResizeFilter extends TransformFilter {

    /**
     * Ширина изображений.
     */
    private int width = 1;
    /**
     * Высота изображений.
     */
    private int height = 1;

    /**
     * Стандартный конструктор.
     */
    public ResizeFilter() {
    }

    /**
     * Конструктор, устанавливающий размеры изображений.
     *
     * @param width  ширина изображений.
     * @param height высота изображений.
     */
    public ResizeFilter(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Получает ширину изображений.
     *
     * @return ширину изображений.
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Устанавливает ширину изображений.
     *
     * @param width ширину изображений.
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Получает высоту изображений.
     *
     * @return высоту изображений.
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Устанавливает высоту изображений.
     *
     * @param height высота изображений.
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Устанавливает размеры изображений.
     *
     * @param width  ширина изображений.
     * @param height высота изображений.
     */
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void apply(int[] in, int[] out, int width, int height) {
        this.transform.setToScale((float) width / this.width, (float) height / this.height);

        super.apply(in, out, width, height);
    }

}
