/*
 *
 */
package ru.ildev.image.filter;

/**
 * Класс фильтра, умножающего цвета пикселей изображений на матрицу. Это делается вот так:
 * <pre>
 * [a1, r1, g1, b1]   [a]   [a1*a + r1*r + g1*g + b1*b]
 * [a2, r2, g2, b2] * [r] = [a2*a + r2*r + g2*g + b2*b]
 * [a3, r3, g3, b3]   [g]   [a3*a + r3*r + g3*g + b3*b]
 * [a4, r4, g4, b4]   [b]   [a4*a + r4*r + g4*g + b4*b]
 * </pre>
 *
 * @author Shafigin Ilyas (Шафигин Ильяс) <Ilyas74>
 * @version 0.0.0
 */
public class BandFilter extends Filter {

    /**
     * Мартица. Она омеет вид:
     * <pre>
     * [
     *  a1, r1, g1, b1,
     *  a2, r2, g2, b2,
     *  a3, r3, g3, b3,
     *  a4, r4, g4, b4
     * ]
     * </pre>
     */
    private float[] matrix = null;

    /**
     * Стандартный констуктор.
     *
     * @param matrix матрица, размер равен 16.
     */
    public BandFilter(float[] matrix) {
        if (matrix == null) throw new NullPointerException("matrix == null");
        if (matrix.length != 16)
            throw new IllegalArgumentException("Illegal array size");
        this.matrix = matrix;
    }

    /**
     * Получает матрицу.
     *
     * @return матрицу, размер равен 16.
     */
    public float[] getMatrix() {
        return this.matrix;
    }

    /**
     * Устанавливает матрицу.
     *
     * @param matrix матрица, размер равен 16.
     */
    public void setMatrix(float[] matrix) {
        if (matrix == null) throw new NullPointerException("matrix == null");
        if (matrix.length != 16)
            throw new IllegalArgumentException("Illegal array size");
        this.matrix = matrix;
    }

    @Override
    public void apply(int[] in, int[] out, int width, int height) {
        boolean rchannel = this.isRedChannel();
        boolean gchannel = this.isGreenChannel();
        boolean bchannel = this.isBlueChannel();
        boolean achannel = this.isAlphaChannel();
        boolean monochrome = this.isMonochrome();

        int size = width * height;
        for (int i = 0; i < size; i++) {
            if (monochrome) {
                out[i] = (int) (this.matrix[12] * in[i] + this.matrix[13] * in[i] + this.matrix[14] * in[i] +
                        this.matrix[15] * in[i]);
            } else {
                int a = 0xff & (in[i] >> 24);
                int r = 0xff & (in[i] >> 16);
                int g = 0xff & (in[i] >> 8);
                int b = 0xff & in[i];

                int ra = achannel ?
                        (int) (this.matrix[0] * a + this.matrix[1] * r + this.matrix[2] * g + this.matrix[3] * b) : a;
                int rr = rchannel ?
                        (int) (this.matrix[4] * a + this.matrix[5] * r + this.matrix[6] * g + this.matrix[7] * b) : r;
                int rg = gchannel ?
                        (int) (this.matrix[8] * a + this.matrix[9] * r + this.matrix[10] * g + this.matrix[11] * b) : g;
                int rb = bchannel ?
                        (int) (this.matrix[12] * a + this.matrix[13] * r + this.matrix[14] * g + this.matrix[15] * b) :
                        b;

                out[i] = (ra << 24) | (rr << 16) | (rg << 8) | rb;
            }
        }
    }

}
