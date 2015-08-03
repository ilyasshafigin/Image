/*
 *
 */
package ru.ildev.image.filter;

import ru.ildev.color.Color;
import ru.ildev.math.MoreMath;

/**
 * <p> Класс фильтра изображения. Фильтр применяется методом свёртки. Возьмем некоторую квадратную матрицу фильтра (ядро
 * или <code>kernel</code>) с нечетным числом элементов и наложим, например, на красный канал так, чтобы центр матрицы
 * совпал с обрабатываемым пикселем. </p> <p> <p> Вычислим сумму произведений элементов матрицы на соответствующие
 * значения в канале, разделим ее на <code>divisor</code> и прибавим <code>offset</code>. Полученное число и является
 * новым значением красной компоненты обрабатываемого пикселя. </p> <p> <p> Алгоритм таким образом проходит по каждому
 * каналу каждого пикселя изображения. Если края матрицы выходитят за пределы изображения, то они либо не
 * обрабатываются, либо дополняются неким цветом или же изображение временно расширяется. В данном алгоритме
 * "несуществующий" пиксель изображения дополняется пикселем из изображения ({@link #expansion expansion}). </p>
 * <p>
 * <pre>
 * Смягчение, известные также как smooth, soften или blur
 * 0 0 0 0 0   1 1 1 1 1   0  1  2  1  0
 * 0 1 3 1 0   1 1 1 1 1   1  3 10  3  1
 * 0 3 9 3 0   1 1 1 1 1   2 10 90 10  2
 * 0 1 3 1 0   1 1 1 1 1   1  3 10  3  1
 * 0 0 0 0 0   1 1 1 1 1   0  1  2  1  0
 *
 * Размытие по Гауссу
 * 1 2 1   1
 * 2 4 2 = 2  * 1 2 1
 * 1 2 1   1
 *
 * Заострение, увеличение резкости (sharpen)
 * 0  0  0  0  0    -1 -1 -1 -1 -1   -1 -1 -1 -1 -1   -1 -1 -1   1 -2  1
 * 0 -1 -3 -1  0    -1 -1 -1 -1 -1   -1  3  4  3 -1   -1  9 -1  -2  5 -2
 * 0 -3 41 -3  0    -1 -1 49 -1 -1   -1  4 13  4 -1   -1 -1 -1   1 -2  1
 * 0 -1 -3 -1  0    -1 -1 -1 -1 -1   -1  3  4  3 -1
 * 0  0  0  0  0    -1 -1 -1 -1 -1   -1 -1 -1 -1 -1
 *
 * Выделение краев (edge detection)
 * -1/8 -1/8 -1/8     -1 -1 -1   -5 0 0    0 0 0   0 -1  0
 * -1/8   1  -1/8      0  0  0    0 0 0   -1 1 0  -1  5 -1
 * -1/8 -1/8 -1/8      1  1  1    0 0 5    0 0 0   0 -1  0
 *
 * Рельефность, тиснение (emboss)
 * Для двух последних матриц нужно добавлять константу 128, т.е offset = 128.
 * -2 -1  0   -2 0 0    0  0  0     2  0  0
 * -1  1  1    0 1 0    0  1  0     0 -1  0
 *  0  1  2    0 0 2    0  0 -1     0  0 -1
 *
 * Диагональное разбиение (diagonal shatter)
 * 1 0 0 0 1
 * 0 0 0 0 0
 * 0 0 0 0 0
 * 0 0 0 0 0
 * 1 0 0 0 1
 *
 * Горизонтальное размытие (horizontal motion blur)
 * 0 0 0 0 0
 * 0 0 0 0 0
 * 2 3 4 5 6
 * 0 0 0 0 0
 * 0 0 0 0 0
 *
 * Инвертирование цветов
 * 0  0  0
 * 0 -1  0
 * 0  0  0
 * offset = 256
 * </pre>
 *
 * @author Ilyas74
 * @version 1.8.6
 */
public class ConvolveFilter extends Filter {

    /**
     * Обрезание краев. Пиксели матрицы, которые выходят за границы изображения будут пропускаться.
     */
    public static final int CROP_EDGES = 0;
    /**
     * Расширение краев. Пикселям матрицы, которые выходят за границы изображения будут устанавливатся цвета ближайших
     * пикселей.
     */
    public static final int EXTEND_EDGES = 1;
    /**
     * Заворачивание краев. Пикселям матрицы, которые выходят за границы изображения будут устанавливатся цвета пикселей
     * изображения с другой стороны.
     */
    public static final int WRAP_EDGES = 2;

    /**
     * Объект ядра.
     */
    protected Kernel kernel = null;
    /**
     * Действие с краями изображения.
     */
    protected int edgeAction = EXTEND_EDGES;

    /**
     * Стандартный конструктор.
     */
    public ConvolveFilter() {
    }

    /**
     * Конструктор, устанавливающий объект ядра.
     *
     * @param kernel объект матрицы.
     */
    public ConvolveFilter(Kernel kernel) {
        this(kernel, EXTEND_EDGES);
    }

    /**
     * Конструктор, устанавливающий объект ядра и тип действия с краями изображения.
     *
     * @param kernel     объект матрицы.
     * @param edgeAction действие с краями.
     */
    public ConvolveFilter(Kernel kernel, int edgeAction) {
        if (kernel == null) throw new NullPointerException("kernel == null");
        this.kernel = kernel;
        this.edgeAction = edgeAction;
    }

    /**
     * Получает объект ядра.
     *
     * @return объект ядра.
     */
    public Kernel getKernel() {
        return this.kernel;
    }

    /**
     * Устанавливает объект ядра.
     *
     * @param kernel объект ядра.
     */
    public void setKernel(Kernel kernel) {
        if (kernel == null) throw new NullPointerException("kernel == null");
        this.kernel = kernel;
    }

    /**
     * Получает тип действия с краями изображения.
     *
     * @return тип действия с краями.
     */
    public int getEdgeAction() {
        return this.edgeAction;
    }

    /**
     * Устанавливает тип действия с краями изображения.
     *
     * @param edgeAction тип действия с краями.
     */
    public void setEdgeAction(int edgeAction) {
        this.edgeAction = edgeAction;
    }

    @Override
    public void apply(int[] in, int[] out, int width, int height) {
        this.blur(in, out, width, height, this.kernel.matrix,
                this.kernel.width, this.kernel.height, this.kernel.divisor,
                this.kernel.offset, this.edgeAction, this.isRedChannel(),
                this.isGreenChannel(), this.isBlueChannel(),
                this.isAlphaChannel(), this.isMonochrome());
    }

    /**
     * Функция для размытия изображения.
     *
     * @param in         входной массив пикселей.
     * @param out        выходной массив пикселей.
     * @param w          ширина изображения.
     * @param h          высота изображения.
     * @param m          матрица ядра.
     * @param kw         ширина матрицы.
     * @param kh         высота матрицы.
     * @param divisor    делитель матрицы.
     * @param offset     смещение цветов.
     * @param edgeAction действие с краями.
     * @param rchannel   флаг обработки красных компонентов.
     * @param gchannel   флаг обработки зеленых компонентов.
     * @param bchannel   флаг обработки синих компонентов.
     * @param achannel   флаг обработки альфа компонентов.
     * @param monochrome флаг монохромной обработки.
     */
    protected void blur(int[] in, int[] out, int w, int h, float[] m, int kw,
                        int kh, float divisor, int offset, int edgeAction,
                        boolean rchannel, boolean gchannel, boolean bchannel,
                        boolean achannel, boolean monochrome) {
        int hw = kw / 2;
        int hh = kh / 2;
        float invDiv = divisor <= 0.0f ? 0.0f : 1.0f / divisor;

        if (monochrome) {
            // Проходим по каждому пикселю изображения.
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    float color = 0.0f;

                    // Проходим по каждому пикселю матрицы.
                    for (int j = 0; j < kh; j++) {
                        // Находим позиции пикселя матрицы относительно
                        // изображения по y.
                        int pixelPosY = y + j - hh;

                        int yOff;
                        if (0 <= pixelPosY && pixelPosY < h) {
                            yOff = pixelPosY * w;
                        } else if (edgeAction == EXTEND_EDGES) {
                            yOff = y * w;
                        } else if (edgeAction == WRAP_EDGES) {
                            yOff = (pixelPosY + h) % h * w;
                        } else /*
                                * if(this.edgeAction ==
                                * ConvolveFilter.CROP_EDGES)
                                */ {
                            continue;
                        }

                        for (int i = 0; i < kw; i++) {
                            float k = m[i + j * kw];
                            // Если значение матрицы равно нулю, то проходим
                            // дальше.
                            if (k == 0.0f) continue;
                            // Находим позиции пикселя матрицы относительно
                            // изображения по x.
                            int pixelPosX = x + i - hw;

                            int xOff;
                            if (0 <= pixelPosX && pixelPosX < w) {
                                xOff = pixelPosX;
                            } else if (edgeAction == EXTEND_EDGES) {
                                xOff = x;
                            } else if (edgeAction == WRAP_EDGES) {
                                xOff = (pixelPosX + w) % w;
                            } else /*
                                    * if(this.edgeAction ==
                                    * ConvolveFilter.CROP_EDGES)
                                    */ {
                                continue;
                            }

                            // Находим значение RGBA цвета в массиве.
                            // Cуммируем найденые цвета к результатирующему
                            // цвету.
                            color += in[xOff + yOff] * k;
                        }
                    }

                    // Контролируем переполнения переменных.
                    // Записываем значения в результирующее изображение.
                    out[x + y * w] = MoreMath.round(MoreMath.clamp(color * invDiv + offset, 0.0f, 255.0f));
                }
            }
        } else {
            // Проходим по каждому пикселю изображения.
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    float rs = 0.0f, gs = 0.0f, bs = 0.0f, as = 0.0f;

                    // Проходим по каждому пикселю матрицы.
                    for (int j = 0; j < kh; j++) {
                        // Находим позиции пикселя матрицы относительно
                        // изображения по y.
                        int pixelPosY = y + j - hh;

                        int yOff;
                        if (0 <= pixelPosY && pixelPosY < h) {
                            yOff = pixelPosY * w;
                        } else if (edgeAction == EXTEND_EDGES) {
                            yOff = y * w;
                        } else if (edgeAction == WRAP_EDGES) {
                            yOff = (pixelPosY + h) % h * w;
                        } else /* if(this.edgeAction == ConvolveFilter.CROP_EDGES) */ {
                            continue;
                        }

                        for (int i = 0; i < kw; i++) {
                            float k = this.kernel.matrix[i + j * kw];
                            // Если значение матрицы равно нулю, то проходим
                            // дальше.
                            if (k == 0.0f) continue;
                            // Находим позиции пикселя матрицы относительно
                            // изображения по x.
                            int pixelPosX = x + i - hw;

                            int xOff;
                            if (0 <= pixelPosX && pixelPosX < w) {
                                xOff = pixelPosX;
                            } else if (edgeAction == EXTEND_EDGES) {
                                xOff = x;
                            } else if (edgeAction == WRAP_EDGES) {
                                xOff = (pixelPosX + w) % w;
                            } else /* if(this.edgeAction == ConvolveFilter.CROP_EDGES) */ {
                                continue;
                            }

                            // Находим значение RGBA цвета в массиве.
                            int rgba = in[xOff + yOff];
                            // Cуммируем найденые цвета к результатирующему
                            // цвету.
                            if (achannel) as += (0xff & rgba >> 24) * k;
                            if (rchannel) rs += (0xff & rgba >> 16) * k;
                            if (gchannel) gs += (0xff & rgba >> 8) * k;
                            if (bchannel) bs += (0xff & rgba) * k;
                        }
                    }

                    int index = x + y * w;
                    // Контролируем переполнения переменных.
                    int a = achannel ? Color.clamp(MoreMath.round(as * invDiv + offset)) : 0xff & in[index] >> 24;
                    int r = rchannel ? Color.clamp(MoreMath.round(rs * invDiv + offset)) : 0xff & in[index] >> 16;
                    int g = gchannel ? Color.clamp(MoreMath.round(gs * invDiv + offset)) : 0xff & in[index] >> 8;
                    int b = bchannel ? Color.clamp(MoreMath.round(bs * invDiv + offset)) : 0xff & in[index];

                    // Записываем значения в результирующее изображение.
                    out[index] = a << 24 | r << 16 | g << 8 | b;
                }
            }
        }
    }

}
