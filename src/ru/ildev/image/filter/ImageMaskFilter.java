/*
 *
 */
package ru.ildev.image.filter;

import java.awt.image.BufferedImage;

/**
 * Класс фильтра, применяющего фильтрируемым изображениям маску.
 *
 * @author Shafigin Ilyas (Шафигин Ильяс) <Ilyas74>
 * @version 0.0.0
 */
public class ImageMaskFilter extends Filter {

    /**
     * Изображение маски.
     */
    private BufferedImage mask;

    /**
     * Стандартный конструктор.
     *
     * @param mask маска.
     */
    public ImageMaskFilter(BufferedImage mask) {
        if (mask == null) throw new NullPointerException("mask == null");
        this.mask = mask;
    }

    /**
     * Получает изображение маски.
     *
     * @return изображение маски.
     */
    public BufferedImage getMask() {
        return this.mask;
    }

    /**
     * Устанавливает изображение маски.
     *
     * @param mask изображение маски.
     */
    public void setMask(BufferedImage mask) {
        if (mask == null) throw new NullPointerException("mask == null");
        this.mask = mask;
    }

    @Override
    public void apply(int[] in, int[] out, int width, int height) {
        int maskWidth = this.mask.getWidth();
        int maskHeight = this.mask.getHeight();
        int[] maskPixels = this.mask.getRGB(0, 0, maskWidth, maskHeight, new int[maskWidth * maskHeight], 0, maskWidth);

        for (int y = 0; y < maskHeight; y++) {
            if(y < 0 || y >= height) continue;

            for (int x = 0; x < maskWidth; x++) {
                if (x < 0 || x >= width ) continue;

                int index = x + y * width;
                out[index] = in[index] & 0xff000000 & (maskPixels[index] & 0xff) << 24 | in[index] & 0x00ffffff;
            }
        }
    }

}
