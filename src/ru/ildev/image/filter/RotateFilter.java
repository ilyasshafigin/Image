/*
 *
 */
package ru.ildev.image.filter;

/**
 * Фильтр, поворачивающий изображение.
 *
 * @author Shafigin Ilyas (Шафигин Ильяс) <Ilyas74>
 * @version 0.0.0
 */
public class RotateFilter extends TransformFilter {

    /**
     * Угол поворота в радианах.
     */
    private float angle = 0.0f;
    /**
     * Координаты центра поворота изображения по оси ox от 0 до 1.
     */
    private float centerX = 0.5f;
    /**
     * Координаты центра поворота изображения по оси oy от 0 до 1.
     */
    private float centerY = 0.5f;

    /**
     * Стандартный конструктор.
     */
    public RotateFilter() {
    }

    /**
     * Конструктор, устанавливающий угол поворота относительно центра изображения.
     *
     * @param angle угол поворота в радианах.
     */
    public RotateFilter(float angle) {
        this.angle = angle;
    }

    /**
     * Конструктор, устанавливающий угол поворота и поординаты центра поворота.
     *
     * @param angle   угол поворота в радианах.
     * @param centerX x-координата центра поворота.
     * @param centerY y-координата центра поворота.
     */
    public RotateFilter(float angle, float centerX, float centerY) {
        this.angle = angle;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    /**
     * Получает угол поворота изображения.
     *
     * @return угол поворота изображения.
     */
    public float getAngle() {
        return this.angle;
    }

    /**
     * Устанавливает угол поворота изображения.
     *
     * @param angle угол поворота в радианах.
     */
    public void setAngle(float angle) {
        this.angle = angle;
    }

    /**
     * Получает координату центра поворота изображения по оси ox.
     *
     * @return координату центра поворота изображения по оси ox.
     */
    public float getCenterX() {
        return this.centerX;
    }

    /**
     * Устанавливает координату центра поворота изображения по оси ox.
     *
     * @param centerX x-координата центра поворота изображения.
     */
    public void setCenterX(float centerX) {
        this.centerX = centerX;
    }

    /**
     * Получает координату центра поворота изображения по оси oy.
     *
     * @return координату центра поворота изображения по оси oy.
     */
    public float getCenterY() {
        return this.centerY;
    }

    /**
     * Устанавливает координату центра поворота изображения по оси oy.
     *
     * @param centerY y-координата центра поворота изображения.
     */
    public void setCenterY(float centerY) {
        this.centerY = centerY;
    }

    /**
     * Устанавливает координату центра поворота изображения.
     *
     * @param centerX x-координата центра поворота изображения.
     * @param centerY y-координата центра поворота изображения.
     */
    public void setCenter(float centerX, float centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
    }

    @Override
    public void apply(int[] in, int[] out, int width, int height) {
        this.transform.setToRotation(this.angle, width * this.centerX, height * this.centerY);
        super.apply(in, out, width, height);
    }

}
