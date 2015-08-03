/*
 *
 */
package ru.ildev.image.filter;

/**
 * Фильтр, масштабирующий изображения.
 *
 * @author Shafigin Ilyas (Шафигин Ильяс) <Ilyas74>
 * @version 0.0.0
 */
public class ScaleFilter extends TransformFilter {

    /**
     * Масштаб изображений по оси ox.
     */
    private float scaleX = 1.0f;
    /**
     * Масштаб изображений по оси oy.
     */
    private float scaleY = 1.0f;
    /**
     * Координаты центра масштабирования изображения по оси ox от 0 до 1.
     */
    private float centerX = 0.5f;
    /**
     * Координаты центра масштабирования изображения по оси oy от 0 до 1.
     */
    private float centerY = 0.5f;

    /**
     * Стандартный конструктор.
     */
    public ScaleFilter() {
    }

    /**
     * Конструктор, устанавливающий масштаб изображений.
     *
     * @param scale масштаб.
     */
    public ScaleFilter(float scale) {
        this.scaleX = scale;
        this.scaleY = scale;
    }

    /**
     * Конструктор, устанавливающий масштаб и поординаты центра масштабирования.
     *
     * @param scale   масштаб.
     * @param centerX x-координата центра поворота.
     * @param centerY y-координата центра поворота.
     */
    public ScaleFilter(float scale, float centerX, float centerY) {
        this.scaleX = scale;
        this.scaleY = scale;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    /**
     * Конструктор, устанавливающий масштаб изображений.
     *
     * @param scaleX масштаб по оси ox.
     * @param scaleY масштаб по оси oy.
     */
    public ScaleFilter(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    /**
     * Конструктор, устанавливающий масштаб и поординаты центра масштабирования.
     *
     * @param scaleX  масштаб по оси ox.
     * @param scaleY  масштаб по оси oy.
     * @param centerX x-координата центра поворота.
     * @param centerY y-координата центра поворота.
     */
    public ScaleFilter(float scaleX, float scaleY, float centerX, float centerY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    /**
     * Получает масштаб изображений по оси ox.
     *
     * @return масштаб изображений.
     */
    public float getScaleX() {
        return this.scaleX;
    }

    /**
     * Устанавливает масштаб изображений по оси ox.
     *
     * @param scaleX масштаб по оси ox.
     */
    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    /**
     * Получает масштаб изображений по оси oy.
     *
     * @return масштаб изображений.
     */
    public float getScaleY() {
        return this.scaleY;
    }

    /**
     * Устанавливает масштаб изображений по оси oy.
     *
     * @param scaleY масштаб по оси oy.
     */
    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    /**
     * Устанавливает масштаб изображений.
     *
     * @param scale масштаб.
     */
    public void setScale(float scale) {
        this.scaleX = scale;
        this.scaleY = scale;
    }

    /**
     * Устанавливает масштаб изображений.
     *
     * @param scaleX масштаб по оси ox.
     * @param scaleY масштаб по оси oy.
     */
    public void setScale(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    /**
     * Получает координату центра масштабирования изображения по оси ox.
     *
     * @return координату центра масштабирования изображения по оси ox.
     */
    public float getCenterX() {
        return this.centerX;
    }

    /**
     * Устанавливает координату центра масштабирования изображения по оси ox.
     *
     * @param centerX x-координата центра масштабирования изображения.
     */
    public void setCenterX(float centerX) {
        this.centerX = centerX;
    }

    /**
     * Получает координату центра масштабирования изображения по оси oy.
     *
     * @return координату центра масштабирования изображения по оси oy.
     */
    public float getCenterY() {
        return this.centerY;
    }

    /**
     * Устанавливает координату центра масштабирования изображения по оси oy.
     *
     * @param centerY y-координата центра масштабирования изображения.
     */
    public void setCenterY(float centerY) {
        this.centerY = centerY;
    }

    /**
     * Устанавливает координату центра масштабирования изображения.
     *
     * @param centerX x-координата центра масштабирования изображения.
     * @param centerY y-координата центра масштабирования изображения.
     */
    public void setCenter(float centerX, float centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
    }

    @Override
    public void apply(int[] in, int[] out, int width, int height) {
        float x = this.centerX * width;
        float y = this.centerY * height;

        this.transform.identity();
        this.transform.translateGlobal(x - x * this.scaleX, y - y * this.scaleY);
        this.transform.scale(this.scaleX, this.scaleY);
        super.apply(in, out, width, height);
    }

}
