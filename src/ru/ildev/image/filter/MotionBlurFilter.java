/*
 *
 */
package ru.ildev.image.filter;

import ru.ildev.color.Color;
import ru.ildev.geom.Transform2;
import ru.ildev.math.MoreMath;

/**
 * Класс фильтра, размывающего изображение, как бы, в движении.
 *
 * @author Shafigin Ilyas (Шафигин Ильяс) <Ilyas74>
 * @version 0.0.1
 */
public class MotionBlurFilter extends Filter {

    /**
     * Центр размытия по оси x в процентах от 0 до 1.
     */
    private float centerX = 0.0f;
    /**
     * Центр размытия по оси y в процентах от 0 до 1.
     */
    private float centerY = 0.0f;
    /**
     * Начальный угол размытия в радианах.
     */
    private float angle = 0.0f;
    /**
     * Расстояние размытия, начиная от центра в пикселях.
     */
    private float distance = 1.0f;
    /**
     * Поворот размытия, начиная от центра в радианах.
     */
    private float rotation = 0.0f;
    /**
     * Масштабирование размытия, начиная от центра.
     */
    private float zoom = 0.0f;

    /**
     * Стандартный конструктор.
     */
    public MotionBlurFilter() {
    }

    /**
     * Конструктор, устанавливающий положение центра размытия и начальный угол.
     *
     * @param centerX x-координата центра размытия в процентах от 0 до 1.
     * @param centerY y-координата центра размытия в процентах от 0 до 1.
     * @param angle   начальный угол в радианах.
     */
    public MotionBlurFilter(float centerX, float centerY, float angle) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.angle = angle;
    }

    /**
     * Конструктор, устанавливающий положение центра размытия и начальный угол и все остальные параметры.
     *
     * @param centerX  x-координата центра размытия в процентах от 0 до 1.
     * @param centerY  y-координата центра размытия в процентах от 0 до 1.
     * @param angle    начальный угол в радианах.
     * @param distance расстояние размытия.
     * @param rotation поворот размытия.
     * @param zoom     масштабирование размытия.
     */
    public MotionBlurFilter(float centerX, float centerY, float angle, float distance, float rotation, float zoom) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.angle = angle;
        this.distance = distance;
        this.rotation = rotation;
        this.zoom = zoom;
    }

    /**
     * Получает центр размытия по оси x в процентах от 0 до 1.
     *
     * @return центр размытия по оси x в процентах от 0 до 1.
     */
    public float getCenterX() {
        return this.centerX;
    }

    /**
     * Устанавливает центр размытия по оси x в процентах от 0 до 1.
     *
     * @param centerX центр размытия по оси x в процентах от 0 до 1.
     */
    public void setCenterX(float centerX) {
        this.centerX = centerX;
    }

    /**
     * Получает центр размытия по оси y в процентах от 0 до 1.
     *
     * @return центр размытия по оси y в процентах от 0 до 1.
     */
    public float getCenterY() {
        return this.centerY;
    }

    /**
     * Устанавливает центр размытия по оси y в процентах от 0 до 1.
     *
     * @param centerY центр размытия по оси y в процентах от 0 до 1.
     */
    public void setCenterY(float centerY) {
        this.centerY = centerY;
    }

    /**
     * Устанавливает центр размытия.
     *
     * @param centerX центр размытия по оси x в процентах от 0 до 1.
     * @param centerY центр размытия по оси y в процентах от 0 до 1.
     */
    public void setCenter(float centerX, float centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
    }

    /**
     * Получает начальный угол размытия.
     *
     * @return начальный угол размытия.
     */
    public float getAngle() {
        return this.angle;
    }

    /**
     * Устанавливает начальный угол размытия.
     *
     * @param angle угол в радианах.
     */
    public void setAngle(float angle) {
        this.angle = angle;
    }

    /**
     * Получает расстояние размытия.
     *
     * @return расстояние размытия.
     */
    public float getDistance() {
        return this.distance;
    }

    /**
     * Устанавливает расстояние размытия.
     *
     * @param distance расстояние размытия.
     */
    public void setDistance(float distance) {
        this.distance = distance;
    }

    /**
     * Получает поворот размытия.
     *
     * @return поворот размытия.
     */
    public float getRotation() {
        return this.rotation;
    }

    /**
     * Устанавливает поворот размытия.
     *
     * @param rotation угол поворота размытия.
     */
    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    /**
     * Получает масштабирование размытия.
     *
     * @return масштабирование размытия.
     */
    public float getZoom() {
        return this.zoom;
    }

    /**
     * Устанавливает масштабирование размытия.
     *
     * @param zoom масштабирование размытия.
     */
    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    @Override
    public void apply(int[] in, int[] out, int width, int height) {
        boolean rchannel = this.isRedChannel();
        boolean gchannel = this.isGreenChannel();
        boolean bchannel = this.isBlueChannel();
        boolean achannel = this.isAlphaChannel();
        boolean monochrome = this.isMonochrome();

        int cx = (int) (this.centerX * width);
        int cy = (int) (this.centerY * height);

        float imageRadius = MoreMath.mag(cx, cy);
        float translateX = this.distance * MoreMath.cos(this.angle);
        float translateY = this.distance * MoreMath.sin(this.angle);
        float maxDistance = this.distance + Math.abs(this.rotation * imageRadius) + this.zoom * imageRadius;

        int repetitions = (int) maxDistance;
        int index = 0;

        Transform2 transform = new Transform2();
        int[] point = new int[2];

        if (monochrome) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int color = 0;
                    int count = 0;

                    for (int i = 0; i < repetitions; i++) {
                        float k = (float) i / repetitions;

                        point[0] = x - cx;
                        point[1] = y - cy;
                        transform.identity();
                        transform.translateGlobal(translateX * k, translateY * k);
                        transform.scale(1.0f - this.zoom * k);
                        if (this.rotation != 0.0f) transform.rotate(this.rotation * k);
                        transform.transform(point, 0, point, 0, 1);
                        int newX = point[0] + cx, newY = point[1] + cy;

                        if (newX < 0 || newX >= width) break;
                        if (newY < 0 || newY >= height) break;

                        color += in[newX + newY * width];
                        count++;
                    }

                    out[index] = count == 0 ? in[index] : Color.clamp(color / count);
                    index++;
                }
            }
        } else {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int a = 0, r = 0, g = 0, b = 0;
                    int count = 0;

                    for (int i = 0; i < repetitions; i++) {
                        float k = (float) i / repetitions;

                        point[0] = x - cx;
                        point[1] = y - cy;
                        transform.identity();
                        transform.translateGlobal(translateX * k, translateY * k);
                        transform.scale(1.0f - this.zoom * k);
                        if (this.rotation != 0.0f) transform.rotate(this.rotation * k);
                        transform.transform(point, 0, point, 0, 1);
                        int newX = point[0] + cx, newY = point[1] + cy;

                        if (newX < 0 || newX >= width) break;
                        if (newY < 0 || newY >= height) break;

                        int rgb = in[newX + newY * width];
                        if (achannel) a += 0xff & (rgb >> 24);
                        if (rchannel) r += 0xff & (rgb >> 16);
                        if (gchannel) g += 0xff & (rgb >> 8);
                        if (bchannel) b += 0xff & rgb;

                        count++;
                    }

                    if (count == 0) {
                        out[index] = in[index];
                    } else {
                        a = achannel ? Color.clamp(a / count) : 0xff & (in[index] >> 24);
                        r = rchannel ? Color.clamp(r / count) : 0xff & (in[index] >> 16);
                        g = gchannel ? Color.clamp(g / count) : 0xff & (in[index] >> 8);
                        b = bchannel ? Color.clamp(b / count) : 0xff & in[index];

                        out[index] = (a << 24) | (r << 16) | (g << 8) | b;
                    }
                    index++;
                }
            }
        }
    }

}
