/*
 *
 */
package ru.ildev.image.filter;

/**
 * Класс фильтра, смещающего изображение по осим Ox и Oy.
 *
 * @author Shafigin Ilyas (Шафигин Ильяс) <Ilyas74>
 * @version 0.0.1
 */
public class OffsetFilter extends TransformFilter {

    /**
     * Смещение изображений по оси Ox.
     */
    private float xOffset;
    /**
     * Смещение изображений по оси Oy.
     */
    private float yOffset;

    /**
     * Стандартный конструктор.
     */
    public OffsetFilter() {
        this(0.0f, 0.0f);
    }

    /**
     * Конструктор, устанавливающий значения смещений изображений.
     *
     * @param xOffset значение смещения изображений по оси Ox.
     * @param yOffset значение смещения изображений по оси Oy.
     */
    public OffsetFilter(float xOffset, float yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    /**
     * Конструктор, устанавливающий значения смещений изображений и действие с их краями.
     *
     * @param xOffset    значение смещения изображений по оси Ox.
     * @param yOffset    значение смещения изображений по оси Oy.
     * @param edgeAction действие с краями изображений.
     */
    public OffsetFilter(float xOffset, float yOffset, int edgeAction) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.edgeAction = edgeAction;
    }

    /**
     * Конструктор, устанавливающий значения смещений изображений, действие с их краями и тип интерполяции.
     *
     * @param xOffset       значение смещения изображений по оси Ox.
     * @param yOffset       значение смещения изображений по оси Oy.
     * @param edgeAction    действие с краями изображений.
     * @param interpolation тип интерполяции.
     */
    public OffsetFilter(float xOffset, float yOffset, int edgeAction, int interpolation) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.edgeAction = edgeAction;
        this.interpolation = interpolation;
    }

    /**
     * Получает смещение изображений по оси Ox.
     *
     * @return смещение изображений по оси Ox.
     */
    public float getXOffset() {
        return this.xOffset;
    }

    /**
     * Устанавливает смещение изображений по оси Ox.
     *
     * @param xOffset смещение изображений по оси Ox.
     */
    public void setXOffset(float xOffset) {
        this.xOffset = xOffset;
    }

    /**
     * Получает смещение изображений по оси Oy.
     *
     * @return смещение изображений по оси Oy.
     */
    public float getYOffset() {
        return this.yOffset;
    }

    /**
     * Устанавливает смещение изображений по оси Oy.
     *
     * @param yOffset смещение изображений по оси Oy.
     */
    public void setYOffset(float yOffset) {
        this.yOffset = yOffset;
    }

    @Override
    protected void transformPixel(int x, int y, float[] out) {
        out[0] = x - this.xOffset;
        out[1] = y - this.yOffset;
    }

}
