/*
*
*/
package ru.ildev.image.filter;

import ru.ildev.math.MoreMath;


/**
 * Фильтр наращивания. Фильтр наращивания служит для получения морфологического расширения. Работает на подобии
 * медианного фильтра, только находит не среднее значение цвета, а пиксель с максимальной интенсивностью из окресности.
 * В результате наращивания происходит увеличение якрих объектов. Фильтр наращивание может быть использован для
 * увеличения бликов, ярких отражений.
 *
 * @author Ilyas74
 * @version 0.2.2
 */
public class BuildingFilter extends ConvolveFilter {

    /**
     * Стандартный конструктор.
     *
     * @param size размер матрицы, т.е длина стороны прямоугольника матрицы.
     */
    public BuildingFilter(int size) {
        this(size, ConvolveFilter.EXTEND_EDGES);
    }

    /**
     * Стандартный конструктор.
     *
     * @param size       размер матрицы, т.е длина стороны прямоугольника матрицы.
     * @param edgeAction действие с краями.
     */
    public BuildingFilter(int size, int edgeAction) {
        if (size <= 0)
            throw new IllegalArgumentException("size <= 0");

        int length = size * size;
        float[] matrix = new float[length];
        for (int i = 0; i < length; i++) matrix[i] = 1;

        this.kernel = new Kernel(size, size, matrix, 1.0f, 0);
        this.edgeAction = edgeAction;
    }

    /**
     * Конструктор, устанавливающий матрицу свертки. Элемент, значение которого не рано нулю, значит, что данный пиксель
     * будет рассматриваться.
     *
     * @param kernel матрица свертки.
     */
    public BuildingFilter(Kernel kernel) {
        super(kernel, ConvolveFilter.EXTEND_EDGES);
    }

    /**
     * Конструктор, устанавливающий матрицу свертки. Элемент, значение которого не рано нулю, значит, что данный пиксель
     * будет рассматриваться.
     *
     * @param kernel     матрица свертки.
     * @param edgeAction действие с краями.
     */
    public BuildingFilter(Kernel kernel, int edgeAction) {
        super(kernel, edgeAction);
    }

    @Override
    public void apply(int[] in, int[] out, int width, int height) {
        int w = this.kernel.width;
        int h = this.kernel.height;
        int hw = w / 2;
        int hh = h / 2;
        int size = w * h;

        boolean rchannel = this.isRedChannel();
        boolean gchannel = this.isGreenChannel();
        boolean bchannel = this.isBlueChannel();
        boolean achannel = this.isAlphaChannel();
        boolean monochrome = this.isMonochrome();

        if (monochrome) {
            // Проходим по каждому пикселю изображения.
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int[] a = new int[size];
                    int index = 0;

                    // Проходим по каждому пикселю матрицы.
                    for (int j = 0; j < h; j++) {
                        // Находим позиции пикселя матрицы относительно изображения по y.
                        int pixelPosY = y + (j - hh);

                        int yOff;
                        if (0 <= pixelPosY && pixelPosY < height) {
                            yOff = pixelPosY * width;
                        } else if (this.edgeAction == ConvolveFilter.EXTEND_EDGES) {
                            yOff = y * width;
                        } else if (this.edgeAction == ConvolveFilter.WRAP_EDGES) {
                            yOff = ((pixelPosY + height) % height) * width;
                        } else {
                            continue;
                        }

                        for (int i = 0; i < w; i++) {
                            float k = this.kernel.matrix[i + j * w];

                            // Если значение матрицы равно нулю, то проходим дальше.
                            if (k == 0.0f) continue;

                            // Находим позиции пикселя матрицы относительно изображения по x.
                            int pixelPosX = x + (i - hw);

                            int xOff;
                            if (0 <= pixelPosX && pixelPosX < width) {
                                xOff = pixelPosX;
                            } else if (this.edgeAction == ConvolveFilter.EXTEND_EDGES) {
                                xOff = x;
                            } else if (this.edgeAction == ConvolveFilter.WRAP_EDGES) {
                                xOff = (x + width) % width;
                            } else {
                                continue;
                            }

                            // Находим значение RGBA цвета в массиве.
                            a[index++] = in[xOff + yOff];
                        }
                    }

                    // Контролируем переполнения переменных и записываем значения
                    // в результирующее изображение.
                    out[x + y * width] = MoreMath.max(a);
                }
            }
        } else {
            // Проходим по каждому пикселю изображения.
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int[] aa = new int[size];
                    int[] ar = new int[size];
                    int[] ag = new int[size];
                    int[] ab = new int[size];
                    int index = 0;

                    // Проходим по каждому пикселю матрицы.
                    for (int j = 0; j < h; j++) {
                        // Находим позиции пикселя матрицы относительно изображения по y.
                        int pixelPosY = y + (j - hh);

                        int yOff;
                        if (0 <= pixelPosY && pixelPosY < height) {
                            yOff = pixelPosY * width;
                        } else if (this.edgeAction == ConvolveFilter.EXTEND_EDGES) {
                            yOff = y * width;
                        } else if (this.edgeAction == ConvolveFilter.WRAP_EDGES) {
                            yOff = ((pixelPosY + height) % height) * width;
                        } else {
                            continue;
                        }

                        for (int i = 0; i < w; i++) {
                            float k = this.kernel.matrix[i + j * w];

                            // Если значение матрицы равно нулю, то проходим дальше.
                            if (k == 0.0f) continue;

                            // Находим позиции пикселя матрицы относительно изображения по x.
                            int pixelPosX = x + (i - hw);

                            int xOff;
                            if (0 <= pixelPosX && pixelPosX < width) {
                                xOff = pixelPosX;
                            } else if (this.edgeAction == ConvolveFilter.EXTEND_EDGES) {
                                xOff = x;
                            } else if (this.edgeAction == ConvolveFilter.WRAP_EDGES) {
                                xOff = (x + width) % width;
                            } else {
                                continue;
                            }

                            // Находим значение RGBA цвета в массиве.
                            int rgba = in[xOff + yOff];

                            if (achannel) aa[index] = 0xff & (rgba >> 24);
                            if (rchannel) ar[index] = 0xff & (rgba >> 16);
                            if (gchannel) ag[index] = 0xff & (rgba >> 8);
                            if (bchannel) ab[index] = 0xff & rgba;

                            index++;
                        }
                    }

                    // Контролируем переполнения переменных и записываем значения
                    // в результирующее изображение.
                    index = x + y * width;
                    int a = achannel ? MoreMath.max(aa) : 0xff & (in[index] >> 24);
                    int r = rchannel ? MoreMath.max(ar) : 0xff & (in[index] >> 16);
                    int g = gchannel ? MoreMath.max(ag) : 0xff & (in[index] >> 8);
                    int b = bchannel ? MoreMath.max(ab) : 0xff & in[index];

                    out[index] = (a << 24) | (r << 16) | (g << 8) | b;
                }
            }
        }
    }

}
