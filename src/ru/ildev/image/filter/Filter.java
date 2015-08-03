/*
 *
 */
package ru.ildev.image.filter;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

/**
 * Абстрактный класс фильтра изображения.
 *
 * @author Ilyas74
 * @version 2.6.6
 */
public abstract class Filter {

    /**
     * Флаг обработки красных каналов цветов.
     */
    public static final int RED_CHANNEL = 1 << 0;
    /**
     * Флаг обработки зеленых каналов цветов.
     */
    public static final int GREEN_CHANNEL = 1 << 1;
    /**
     * Флаг обработки синих каналов цветов.
     */
    public static final int BLUE_CHANNEL = 1 << 2;
    /**
     * Флаг обработки альфа каналов цветов.
     */
    public static final int ALPHA_CHANNEL = 1 << 3;
    /**
     * Флаг монохромной обработки цветов.
     */
    public static final int MONOCHROME = 1 << 4;

    /**
     * Флаги обрабатываемых каналов.
     */
    protected int channels = RED_CHANNEL | GREEN_CHANNEL | BLUE_CHANNEL | ALPHA_CHANNEL;

    /**
     * Стандартный конструктор.
     */
    public Filter() {
    }

    /**
     * Определяет, включена ли обработка красных каналов цветов.
     *
     * @return {@code true}, если включена обработка красных каналов цветов.
     */
    public boolean isRedChannel() {
        return (this.channels & RED_CHANNEL) > 0;
    }

    /**
     * Включает или отключает обработку красных каналов цветов.
     *
     * @param flag флаг.
     */
    public void setRedChannel(boolean flag) {
        if (flag) {
            this.channels |= RED_CHANNEL;
        } else {
            this.channels &= ~RED_CHANNEL;
        }
    }

    /**
     * Определяет, включена ли обработка зеленых каналов цветов.
     *
     * @return {@code true}, если включена обработка зеленых каналов цветов.
     */
    public boolean isGreenChannel() {
        return (this.channels & GREEN_CHANNEL) > 0;
    }

    /**
     * Включает или отключает обработку зеленых каналов цветов.
     *
     * @param flag флаг.
     */
    public void setGreenChannel(boolean flag) {
        if (flag) {
            this.channels |= GREEN_CHANNEL;
        } else {
            this.channels &= ~GREEN_CHANNEL;
        }
    }

    /**
     * Определяет, включена ли обработка синих каналов цветов.
     *
     * @return {@code true}, если включена обработка синих каналов цветов.
     */
    public boolean isBlueChannel() {
        return (this.channels & BLUE_CHANNEL) > 0;
    }

    /**
     * Включает или отключает обработку синих каналов цветов.
     *
     * @param flag флаг.
     */
    public void setBlueChannel(boolean flag) {
        if (flag) {
            this.channels |= BLUE_CHANNEL;
        } else {
            this.channels &= ~BLUE_CHANNEL;
        }
    }

    /**
     * Определяет, включена ли обработка альфа каналов цветов.
     *
     * @return {@code true}, если включена обработка альфа каналов цветов.
     */
    public boolean isAlphaChannel() {
        return (this.channels & ALPHA_CHANNEL) > 0;
    }

    /**
     * Включает или отключает обработку альфа каналов цветов.
     *
     * @param flag флаг.
     */
    public void setAlphaChannel(boolean flag) {
        if (flag) {
            this.channels |= ALPHA_CHANNEL;
        } else {
            this.channels &= ~ALPHA_CHANNEL;
        }
    }

    /**
     * Определяет, включена ли монохромная обработка.
     *
     * @return {@code true}, если включена ли монохромная обработка.
     */
    public boolean isMonochrome() {
        return (this.channels & MONOCHROME) > 0;
    }

    /**
     * Включает или отключает монохромную обработку.
     *
     * @param flag флаг.
     */
    public void setMonochrome(boolean flag) {
        if (flag) {
            this.channels |= MONOCHROME;
        } else {
            this.channels &= ~MONOCHROME;
        }
    }

    /**
     * Вкбчает или отключает обработку каналов.
     *
     * @param r          флаг обработки красных каналов.
     * @param g          флаг обработки зеленых каналов.
     * @param b          флаг обработки синих каналов.
     * @param a          флаг обработки альфа каналов.
     * @param monochrome флаг монохромной обработки.
     */
    public void setChannels(boolean r, boolean g, boolean b, boolean a, boolean monochrome) {
        this.setRedChannel(r);
        this.setGreenChannel(g);
        this.setBlueChannel(b);
        this.setAlphaChannel(a);
        this.setMonochrome(monochrome);
    }

    /**
     * Метод применения фильтра изображению.
     *
     * @param in     входной массив пикселей.
     * @param out    выходной массив пикселей изображения.
     * @param width  длина изображения.
     * @param height высота изображения.
     */
    public abstract void apply(int[] in, int[] out, int width, int height);

    /**
     * Применяет фильтр изображению.
     *
     * @param image изображение.
     * @return новое фильтрированное изображение.
     */
    public BufferedImage apply(BufferedImage image) {
        return this.apply(image, image);
    }

    /**
     * Применяет фильтр области изображения.
     *
     * @param src изображение.
     * @param dst конечное изображение.
     * @return {@code dest}.
     */
    public BufferedImage apply(BufferedImage src, BufferedImage dst) {
        if (src == null) throw new NullPointerException("src == null");
        return this.apply(src, dst, 0, 0, src.getWidth(), src.getHeight(), 1);
    }

    /**
     * Применяет фильтр области изображения.
     *
     * @param src        изображение.
     * @param dst        конечное изображение.
     * @param iterations количество итераций.
     * @return {@code dest}.
     */
    public BufferedImage apply(BufferedImage src, BufferedImage dst, int iterations) {
        if (src == null) throw new NullPointerException("src == null");
        return this.apply(src, dst, 0, 0, src.getWidth(), src.getHeight(), iterations);
    }

    /**
     * Применяет фильтр области изображения.
     *
     * @param src    изображение.
     * @param dst    конечное изображение.
     * @param x      x-координата области изображения.
     * @param y      y-координата области изображения.
     * @param width  ширина области изображения.
     * @param height высота области изображения.
     * @return {@code dest}.
     */
    public BufferedImage apply(BufferedImage src, BufferedImage dst, int x, int y, int width, int height) {
        return this.apply(src, dst, x, y, width, height, 1);
    }

    /**
     * Применяет фильтр изображению.
     *
     * @param src        изображение.
     * @param dst        конечное изображение.
     * @param x          x-координата области изображения.
     * @param y          y-координата области изображения.
     * @param width      ширина области изображения.
     * @param height     высота области изображения.
     * @param iterations количество итераций.
     * @return {@code dest}.
     */
    public BufferedImage apply(BufferedImage src, BufferedImage dst, int x, int y,
                               int width, int height, int iterations) {
        if (src == null) throw new NullPointerException("src == null");
        if (iterations <= 0) return dst == null ? this.createDestination(src) : dst;

        // Находим размер изображения, т.е. количество пикселей.
        int size = width * height;

        // Создаем входной двухмерный массив.
        int[] in = this.getRGB(src, x, y, width, height, new int[size]);
        // Создаем выходной двухмерный массив.
        int[] out = new int[size];

        if (iterations == 1) {
            // Применяем фильтр.
            this.apply(in, out, width, height);
        } else {
            int[] t = new int[size];
            for (int i = 0; i < iterations; i++) {
                // Применяем фильтр.
                this.apply(in, out, width, height);

                System.arraycopy(out, 0, t, 0, size);
                System.arraycopy(in, 0, out, 0, size);
                System.arraycopy(t, 0, in, 0, size);
            }
        }

        if (dst == null) dst = this.createDestination(src, width, height);
        // Заносим выходной массив в конечное изображение.
        this.setRGB(dst, x, y, width, height, out);
        return dst;
    }

    /**
     * Получает массив пикселей области изображения.
     *
     * @param image  изображение.
     * @param x      x-координата области обработки.
     * @param y      y-координата области обработки.
     * @param width  ширина области обработки.
     * @param height высота области обработки.
     * @param pixels массив пикселей.
     * @return массив пикселей.
     */
    protected int[] getRGB(BufferedImage image, int x, int y, int width, int height, int[] pixels) {
        int type = image.getType();
        if (type == BufferedImage.TYPE_INT_ARGB || type == BufferedImage.TYPE_INT_RGB) {
            return (int[]) image.getRaster().getDataElements(x, y, width, height, pixels);
        } else {
            return image.getRGB(x, y, width, height, pixels, 0, width);
        }
    }

    /**
     * Устанавливает изображению пиксели.
     *
     * @param image  изображение.
     * @param x      x-координата области обработки.
     * @param y      y-координата области обработки.
     * @param width  ширина области обработки.
     * @param height высота области обработки.
     * @param pixels массив пикселей.
     */
    protected void setRGB(BufferedImage image, int x, int y, int width,
                          int height, int[] pixels) {
        int type = image.getType();
        if (type == BufferedImage.TYPE_INT_ARGB || type == BufferedImage.TYPE_INT_RGB) {
            image.getRaster().setDataElements(x, y, width, height, pixels);
        } else {
            image.setRGB(x, y, width, height, pixels, 0, width);
        }
    }

    /**
     * Создает конечное изображение по исходному.
     *
     * @param src исходное изображение.
     * @return конечное изображение.
     */
    protected BufferedImage createDestination(BufferedImage src) {
        ColorModel cm = src.getColorModel();
        return new BufferedImage(cm, cm.createCompatibleWritableRaster(src.getWidth(), src.getHeight()),
                cm.isAlphaPremultiplied(), null);
    }

    /**
     * Создает конечное изображение по исходному.
     *
     * @param src    исходное изображение.
     * @param width  ширина конечного изображения.
     * @param height высота конечного изображения.
     * @return конечное изображение.
     */
    protected BufferedImage createDestination(BufferedImage src, int width, int height) {
        ColorModel cm = src.getColorModel();
        return new BufferedImage(cm, cm.createCompatibleWritableRaster(width, height), cm.isAlphaPremultiplied(), null);
    }

}
