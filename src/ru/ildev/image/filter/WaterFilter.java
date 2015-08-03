/*
 *
 */
package ru.ildev.image.filter;

import ru.ildev.math.MoreMath;

/**
 * Класс фильтра, накладывающего на изображение волны воды.
 *
 * @author Shafigin Ilyas (Шафигин Ильяс) <Ilyas74>
 * @version 0.1.2
 */
public class WaterFilter extends TransformFilter {

    /**
     * Длина волны.
     */
    private float wavelength = 16.0f;
    /**
     * Амплитуда волны.
     */
    private float amplitude = 10.0f;
    /**
     * Фаза.
     */
    private float phase = 0.0f;
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
    public WaterFilter() {
        this.init();
    }

    /**
     * Конструктор, устанавливающий радиус волн.
     *
     * @param radius радиус волн.
     */
    public WaterFilter(float radius) {
        this.radius = radius;
        this.init();
    }

    /**
     * Конструктор, устанавливающий радиус волн и их расположение.
     *
     * @param radius  радиус волн.
     * @param centerX расположение центра линзы по оси ox от 0 до 1.
     * @param centerY расположение центра линзы по оси oy от 0 до 1.
     */
    public WaterFilter(float radius, float centerX, float centerY) {
        this.radius = radius;
        this.centerX = centerX;
        this.centerY = centerY;
        this.init();
    }

    /**
     * Конструктор, устанавливающий радиус волн и их расположение.
     *
     * @param radius     радиус волн.
     * @param centerX    расположение центра волн по оси ox от 0 до 1.
     * @param centerY    расположение центра волн по оси oy от 0 до 1.
     * @param wavelength длина волны.
     * @param amplitude  амплитуда волны.
     */
    public WaterFilter(float radius, float centerX, float centerY,
                       float wavelength, float amplitude) {
        this.radius = radius;
        this.centerX = centerX;
        this.centerY = centerY;
        this.wavelength = wavelength;
        this.amplitude = amplitude;
        this.init();
    }

    /**
     * Получает значение длины волны.
     *
     * @return длину волны.
     */
    public float getWavelength() {
        return this.wavelength;
    }

    /**
     * Устанавливает длину волны.
     *
     * @param wavelength длина волны.
     */
    public void setWavelength(float wavelength) {
        this.wavelength = wavelength;
        this.init();
    }

    /**
     * Получает амплитуду волн.
     *
     * @return амплитуда.
     */
    public float getAmplitude() {
        return this.amplitude;
    }

    /**
     * Устанавливает амплитуду волн.
     *
     * @param amplitude амплитуда.
     */
    public void setAmplitude(float amplitude) {
        this.amplitude = amplitude;
        this.init();
    }

    /**
     * Получает фазу.
     *
     * @return фазу.
     */
    public float getPhase() {
        return this.phase;
    }

    /**
     * Устанавливает фазу.
     *
     * @param phase фаза.
     */
    public void setPhase(float phase) {
        this.phase = phase;
        this.init();
    }

    /**
     * Получает радиус волн.
     *
     * @return радиус волн.
     */
    public float getRadius() {
        return this.radius;
    }

    /**
     * Устанавливает радиус волн.
     *
     * @param radius радиус волн.
     */
    public void setRadius(float radius) {
        this.radius = radius;
    }

    /**
     * Получает расположение центра волн по оси ox.
     *
     * @return расположение центра волн по оси ox.
     */
    public float getCenterX() {
        return this.centerX;
    }

    /**
     * Устанавливает расположение центра волн по оси ox.
     *
     * @param centerX расположение центра волн по оси ox от 0 до 1.
     */
    public void setCenterX(float centerX) {
        this.centerX = centerX;
    }

    /**
     * Получает расположение центра волн по оси oy.
     *
     * @return расположение центра волн по оси oy.
     */
    public float getCenterY() {
        return this.centerY;
    }

    /**
     * Устанавливает расположение центра волн по оси oy.
     *
     * @param centerY расположение центра волн по оси oy от 0 до 1.
     */
    public void setCenterY(float centerY) {
        this.centerY = centerY;
    }

    /**
     * Устанавливает расположение центра волн.
     *
     * @param centerX расположение центра волн по оси ox от 0 до 1.
     * @param centerY расположение центра волн по оси oy от 0 до 1.
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
            float amount = this.amplitude * MoreMath.sin(distance / this.wavelength * MoreMath.TWO_PI - this.phase);
            if (distance != 0.0f) {
                amount *= (this.radius - distance) / this.radius;
                amount *= this.wavelength / distance;
            }

            out[0] = dx * amount + x;
            out[1] = dy * amount + y;
        }
    }

    @Override
    protected void transformBound() {
        this.bound.setRect(this.cx - this.radius, this.cy - this.radius, this.d, this.d);
    }

}
