/*
 *
 */
package ru.ildev.image.filter;

import ru.ildev.math.MoreMath;

/**
 * Класс фильтра, вращающего изображение в опредененных границах.
 *
 * @author Shafigin Ilyas (Шафигин Ильяс) <Ilyas74>
 * @version 0.1.2
 */
public class TwirlFilter extends TransformFilter {

    /**
     * Угол вращения а радианах.
     */
    private float angle = 0.0f;
    /**
     * Расположение точки относительно изображения по оси ox от 0 до 1.
     */
    private float centerX = 0.5f;
    /**
     * Расположение точки относительно изображения по оси ox от 0 до 1.
     */
    private float centerY = 0.5f;
    /**
     * Радиус.
     */
    private float radius = 50.0f;

    private float cx;
    private float cy;
    private int r;
    private int d;
    private float r2;

    /**
     * Стандартный конструктор.
     */
    public TwirlFilter() {
        this.init();
    }

    /**
     * Конструктор, устанавливающий угол вращения и радиус.
     *
     * @param angle  угол в радианах.
     * @param radius радиус.
     */
    public TwirlFilter(float angle, float radius) {
        this.angle = angle;
        this.radius = radius;
        this.init();
    }

    /**
     * Конструктор, устанавливающий радиус волн и их расположение.
     *
     * @param angle   угол в радианах.
     * @param radius  радиус волн.
     * @param centerX расположение центра линзы по оси ox от 0 до 1.
     * @param centerY расположение центра линзы по оси oy от 0 до 1.
     */
    public TwirlFilter(float angle, float radius, float centerX, float centerY) {
        this.angle = angle;
        this.radius = radius;
        this.centerX = centerX;
        this.centerY = centerY;
        this.init();
    }

    /**
     * Получает угол вращения.
     *
     * @return угол вращения.
     */
    public float getAngle() {
        return this.angle;
    }

    /**
     * Устанавливает угол вращения.
     *
     * @param angle угол вращения.
     */
    public void setAngle(float angle) {
        this.angle = angle;
        this.init();
    }

    /**
     * Получает радиус вращения.
     *
     * @return радиус вращения.
     */
    public float getRadius() {
        return this.radius;
    }

    /**
     * Устанавливает радиус вращения.
     *
     * @param radius радиус вращения.
     */
    public void setRadius(float radius) {
        this.radius = radius;
    }

    /**
     * Получает расположение центра вращения по оси ox.
     *
     * @return расположение центра вращения по оси ox.
     */
    public float getCenterX() {
        return this.centerX;
    }

    /**
     * Устанавливает расположение центра вращения по оси ox.
     *
     * @param centerX расположение центра вращения по оси ox от 0 до 1.
     */
    public void setCenterX(float centerX) {
        this.centerX = centerX;
    }

    /**
     * Получает расположение центра вращения по оси oy.
     *
     * @return расположение центра вращения по оси oy.
     */
    public float getCenterY() {
        return this.centerY;
    }

    /**
     * Устанавливает расположение центра вращения по оси oy.
     *
     * @param centerY расположение центра вращения по оси oy от 0 до 1.
     */
    public void setCenterY(float centerY) {
        this.centerY = centerY;
    }

    /**
     * Устанавливает расположение центра вращения.
     *
     * @param centerX расположение центра вращения по оси ox от 0 до 1.
     * @param centerY расположение центра вращения по оси oy от 0 до 1.
     */
    public void setCenter(float centerX, float centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
    }

    private void init() {
        this.r = (int) this.radius;
        this.d = this.r * 2;
        this.r2 = this.radius * this.radius;
    }

    @Override
    public void apply(int[] in, int[] out, int width, int height) {
        this.cx = width * this.centerX;
        this.cy = height * this.centerY;
        super.apply(in, out, width, height);
    }

    @Override
    public void transformPixel(int x, int y, float[] out) {
        float dx = x - this.cx;
        float dy = y - this.cy;
        float distance2 = dx * dx + dy * dy;

        if (distance2 >= this.r2) {
            out[0] = x;
            out[1] = y;
        } else {
            float distance = MoreMath.sqrt(distance2);
            float angle = MoreMath.atan2(dy, dx) + this.angle * (this.radius - distance) / this.radius;

            out[0] = distance * MoreMath.cos(angle) + this.cx;
            out[1] = distance * MoreMath.sin(angle) + this.cy;
        }
    }

    @Override
    protected void transformBound() {
        this.bound.setRect(this.cx - this.radius, this.cy - this.radius, this.d, this.d);
    }

}
