/*
 *
 */
package ru.ildev.image.filter;

import ru.ildev.math.MoreMath;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс фильтра размытия по Гауссу.
 *
 * @author Shafigin Ilyas (Шафигин Ильяс) <Ilyas74>
 * @version 0.2.0
 */
public class GaussianBlurFilter extends ConvolveFilter {

    /**
     * Радиус размытия.
     */
    protected int radius;

    /**
     * Стандартный конструктор.
     *
     * @param radius радиус размытия.
     */
    public GaussianBlurFilter(int radius) {
        this(radius, ConvolveFilter.EXTEND_EDGES);
    }

    /**
     * Конструктор.
     *
     * @param radius     радиус размытия.
     * @param edgeAction действие с краями.
     */
    public GaussianBlurFilter(int radius, int edgeAction) {
        this.radius = radius;
        this.kernel = makeKernel(radius);
        this.edgeAction = edgeAction;
    }

    /**
     * Получает радиус размытия.
     *
     * @return радиус размытия.
     */
    public int getRadius() {
        return this.radius;
    }

    /**
     * Устанавливает радиус размытия.
     *
     * @param radius радиус размытия.
     */
    public void setRadius(int radius) {
        assert radius > 0 : "radius <= 0";
        this.radius = radius;
        this.kernel = makeKernel(radius);
    }
/*
    @Override
    public void apply(int[] in, int[] out, int width, int height) {
        int[] tmp = new int[width * height];

        int kw = this.kernel.width;
        int kh = this.kernel.height;

        float[] kernelH = new float[kw];
        float[] kernelV = new float[kh];

        for(int x = 0; x < kw; x++) {
            kernelH[x] = this.kernel.matrix[x + kh/2*kw];
            for(int y = 0; y < kh; y++) {
                kernelH[x] += this.kernel.matrix[x + y * kw];
            }
        }

        for(int y = 0; y < kh; y++) {
            kernelV[y] = this.kernel.matrix[kw/2 + y * kw];
            for(int x = 0; x < kw; x++) {
                kernelV[y] += this.kernel.matrix[x + y * kw];
            }
        }

        this.blur(in, tmp, width, height, kernelV, 1, kh, 1.0f, 0, this.edgeAction, this.isRedChannel(), this.isGreenChannel(), this.isBlueChannel(), this.isAlphaChannel(), this.isMonochrome());
        this.blur(tmp, out, width, height, kernelH, kw, 1, 1.0f, 0, this.edgeAction, this.isRedChannel(), this.isGreenChannel(), this.isBlueChannel(), this.isAlphaChannel(), this.isMonochrome());
    }
*/

    /**
     * Таблица ядер.
     */
    private static final Map<Integer, Kernel> KERNELS = new HashMap<>();

    /**
     * Создает ядро фильтра размытия по Гауссу.
     *
     * @param radius радиус размытия.
     * @return ядро фильтра.
     */
    public static Kernel makeKernel(int radius) {
        assert radius > 0 : "radius <= 0";

        if (KERNELS.containsKey(radius)) {
            return KERNELS.get(radius);
        } else {
            float sigma = radius / 3.0f;
            float sigma22 = 2.0f * sigma * sigma;
            float sigmaRoot = 1.0f / MoreMath.sqrt(sigma22 * MoreMath.PI);

            float div = 0.0f;
            int r2 = radius * 2;
            int length = r2 * r2;
            float[] matrix = new float[length];

            for (int x = 0; x < r2; x++) {
                for (int y = 0; y < r2; y++) {
                    int u = x - radius;
                    int v = y - radius;

                    //              1         ( -u^2+v^2    )
                    // Gauss = ------------*e^(-------------)
                    //         2*PI*sigma^2   (2*sigma*sigma)
                    float k = MoreMath.exp(-(u * u + v * v) / sigma22) * sigmaRoot;
                    matrix[y * r2 + x] = k;
                    div += k;
                }
            }

            div = div <= 0.0f ? 1.0f : 1.0f / div;
            for (int i = 0; i < length; i++) {
                matrix[i] *= div;
            }

            Kernel kernel = new Kernel(r2, r2, matrix, 1.0f, 0);
            KERNELS.put(radius, kernel);
            return kernel;
        }
    }

}
