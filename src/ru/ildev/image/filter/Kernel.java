/*
 *
 */
package ru.ildev.image.filter;

/**
 * Класс ядра свертки фильтра изображения.
 *
 * @author Shafigin Ilyas (Шафигин Ильяс) <Ilyas74>
 * @version 0.0.0
 */
public class Kernel {

    /**
     * Ширина матрицы.
     */
    protected int width;
    /**
     * Высота матрицы.
     */
    protected int height;
    /**
     * Ядро или матрица свертки, она обычно выглядит так:<pre>
     * [
     *   x1 x2 x3  +
     *   x4 x4 x5  | высота
     *   x6 x7 xn  +
     *   +--------+
     *     ширина
     * ].</pre>
     * <p>
     * Для корректного отображения изображения сумма всех x деленная на divisor должна быть равна 1.
     */
    protected float[] matrix = new float[0];
    /**
     * Делитель результата искривления, используется для нормализации. Обычно должен быть равен сумме всех чисел
     * матрицы, чтобы сумма всех элементов нормализированной матрицы была равна 1.
     */
    protected float divisor = 1.0f;
    /**
     * Смещение цвета. К каждому результатирующему цвета прибавляется это значение.
     */
    protected int offset = 0;

    /**
     * Конструктор, устанавливающий матрицу и ее размеры.
     *
     * @param width  ширина матрицы.
     * @param height высота матрицы.
     * @param matrix матрица.
     */
    public Kernel(int width, int height, float[] matrix) {
        this(width, height, matrix, 1.0f, 0);
    }

    /**
     * Конструктор, устанавливающий матрицу, ее размеры и делитель.
     *
     * @param width   ширина матрицы.
     * @param height  высота матрицы.
     * @param matrix  матрица.
     * @param divisor делитель.
     */
    public Kernel(int width, int height, float[] matrix, float divisor) {
        this(width, height, matrix, divisor, 0);
    }

    /**
     * Конструктор, устанавливающий матрицу, ее размеры, делитель и смещение цветов.
     *
     * @param width   ширина матрицы.
     * @param height  высота матрицы.
     * @param matrix  матрица.
     * @param divisor делитель.
     * @param offset  смещенгие.
     */
    public Kernel(int width, int height, float[] matrix, float divisor, int offset) {
        this.width = width;
        this.height = height;
        this.divisor = divisor <= 0.0f ? 1.0f : divisor;
        this.offset = offset;

        // Находим указанный размер матрицы,
        int length = width * height;
        // если он не совпадает с действительным, то возбуждаем исключенгие.
        if (matrix.length != length) throw new IllegalStateException("matrix.length != length");
        this.matrix = new float[length];
        System.arraycopy(matrix, 0, this.matrix, 0, length);
    }

    /**
     * Получает ширину матрицы.
     *
     * @return ширину матрицы.
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Получает высоту матрицы.
     *
     * @return высоту матрицы.
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Получает матрицу.
     *
     * @return матрицу.
     */
    public float[] getMatrix() {
        return this.matrix;
    }

    /**
     * Получает делитель матрицы.
     *
     * @return делитель матрицы.
     */
    public float getDivisor() {
        return this.divisor;
    }

    /**
     * Получает смещение цветов матрицы.
     *
     * @return смещение цветов матрицы.
     */
    public int getOffset() {
        return this.offset;
    }

}
