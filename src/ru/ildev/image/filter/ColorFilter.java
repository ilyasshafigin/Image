/*
 *
 */
package ru.ildev.image.filter;

/**
 * Класс цветового фильтра изображения. Он содержит метод, который запускается для каждого пикселя с параметрами
 * положения пикселя в изображении и его цветa.
 *
 * @author Ilyas74
 * @version 0.0.1
 */
public abstract class ColorFilter extends Filter {

    @Override
    public void apply(int[] in, int[] out, int width, int height) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int index = x + y * width;
                out[index] = this.apply(x, y, in[index]);
            }
        }
    }

    /**
     * Применяет фильтр для пикселя изображения.
     *
     * @param x     x-координата пикселя.
     * @param y     y-координата пикселя.
     * @param color цвет пикселя.
     * @return фильтрированный RGBA цвет.
     */
    protected abstract int apply(int x, int y, int color);

}
