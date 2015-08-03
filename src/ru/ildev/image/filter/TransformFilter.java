/*
 *
 */
package ru.ildev.image.filter;

import ru.ildev.geom.Transform2;
import ru.ildev.math.MoreMath;

import java.awt.*;

/**
 * Класс фильтра изображений, который трансформирует изображение объектом трансформации.
 *
 * @author Shafigin Ilyas (Шафигин Ильяс) <Ilyas74>
 * @version 0.2.2
 */
public class TransformFilter extends Filter {

    /**
     * Обрезание краев. Пиксели, которые выходят за границы изображения будут пропускаться.
     */
    public static final int CROP_EDGES = 0;
    /**
     * Расширение краев. Пикселям, которые выходят за границы изображения будут устанавливатся цвета ближайших
     * пикселей.
     */
    public static final int EXTEND_EDGES = 1;
    /**
     * Заворачивание краев. Пикселям, которые выходят за границы изображения будут устанавливатся цвета пикселей
     * изображения с другой стороны.
     */
    public static final int WRAP_EDGES = 2;

    /**
     * Интерполяция округления (округление до ближайшего соседа).
     */
    public static final int NEAREST_NEIGHBOUR_INTERPOLATION = 0;
    /**
     * Билинейная интерполяция.
     */
    public static final int BILINEAR_INTERPOLATION = 1;
    /**
     * Бикубическая интерполяция.
     */
    public static final int BICUBIC_INTERPOLATION = 2;

    /**
     * Объект трансформации.
     */
    protected Transform2 transform = new Transform2();
    /**
     * Ограничивающий прямоугольник обрабатываемой области изображения.
     */
    protected Rectangle bound = new Rectangle();
    /**
     * Действие с краями изображения.
     */
    protected int edgeAction = EXTEND_EDGES;
    /**
     * Тип интерполяции изображений.
     */
    protected int interpolation = BILINEAR_INTERPOLATION;

    /**
     * Стандартный конструктор.
     */
    public TransformFilter() {
    }

    /**
     * Конструктор, устанавливающий объект трансформации.
     *
     * @param transform объект трансформации.
     */
    public TransformFilter(Transform2 transform) {
        if (transform == null) throw new NullPointerException("transform == null");
        this.transform = transform;
    }

    /**
     * Конструктор, устанавливающий объект трансформации.
     *
     * @param transform  объект трансформации.
     * @param edgeAction действие с краями.
     */
    public TransformFilter(Transform2 transform, int edgeAction) {
        if (transform == null) throw new NullPointerException("transform == null");
        this.transform = transform;
        this.edgeAction = edgeAction;
    }

    /**
     * Конструктор, устанавливающий объект трансформации.
     *
     * @param transform     объект трансформации.
     * @param edgeAction    действие с краями.
     * @param interpolation тип интерполяции изображений.
     */
    public TransformFilter(Transform2 transform, int edgeAction,
                           int interpolation) {
        if (transform == null) throw new NullPointerException("transform == null");
        this.transform = transform;
        this.edgeAction = edgeAction;
        this.interpolation = interpolation;
    }

    private static int bilinearInterpolate(float x, float y, int p11, int p12, int p21, int p22) {
        float z1, z2;

        float cx = 1.0f - x;
        float cy = 1.0f - y;

        z1 = cx * ((p11 >> 24) & 0xff) + x * ((p12 >> 24) & 0xff);
        z2 = cx * ((p21 >> 24) & 0xff) + x * ((p12 >> 24) & 0xff);
        int a = (int) (cy * z1 + y * z2);

        z1 = cx * ((p11 >> 16) & 0xff) + x * ((p12 >> 16) & 0xff);
        z2 = cx * ((p21 >> 16) & 0xff) + x * ((p22 >> 16) & 0xff);
        int r = (int) (cy * z1 + y * z2);

        z1 = cx * ((p11 >> 8) & 0xff) + x * ((p12 >> 8) & 0xff);
        z2 = cx * ((p21 >> 8) & 0xff) + x * ((p22 >> 8) & 0xff);
        int g = (int) (cy * z1 + y * z2);

        z1 = cx * (p11 & 0xff) + x * (p12 & 0xff);
        z2 = cx * (p21 & 0xff) + x * (p22 & 0xff);
        int b = (int) (cy * z1 + y * z2);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    private static int bicubicInterpolate(float x, float y, int p11, int p12, int p13, int p14, int p21, int p22,
                                          int p23, int p24, int p31, int p32, int p33, int p34, int p41, int p42,
                                          int p43, int p44) {
        float z1, z2, z3, z4;

        // Интерполяция Кея.
        float w1x = (-0.5f + (1.0f - 0.5f * x) * x) * x;
        float w2x = 1.0f + (-2.5f + 1.5f * x) * x * x;
        float w3x = (0.5f + (2.0f - 1.5f * x) * x) * x;
        float w4x = (-0.5f + 0.5f * x) * x * x;

        float w1y = (-0.5f + (1.0f - 0.5f * y) * y) * y;
        float w2y = 1.0f + (-2.5f + 1.5f * y) * y * y;
        float w3y = (0.5f + (2.0f - 1.5f * y) * y) * y;
        float w4y = (-0.5f + 0.5f * y) * y * y;

        // Интерполяция Шаума.
        /*
        float w1x = -1.0 / 6.0 * x * (x - 1.0) * (x - 2.0);
        float w2x = 0.5 * (x * x - 1.0) * (x - 2.0);
        float w3x = -0.5 * x * (x + 1.0) * (x - 2.0);
        float w4x = 1.0 / 6.0 * x * (x * x - 1.0);
        
        float w1y = -1.0 / 6.0 * y * (y - 1.0) * (y - 2.0);
        float w2y = 0.5 * (y * y - 1.0) * (y - 2.0);
        float w3y = -0.5 * y * (y + 1.0) * (y - 2.0);
        float w4y = 1.0 / 6.0 * y * (y * y - 1.0);
        */
        // Интерполяция O-MOMS
        /*
        float w1x = 4.0 / 21.0 + (-11.0 / 21.0 + (0.5 - 1.0 / 6.0 * x) * x) * x;
        float w2x = 13.0 / 21.0 + (1.0 / 14.0 + (-1.0 + 0.5 * x) * x) * x;
        float w3x = 4.0 / 21.0 + (3.0 / 7.0 + (0.5 - 0.5 * x) * x) * x;
        float w4x = (1.0 / 41.0 + 1.0 / 6.0 * x * x) * x;
        
        float w1y = 4.0 / 21.0 + (-11.0 / 21.0 + (0.5 - 1.0 / 6.0 * y) * y) * y;
        float w2y = 13.0 / 21.0 + (1.0 / 14.0 + (-1.0 + 0.5 * y) * y) * y;
        float w3y = 4.0  / 21.0 + (3.0 / 7.0 + (0.5 - 0.5 * y) * y) * y;
        float w4y = (1.0 / 41.0 + 1.0 / 6.0 * y * y) * y;
        */
        z1 = w1x * ((p11 >> 24) & 0xff) + w2x * ((p12 >> 24) & 0xff) + w3x * ((p13 >> 24) & 0xff) +
                w4x * ((p14 >> 24) & 0xff);
        z2 = w1x * ((p21 >> 24) & 0xff) + w2x * ((p22 >> 24) & 0xff) + w3x * ((p23 >> 24) & 0xff) +
                w4x * ((p24 >> 24) & 0xff);
        z3 = w1x * ((p31 >> 24) & 0xff) + w2x * ((p32 >> 24) & 0xff) + w3x * ((p33 >> 24) & 0xff) +
                w4x * ((p34 >> 24) & 0xff);
        z4 = w1x * ((p41 >> 24) & 0xff) + w2x * ((p42 >> 24) & 0xff) + w3x * ((p43 >> 24) & 0xff) +
                w4x * ((p44 >> 24) & 0xff);
        int a = (int) (w1y * z1 + w2y * z2 + w3y * z3 + w4y * z4);
        a = a < 0 ? 0 : (a > 255 ? 255 : a);

        z1 = w1x * ((p11 >> 16) & 0xff) + w2x * ((p12 >> 16) & 0xff) + w3x * ((p13 >> 16) & 0xff) +
                w4x * ((p14 >> 16) & 0xff);
        z2 = w1x * ((p21 >> 16) & 0xff) + w2x * ((p22 >> 16) & 0xff) + w3x * ((p23 >> 16) & 0xff) +
                w4x * ((p24 >> 16) & 0xff);
        z3 = w1x * ((p31 >> 16) & 0xff) + w2x * ((p32 >> 16) & 0xff) + w3x * ((p33 >> 16) & 0xff) +
                w4x * ((p34 >> 16) & 0xff);
        z4 = w1x * ((p41 >> 16) & 0xff) + w2x * ((p42 >> 16) & 0xff) + w3x * ((p43 >> 16) & 0xff) +
                w4x * ((p44 >> 16) & 0xff);
        int r = (int) (w1y * z1 + w2y * z2 + w3y * z3 + w4y * z4);
        r = r < 0 ? 0 : (r > 255 ? 255 : r);

        z1 = w1x * ((p11 >> 8) & 0xff) + w2x * ((p12 >> 8) & 0xff) + w3x * ((p13 >> 8) & 0xff) +
                w4x * ((p14 >> 8) & 0xff);
        z2 = w1x * ((p21 >> 8) & 0xff) + w2x * ((p22 >> 8) & 0xff) + w3x * ((p23 >> 8) & 0xff) +
                w4x * ((p24 >> 8) & 0xff);
        z3 = w1x * ((p31 >> 8) & 0xff) + w2x * ((p32 >> 8) & 0xff) + w3x * ((p33 >> 8) & 0xff) +
                w4x * ((p34 >> 8) & 0xff);
        z4 = w1x * ((p41 >> 8) & 0xff) + w2x * ((p42 >> 8) & 0xff) + w3x * ((p43 >> 8) & 0xff) +
                w4x * ((p44 >> 8) & 0xff);
        int g = (int) (w1y * z1 + w2y * z2 + w3y * z3 + w4y * z4);
        g = g < 0 ? 0 : (g > 255 ? 255 : g);

        z1 = w1x * (p11 & 0xff) + w2x * (p12 & 0xff) + w3x * (p13 & 0xff) + w4x * (p14 & 0xff);
        z2 = w1x * (p21 & 0xff) + w2x * (p22 & 0xff) + w3x * (p23 & 0xff) + w4x * (p24 & 0xff);
        z3 = w1x * (p31 & 0xff) + w2x * (p32 & 0xff) + w3x * (p33 & 0xff) + w4x * (p34 & 0xff);
        z4 = w1x * (p41 & 0xff) + w2x * (p42 & 0xff) + w3x * (p43 & 0xff) + w4x * (p44 & 0xff);
        int b = (int) (w1y * z1 + w2y * z2 + w3y * z3 + w4y * z4);
        b = b < 0 ? 0 : (b > 255 ? 255 : b);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    /**
     * Получает объект трансформации.
     *
     * @return объект трансформации.
     */
    public Transform2 getTransform() {
        return this.transform;
    }

    /**
     * Устанавливает объект трансформации.
     *
     * @param transform объект трансформации.
     */
    public void setTransform(Transform2 transform) {
        if (transform == null) throw new NullPointerException("transform == null");
        this.transform = transform;
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

    /**
     * Получает тип интерполяции изображений.
     *
     * @return тип интерполяции изображений.
     */
    public int getInterpolation() {
        return this.interpolation;
    }

    /**
     * Устанавливает тип интерполяции изображений.
     *
     * @param interpolation тип интерполяции изображений.
     */
    public void setInterpolation(int interpolation) {
        this.interpolation = interpolation;
    }

    @Override
    public void apply(int[] in, int[] out, int width, int height) {
        this.bound.setRect(0, 0, width, height);
        this.transformBound();

        switch (this.interpolation) {
            default:
            case TransformFilter.NEAREST_NEIGHBOUR_INTERPOLATION:
                this.nearest_neighbour(in, out, width, height);
                break;
            case TransformFilter.BILINEAR_INTERPOLATION:
                this.bilinear(in, out, width, height);
                break;
            case TransformFilter.BICUBIC_INTERPOLATION:
                this.bicubic(in, out, width, height);
                break;
        }
    }

    private void nearest_neighbour(int[] in, int[] out, int width, int height) {
        boolean rchannel = this.isRedChannel();
        boolean gchannel = this.isGreenChannel();
        boolean bchannel = this.isBlueChannel();
        boolean achannel = this.isAlphaChannel();
        boolean monochrome = this.isMonochrome();

        int srcX;
        int srcY;
        int srcWidth = width;
        int srcHeight = height;

        int outX = this.bound.x;
        int outY = this.bound.y;
        int outWidth = this.bound.width;
        int outHeight = this.bound.height;

        // Создаем временный массив.
        float[] tmp = new float[2];

        // Проходим по всем пикселям изображения.
        for (int y = 0; y < outHeight; y++) {
            int oy = outY + y;
            if(oy < 0 || oy >= srcHeight) continue;

            for (int x = 0; x < outWidth; x++) {
                int ox = outX + x;
                if (ox < 0 || ox >= srcWidth) continue;

                // Трансформируем пиксель.
                this.transformPixel(ox, oy, tmp);
                srcX = (int) tmp[0];
                srcY = (int) tmp[1];

                int xOff, yOff;
                // Находим индекс пикселя.
                int index = ox + oy * srcWidth;

                // Если он выходит за граници изображения.
                if (0 <= srcX && srcX < srcWidth) {
                    xOff = srcX;
                } else if (this.edgeAction == ConvolveFilter.EXTEND_EDGES) {
                    xOff = MoreMath.clamp(srcX, 0, srcWidth - 1);
                } else if (this.edgeAction == ConvolveFilter.WRAP_EDGES) {
                    xOff = (srcX + srcWidth) % srcWidth;
                } else /*if(this.edgeAction == ConvolveFilter.CROP_EDGES)*/ {
                    out[index] = 0;
                    continue;
                }

                if (0 <= srcY && srcY < srcHeight) {
                    yOff = srcY * srcWidth;
                } else if (this.edgeAction == ConvolveFilter.EXTEND_EDGES) {
                    yOff = MoreMath.clamp(srcY, 0, srcHeight - 1) * srcWidth;
                } else if (this.edgeAction == ConvolveFilter.WRAP_EDGES) {
                    yOff = (srcY + srcHeight) % srcHeight * srcWidth;
                } else /*if(this.edgeAction == ConvolveFilter.CROP_EDGES)*/ {
                    out[index] = 0;
                    continue;
                }

                int color = in[xOff + yOff];

                // Заносим цвет пикселя в конечный массив.
                if (monochrome) {
                    out[index] = color;
                } else {
                    int a = achannel ? 0xff & (color >> 24) : 0xff & (in[index] >> 24);
                    int r = rchannel ? 0xff & (color >> 16) : 0xff & (in[index] >> 16);
                    int g = gchannel ? 0xff & (color >> 8) : 0xff & (in[index] >> 8);
                    int b = bchannel ? 0xff & color : 0xff & in[index];

                    out[index] = (a << 24) | (r << 16) | (g << 8) | b;
                }
            }
        }
    }

    private void bilinear(int[] in, int[] out, int width, int height) {
        boolean rchannel = this.isRedChannel();
        boolean gchannel = this.isGreenChannel();
        boolean bchannel = this.isBlueChannel();
        boolean achannel = this.isAlphaChannel();
        boolean monochrome = this.isMonochrome();

        int srcX;
        int srcY;
        int srcWidth = width;
        int srcHeight = height;

        int srcWidthSub1 = srcWidth - 1;
        int srcHeightSub1 = srcHeight - 1;

        int outX = this.bound.x;
        int outY = this.bound.y;
        int outWidth = this.bound.width;
        int outHeight = this.bound.height;

        // Создаем временный массив.
        float[] tmp = new float[2];

        // Проходим по всем пикселям изображения.
        for (int y = 0; y < outHeight; y++) {
            int oy = outY + y;
            if(oy < 0 || oy >= srcHeight) continue;

            for (int x = 0; x < outWidth; x++) {
                int ox = outX + x;
                if (ox < 0 || ox >= srcWidth) continue;

                // Трансформируем пиксель.
                this.transformPixel(ox, oy, tmp);
                srcX = MoreMath.floor(tmp[0]);
                srcY = MoreMath.floor(tmp[1]);

                float xWeight = tmp[0] - srcX;
                float yWeight = tmp[1] - srcY;
                int p11, p12, p21, p22;

                // Если он выходит за граници изображения.
                if (0 <= srcX && srcX < srcWidthSub1 && 0 <= srcY && srcY < srcHeightSub1) {
                    int i = srcX + srcY * srcWidth;
                    p11 = in[i];
                    p12 = in[i + 1];

                    p21 = in[i + srcWidth];
                    p22 = in[i + srcWidth + 1];
                } else {
                    p11 = this.getPixel(in, srcX, srcY, srcWidth, srcHeight);
                    p12 = this.getPixel(in, srcX + 1, srcY, srcWidth, srcHeight);

                    p21 = this.getPixel(in, srcX, srcY + 1, srcWidth, srcHeight);
                    p22 = this.getPixel(in, srcX + 1, srcY + 1, srcWidth, srcHeight);
                }

                // Находим индекс пикселя.
                int index = ox + oy * srcWidth;
                int color = TransformFilter.bilinearInterpolate(xWeight, yWeight, p11, p12, p21, p22);
                // Заносим цвет пикселя в конечный массив.
                if (monochrome) {
                    out[index] = color;
                } else {
                    int a = achannel ? 0xff & (color >> 24) : 0xff & (in[index] >> 24);
                    int r = rchannel ? 0xff & (color >> 16) : 0xff & (in[index] >> 16);
                    int g = gchannel ? 0xff & (color >> 8) : 0xff & (in[index] >> 8);
                    int b = bchannel ? 0xff & color : 0xff & in[index];

                    out[index] = (a << 24) | (r << 16) | (g << 8) | b;
                }
            }
        }
    }

    private void bicubic(int[] in, int[] out, int width, int height) {
        boolean rchannel = this.isRedChannel();
        boolean gchannel = this.isGreenChannel();
        boolean bchannel = this.isBlueChannel();
        boolean achannel = this.isAlphaChannel();
        boolean monochrome = this.isMonochrome();

        int srcX;
        int srcY;
        int srcWidth = width;
        int srcHeight = height;

        int srcWidthSub2 = srcWidth - 2;
        int srcWidthMul2 = srcWidth * 2;
        int srcHeightSub2 = srcHeight - 2;

        int outX = this.bound.x;
        int outY = this.bound.y;
        int outWidth = this.bound.width;
        int outHeight = this.bound.height;

        // Создаем временный массив.
        float[] tmp = new float[2];

        // Проходим по всем пикселям изображения.
        for (int y = 0; y < outHeight; y++) {
            int oy = outY + y;
            if(oy < 0 || oy >= srcHeight) continue;

            for (int x = 0; x < outWidth; x++) {
                int ox = outX + x;
                if (ox < 0 || ox >= srcWidth) continue;

                // Трансформируем пиксель.
                this.transformPixel(ox, oy, tmp);
                srcX = MoreMath.floor(tmp[0]);
                srcY = MoreMath.floor(tmp[1]);

                float xWeight = tmp[0] - srcX;
                float yWeight = tmp[1] - srcY;
                int p11, p12, p13, p14, p21, p22, p23, p24, p31, p32, p33, p34, p41, p42, p43, p44;

                // Если он выходит за граници изображения.
                if (1 <= srcX && srcX < srcWidthSub2 && 1 <= srcY && srcY < srcHeightSub2) {
                    int i = srcX + srcY * srcWidth;
                    p11 = in[i - srcWidth - 1];
                    p12 = in[i - srcWidth];
                    p13 = in[i - srcWidth + 1];
                    p14 = in[i - srcWidth + 2];

                    p21 = in[i - 1];
                    p22 = in[i];
                    p23 = in[i + 1];
                    p24 = in[i + 2];

                    p31 = in[i + srcWidth - 1];
                    p32 = in[i + srcWidth];
                    p33 = in[i + srcWidth + 1];
                    p34 = in[i + srcWidth + 2];

                    p41 = in[i + srcWidthMul2 - 1];
                    p42 = in[i + srcWidthMul2];
                    p43 = in[i + srcWidthMul2 + 1];
                    p44 = in[i + srcWidthMul2 + 2];
                } else {
                    p11 = this.getPixel(in, srcX - 1, srcY - 1, srcWidth, srcHeight);
                    p12 = this.getPixel(in, srcX, srcY - 1, srcWidth, srcHeight);
                    p13 = this.getPixel(in, srcX + 1, srcY - 1, srcWidth, srcHeight);
                    p14 = this.getPixel(in, srcX + 2, srcY - 1, srcWidth, srcHeight);

                    p21 = this.getPixel(in, srcX - 1, srcY, srcWidth, srcHeight);
                    p22 = this.getPixel(in, srcX, srcY, srcWidth, srcHeight);
                    p23 = this.getPixel(in, srcX + 1, srcY, srcWidth, srcHeight);
                    p24 = this.getPixel(in, srcX + 2, srcY, srcWidth, srcHeight);

                    p31 = this.getPixel(in, srcX - 1, srcY + 1, srcWidth, srcHeight);
                    p32 = this.getPixel(in, srcX, srcY + 1, srcWidth, srcHeight);
                    p33 = this.getPixel(in, srcX + 1, srcY + 1, srcWidth, srcHeight);
                    p34 = this.getPixel(in, srcX + 2, srcY + 1, srcWidth, srcHeight);

                    p41 = this.getPixel(in, srcX - 1, srcY + 2, srcWidth, srcHeight);
                    p42 = this.getPixel(in, srcX, srcY + 2, srcWidth, srcHeight);
                    p43 = this.getPixel(in, srcX + 1, srcY + 2, srcWidth, srcHeight);
                    p44 = this.getPixel(in, srcX + 2, srcY + 2, srcWidth, srcHeight);
                }

                // Находим индекс пикселя.
                int index = ox + oy * srcWidth;
                int color = TransformFilter.bicubicInterpolate(xWeight, yWeight, p11, p12, p13, p14,
                        p21, p22, p23, p24, p31, p32, p33, p34, p41, p42, p43, p44);
                // Заносим цвет пикселя в конечный массив.
                if (monochrome) {
                    out[index] = color;
                } else {
                    int a = achannel ? 0xff & (color >> 24) : 0xff & (in[index] >> 24);
                    int r = rchannel ? 0xff & (color >> 16) : 0xff & (in[index] >> 16);
                    int g = gchannel ? 0xff & (color >> 8) : 0xff & (in[index] >> 8);
                    int b = bchannel ? 0xff & color : 0xff & in[index];

                    out[index] = (a << 24) | (r << 16) | (g << 8) | b;
                }
            }
        }
    }

    /**
     * Трансформирует положение пикселя.
     *
     * @param x   x-координата пикселя.
     * @param y   y-координата пикселя.
     * @param out массив значений трансформированного положения пикселя.
     */
    protected void transformPixel(int x, int y, float[] out) {
        // Заносим во временный массив координаты пикселя.
        out[0] = x;
        out[1] = y;
        // Трансформируем пиксель.
        this.transform.transform(out, 0, out, 0, 1);
    }

    /**
     * Трансформирует ограничивающий прямоугольник обрабатываемой области.
     */
    protected void transformBound() {
    }

    private int getPixel(int[] pixels, int x, int y, int width, int height) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            switch (this.edgeAction) {
                default:
                case TransformFilter.CROP_EDGES:
                    return 0;
                case TransformFilter.EXTEND_EDGES:
                    return pixels[(MoreMath.clamp(y, 0, height - 1) * width) + MoreMath.clamp(x, 0, width - 1)];
                case TransformFilter.WRAP_EDGES:
                    return pixels[(MoreMath.mod(y, height) * width) + MoreMath.mod(x, width)];
            }
        }
        return pixels[x + y * width];
    }

}
