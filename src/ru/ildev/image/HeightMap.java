/*
 *
 */
package ru.ildev.image;

import ru.ildev.image.filter.Filter;
import ru.ildev.image.filter.GrayscaleFilter;

import java.awt.image.BufferedImage;

/**
 * Класс, генерирующий карту высот изображений.
 *
 * @author Shafigin Ilyas (Шафигин Ильяс) <Ilyas74>
 * @version 0.0.0
 */
public class HeightMap {

    /**
     * Объект фильтра градации серого.
     */
    private static final Filter GRAYSCALE_FILTER = new GrayscaleFilter();

    /**
     * Генерирует карту высот изображения.
     *
     * @param source изображение.
     * @return карту высот изображения.
     */
    public static BufferedImage generate(BufferedImage source) {
        if (source == null) throw new NullPointerException("source == null");
        return GRAYSCALE_FILTER.apply(source, null);
    }

}
