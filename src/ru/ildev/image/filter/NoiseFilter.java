/*
 *
 */
package ru.ildev.image.filter;

import ru.ildev.color.Color;
import ru.ildev.math.Noise;

/**
 * Класс фильтра, заполняющего изображение серым шумом Перлина.
 *
 * @author Shafigin Ilyas (Шафигин Ильяс) <Ilyas74>
 * @version 0.1.1
 */
public class NoiseFilter extends ColorFilter {

    /**
     * Объект генератора шума.
     */
    private Noise noise;
    /**
     * Z-координата шума.
     */
    private float z = 0.0f;

    /**
     * Стандартный конструктор.
     *
     * @param noise генератор шума.
     */
    public NoiseFilter(Noise noise) {
        if(noise == null) throw new NullPointerException("noise == null");
        this.noise = noise;
    }

    /**
     * Стандартный конструктор.
     *
     * @param noise генератор шума.
     * @param z     z-координата щума.
     */
    public NoiseFilter(Noise noise, float z) {
        if(noise == null) throw new NullPointerException("noise == null");
        this.noise = noise;
    }

    /**
     * Получает генератор шума.
     *
     * @return генератор шума.
     */
    public Noise getNoise() {
        return this.noise;
    }

    /**
     * Устанавливает генератор шума.
     *
     * @param noise генератор шума.
     */
    public void setNoise(Noise noise) {
        if(noise == null) throw new NullPointerException("noise == null");
        this.noise = noise;
    }

    /**
     * Получает z-координату щума.
     *
     * @return z-координату щума.
     */
    public float getZ() {
        return this.z;
    }

    /**
     * Устанавливает z-координату щума.
     *
     * @param z z-координата щума.
     */
    public void setZ(float z) {
        this.z = z;
    }

    @Override
    protected int apply(int x, int y, int color) {
        int a = 255;
        int r = 0;
        int g = 0;
        int b = 0;
        int gray = (int) (this.noise.noise(x, y, this.z) * Color.FLOAT_TO_INT);

        if (this.isRedChannel()) r = gray;
        if (this.isGreenChannel()) g = gray;
        if (this.isBlueChannel()) b = gray;

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

}
