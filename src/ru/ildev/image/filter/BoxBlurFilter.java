/*
 *
 */
package ru.ildev.image.filter;

/**
 * Клас фильтра, обрабатывающий изображение однородной прямоугольной матрицей, т.е. все элементы матрицы имеют
 * одинаковое значение.
 *
 * @author Shafigin Ilyas (Шафигин Ильяс) <Ilyas74>
 * @version 0.0.0
 */
public class BoxBlurFilter extends ConvolveFilter {

    /**
     * Ширина матрицы.
     */
    protected int width;
    /**
     * Высота матрицы.
     */
    protected int height;

    /**
     * Стандартный конструктор.
     *
     * @param width  ширина матрицы.
     * @param height высота матрицы.
     */
    public BoxBlurFilter(int width, int height) {
        this(width, height, ConvolveFilter.EXTEND_EDGES);
    }

    /**
     * Конструктор.
     *
     * @param width      ширина матрицы.
     * @param height     высота матрицы.
     * @param edgeAction действие с краями.
     */
    public BoxBlurFilter(int width, int height, int edgeAction) {
        int size = width * height;
        float[] matrix = new float[size];
        float k = 1.0f / size;
        for (int i = 0; i < size; i++) matrix[i] = k;

        this.kernel = new Kernel(width, height, matrix, 1.0f, 0);
        this.edgeAction = edgeAction;
    }

}
