/*
 *
 */
package ru.ildev.image;

import ru.ildev.color.Color;

import java.awt.image.BufferedImage;

/**
 * Класс генератора гистограммы изображений.
 *
 * @author Ilyas74
 * @version 1.1.1
 */
public class Histogram {

    /**
     * Индекс краского компонента гистограммы.
     */
    public static final int RED = 0;
    /**
     * Индекс зеленого компонента гистограммы.
     */
    public static final int GREEN = 1;
    /**
     * Индекс синего компонента гистограммы.
     */
    public static final int BLUE = 2;
    /**
     * Индекс серого компонента гистограммы.
     */
    public static final int GRAY = 3;

    /**
     * Массив гистограммы изображения всех компонентов.
     */
    protected int[][] histogram = new int[4][256];
    /**
     * Количество рассматриваемых пикселей изображения.
     */
    protected int size = 0;
    /**
     * Массив минимальных значений всех компонентов.
     */
    protected int[] minValue = new int[4];
    /**
     * Массив максимальных значений всех компонентов.
     */
    protected int[] maxValue = new int[4];

    /**
     * Конструктор.
     *
     * @param image  изображение.
     * @param width  ширина области изображения для создания гистограммы.
     * @param height высота области изображения для создания гистограммы.
     * @param offset положения области изображения для создания гистограммы.
     */
    public Histogram(BufferedImage image, int width, int height, int offset) {
        if (image == null) throw new NullPointerException("image == null");

        this.size = width * height;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                int r = 0xff & (rgb >> 16);
                int g = 0xff & (rgb >> 8);
                int b = 0xff & rgb;
                int gray = (int) (Color.brightness(r, g, b) * Color.FLOAT_TO_INT);

                this.histogram[RED][r]++;
                this.histogram[GREEN][g]++;
                this.histogram[BLUE][b]++;
                this.histogram[GRAY][gray]++;
            }
        }

        for (int i = 0; i < 4; i++) {
            int min = Integer.MAX_VALUE;
            int max = Integer.MIN_VALUE;

            for (int j = 0; j < 256; j++) {
                this.minValue[i] = Math.min(min, this.histogram[i][j]);
            }

            for (int j = 0; j < 256; j++) {
                this.maxValue[i] = Math.max(max, this.histogram[i][j]);
            }
        }
    }

    /**
     * Конструктор.
     *
     * @param pixels массив пикселей изображения.
     * @param width  ширина области изображения для создания гистограммы.
     * @param height высота области изображения для создания гистограммы.
     * @param offset положения области изображения для создания гистограммы.
     */
    public Histogram(int[] pixels, int width, int height, int offset) {
        if (pixels == null) throw new NullPointerException("pixels == null");
        if (pixels.length + offset < width * height)
            throw new IllegalArgumentException("Illegal array size");

        this.size = width * height;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int index = x + y * width;
                int rgb = pixels[index];
                int r = 0xff & (rgb >> 16);
                int g = 0xff & (rgb >> 8);
                int b = 0xff & rgb;
                int gray = (int) (Color.brightness(r, g, b) * Color.FLOAT_TO_INT);

                this.histogram[RED][r]++;
                this.histogram[GREEN][g]++;
                this.histogram[BLUE][b]++;
                this.histogram[GRAY][gray]++;
            }
        }

        for (int i = 0; i <= GRAY; i++) {
            int min = Integer.MAX_VALUE;
            int max = Integer.MIN_VALUE;

            for (int j = 0; j < 256; j++) {
                this.minValue[i] = Math.min(min, this.histogram[i][j]);
            }

            for (int j = 0; j < 256; j++) {
                this.maxValue[i] = Math.max(max, this.histogram[i][j]);
            }
        }
    }

    /**
     * Получает количество рассматриваемыз пикселей.
     *
     * @return количество рассматриваемыз пикселей.
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Получает значение компонента гистограммы.
     *
     * @param component индекс компонента.
     * @param value     значение, от 0 до 255.
     * @return значение компонента гистограммы.
     */
    public int getValue(int component, int value) {
        if (this.size < 1 || component < RED || component > GRAY || value < 0 || value > 255) {
            return -1;
        } else {
            return this.histogram[component][value];
        }
    }

    /**
     * Получает минимальное значение компонента гистограммы.
     *
     * @param component индекс компонента.
     * @return минимальное значение компонента гистограммы.
     */
    public int getMinValue(int component) {
        if (component < RED || component > GRAY) {
            return -1;
        } else {
            return this.minValue[component];
        }
    }

    /**
     * Получает максимальное значение компонента гистограммы.
     *
     * @param component индекс компонента.
     * @return максимальное значение компонента гистограммы.
     */
    public int getMaxValue(int component) {
        if (component < RED || component > GRAY) {
            return -1;
        } else {
            return this.maxValue[component];
        }
    }

}
