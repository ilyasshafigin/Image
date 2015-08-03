/*
 *
 */
package ru.ildev.image.filter;

import ru.ildev.math.MoreMath;

/**
 * Класс фильтра, накладывающего на изображение линзы.
 *
 * @author Shafigin Ilyas (Шафигин Ильяс) <Ilyas74>
 * @version 0.1.3
 */
public class LensFilter extends TransformFilter {

    /**
     * Радиус линзы.
     */
    private float radius = 0.0f;
    /**
     * Расположение линзы относительно изображения по оси ox от 0 до 1.
     */
    private float centerX = 0.5f;
    /**
     * Расположение линзы относительно изображения по оси ox от 0 до 1.
     */
    private float centerY = 0.5f;
    /**
     * Коэффициент преломления.
     */
    private float refractionIndex = 1.5f;

    private float cx;
    private float cy;
    private int r;
    private int d;
    private float r2;

    /**
     * Стандартный конструктор.
     */
    public LensFilter() {
        this.init();
    }

    /**
     * Конструктор, устанавливающий радиус линзы.
     *
     * @param radius радиус линзы.
     */
    public LensFilter(float radius) {
        this.radius = radius;
        this.init();
    }

    /**
     * Конструктор, устанавливающий радиус линзы и ее расположение.
     *
     * @param radius  радиус линзы.
     * @param centerX расположение центра линзы по оси ox от 0 до 1.
     * @param centerY расположение центра линзы по оси oy от 0 до 1.
     */
    public LensFilter(float radius, float centerX, float centerY) {
        this.radius = radius;
        this.centerX = centerX;
        this.centerY = centerY;
        this.init();
    }

    /**
     * Конструктор, устанавливающий радиус линзы и ее расположение.
     *
     * @param radius          радиус линзы.
     * @param centerX         расположение центра линзы по оси ox от 0 до 1.
     * @param centerY         расположение центра линзы по оси oy от 0 до 1.
     * @param refractionIndex коэффициент преломления.
     */
    public LensFilter(float radius, float centerX, float centerY, float refractionIndex) {
        this.radius = radius;
        this.centerX = centerX;
        this.centerY = centerY;
        this.refractionIndex = refractionIndex;
        this.init();
    }

    /**
     * Получает радиус линзы.
     *
     * @return радиус линзы.
     */
    public float getRadius() {
        return this.radius;
    }

    /**
     * Устанавливает радиус линзы.
     *
     * @param radius радиус линзы.
     */
    public void setRadius(float radius) {
        this.radius = radius;
        this.init();
    }

    /**
     * Получает расположение центра линзы по оси ox.
     *
     * @return расположение центра линзы по оси ox.
     */
    public float getCenterX() {
        return this.centerX;
    }

    /**
     * Устанавливает расположение центра линзы по оси ox.
     *
     * @param centerX расположение центра линзы по оси ox от 0 до 1.
     */
    public void setCenterX(float centerX) {
        this.centerX = centerX;
    }

    /**
     * Получает расположение центра линзы по оси oy.
     *
     * @return расположение центра линзы по оси oy.
     */
    public float getCenterY() {
        return this.centerY;
    }

    /**
     * Устанавливает расположение центра линзы по оси oy.
     *
     * @param centerY расположение центра линзы по оси oy от 0 до 1.
     */
    public void setCenterY(float centerY) {
        this.centerY = centerY;
    }

    /**
     * Устанавливает расположение центра линзы.
     *
     * @param centerX расположение центра линзы по оси ox от 0 до 1.
     * @param centerY расположение центра линзы по оси oy от 0 до 1.
     */
    public void setCenter(float centerX, float centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
    }

    /**
     * Получает коэффициент преломления.
     *
     * @return коэффициент преломления.
     */
    public float getRefractionIndex() {
        return this.refractionIndex;
    }

    /**
     * Устанавливает коэффициент преломления.
     *
     * @param refractionIndex коэффициент преломления.
     */
    public void setRefractionIndex(float refractionIndex) {
        this.refractionIndex = refractionIndex;
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
    protected void transformPixel(int x, int y, float[] out) {
        float dx = x - this.cx;
        float dy = y - this.cy;

        float x2 = dx * dx;
        float y2 = dy * dy;

        if ((x2 + y2) >= this.r2) {
            out[0] = x;
            out[1] = y;
        } else {
            float invRefraction = 1.0f / this.refractionIndex;
            float z = MoreMath.sqrt(this.r2 - x2 - y2);
            float z2 = z * z;

            float angle1 = MoreMath.HALF_PI - MoreMath.acos(dx / MoreMath.sqrt(x2 + z2));
            float angle2 = angle1 - MoreMath.asin(MoreMath.sin(angle1) * invRefraction);
            out[0] = -MoreMath.tan(angle2) * z + x;

            angle1 = MoreMath.HALF_PI - MoreMath.acos(dy / MoreMath.sqrt(y2 + z2));
            angle2 = angle1 - MoreMath.asin(MoreMath.sin(angle1) * invRefraction);
            out[1] = -MoreMath.tan(angle2) * z + y;
        }
    }

    @Override
    protected void transformBound() {
        this.bound.setRect(this.cx - this.radius, this.cy - this.radius,
                this.d, this.d);
    }

}
