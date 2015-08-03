/*
 *
 */
package ru.ildev.image.filter;

import ru.ildev.math.MoreMath;

/**
 * Класс фильтра, накладывающего на изображение скручивание.
 *
 * @author Shafigin Ilyas (Шафигин Ильяс) <Ilyas74>
 * @version 0.1.3
 */
public class BendFilter extends TransformFilter {

    /**
     * Радиус скручивания.
     */
    private float radius = 0.0f;
    /**
     * Расположение скручивания относительно изображения по оси ox от 0 до 1.
     */
    private float centerX = 0.5f;
    /**
     * Расположение скручивания относительно изображения по оси ox от 0 до 1.
     */
    private float centerY = 0.5f;
    /**
     * Амплитуда скручивания.
     */
    private float amplitude = MoreMath.TWO_PI;
    /**
     * Степень увеличения скручивания.
     */
    private float pow = 6.0f;

    private float cx;
    private float cy;
    private int r;
    private int d;
    private float r2;

    /**
     * Стандартный конструктор.
     */
    public BendFilter() {
        this.init();
    }

    /**
     * Конструктор, устанавливающий радиус скручивания.
     *
     * @param radius радиус скручивания.
     */
    public BendFilter(float radius) {
        this.radius = radius;
        this.init();
    }

    /**
     * Конструктор, устанавливающий радиус скручивания и его расположение.
     *
     * @param radius  радиус скручивания.
     * @param centerX расположение центра скручивания по оси ox от 0 до 1.
     * @param centerY расположение центра скручивания по оси oy от 0 до 1.
     */
    public BendFilter(float radius, float centerX, float centerY) {
        this.radius = radius;
        this.centerX = centerX;
        this.centerY = centerY;
        this.init();
    }

    /**
     * Конструктор, устанавливающий радиус скручивания и его расположение.
     *
     * @param radius    радиус скручивания.
     * @param centerX   расположение центра скручивания по оси ox от 0 до 1.
     * @param centerY   расположение центра скручивания по оси oy от 0 до 1.
     * @param amplitude амплитуда скручивания.
     */
    public BendFilter(float radius, float centerX, float centerY, float amplitude) {
        this.radius = radius;
        this.centerX = centerX;
        this.centerY = centerY;
        this.amplitude = amplitude;
        this.init();
    }

    /**
     * Конструктор, устанавливающий радиус скручивания и его расположение.
     *
     * @param radius    радиус скручивания.
     * @param centerX   расположение центра скручивания по оси ox от 0 до 1.
     * @param centerY   расположение центра скручивания по оси oy от 0 до 1.
     * @param amplitude амплитуда скручивания.
     * @param pow       степень увелячения скручивания.
     */
    public BendFilter(float radius, float centerX, float centerY, float amplitude, float pow) {
        this.radius = radius;
        this.centerX = centerX;
        this.centerY = centerY;
        this.amplitude = amplitude;
        this.pow = pow;
        this.init();
    }

    /**
     * Получает радиус скручивания.
     *
     * @return радиус скручивания.
     */
    public float getRadius() {
        return this.radius;
    }

    /**
     * Устанавливает радиус скручивания.
     *
     * @param radius радиус скручивания.
     */
    public void setRadius(float radius) {
        this.radius = radius;
        this.init();
    }

    /**
     * Получает расположение центра скручивания по оси ox.
     *
     * @return расположение центра скручивания по оси ox.
     */
    public float getCenterX() {
        return this.centerX;
    }

    /**
     * Устанавливает расположение центра скручивания по оси ox.
     *
     * @param centerX расположение центра скручивания по оси ox от 0 до 1.
     */
    public void setCenterX(float centerX) {
        this.centerX = centerX;
    }

    /**
     * Получает расположение центра скручивания по оси oy.
     *
     * @return расположение центра скручивания по оси oy.
     */
    public float getCenterY() {
        return this.centerY;
    }

    /**
     * Устанавливает расположение центра скручивания по оси oy.
     *
     * @param centerY расположение центра скручивания по оси oy от 0 до 1.
     */
    public void setCenterY(float centerY) {
        this.centerY = centerY;
    }

    /**
     * Устанавливает расположение центра скручивания.
     *
     * @param centerX расположение центра скручивания по оси ox от 0 до 1.
     * @param centerY расположение центра скручивания по оси oy от 0 до 1.
     */
    public void setCenter(float centerX, float centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
    }

    /**
     * Получает амплитуду скручивания.
     *
     * @return амплитуду скручивания.
     */
    public float getAmplitude() {
        return this.amplitude;
    }

    /**
     * Устанавливает амплитуду скручивания.
     *
     * @param amplitude амплитуда скручивания.
     */
    public void setAmplitude(float amplitude) {
        this.amplitude = amplitude;
        this.init();
    }

    /**
     * Получает степень увелячения скручивания.
     *
     * @return степень увелячения скручивания.
     */
    public float getPow() {
        return this.pow;
    }

    /**
     * Устанавливает степень увелячения скручивания.
     *
     * @param pow степень увелячения скручивания.
     */
    public void setPow(float pow) {
        this.pow = pow;
        this.init();
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
            float invR2 = this.r2 <= 0.0f ? 0.0f : 1.0f / this.r2;
            float z = distance2 * invR2;
            float angle = this.amplitude * MoreMath.pow(1.0f - z, this.pow);
            float sin = MoreMath.sin(angle);
            float cos = MoreMath.cos(angle);

            out[0] = dx * cos - dy * sin + this.cx;
            out[1] = dx * sin + dy * cos + this.cy;
        }
    }

    @Override
    protected void transformBound() {
        this.bound.setRect(this.cx - this.radius, this.cy - this.radius, this.d, this.d);
    }

}
