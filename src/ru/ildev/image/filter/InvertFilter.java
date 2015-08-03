/*
 *
 */
package ru.ildev.image.filter;

/**
 * Класс фильтра, инвертирующего цвета изображений.
 *
 * @author Shafigin Ilyas (Шафигин Ильяс) <Ilyas74>
 * @version 0.0.1
 */
public class InvertFilter extends ColorFilter {

    @Override
    protected int apply(int x, int y, int color) {
        if (this.isMonochrome()) {
            return 255 - (color & 0xff);
        } else {
            int r = 0xff & (color >> 16);
            int g = 0xff & (color >> 8);
            int b = 0xff & color;

            return (color & 0xff000000) |
                    ((this.isRedChannel() ? 255 - r : r) << 16) |
                    ((this.isGreenChannel() ? 255 - g : g) << 8) |
                    (this.isBlueChannel() ? 255 - b : b);
        }
    }

}
