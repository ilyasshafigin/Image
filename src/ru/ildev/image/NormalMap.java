/*
 *
 */
package ru.ildev.image;

import ru.ildev.color.Color;
import ru.ildev.math.MoreMath;

import java.awt.image.BufferedImage;

/**
 * Класс, генерирующий карту нормалей из карты высот.
 *
 * @author Shafigin Ilyas (Шафигин Ильяс) <Ilyas74>
 * @version 0.1.1
 */
public class NormalMap {

    /**
     * Генерирует карту нормалей из карты высот.
     *
     * @param heightMap изображение карты высот.
     * @param factor    фактор.
     * @return карту нормалей изображения.
     */
    public static BufferedImage generate(BufferedImage heightMap, float factor) {
        int width = heightMap.getWidth();
        int height = heightMap.getHeight();
        BufferedImage bumpMap = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                bumpMap.setRGB(x, y, generateBumpPixel(heightMap, x, y, factor));
            }
        }
        return bumpMap;
    }

    /**
     * Применяет карту нормалей изображению.
     *
     * @param image изображение.
     * @param bump  карта нормалей.
     * @return новое изображение.
     */
    public static BufferedImage apply(BufferedImage image, BufferedImage bump) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        bump = HeightMap.generate(bump);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int srgb = image.getRGB(x, y);
                int bb = bump.getRGB(x, y) & 0xff;

                int sr = 0xff & (srgb >> 16);
                int sg = 0xff & (srgb >> 8);
                int sb = 0xff & srgb;

                int r = Color.clamp(sr + bb * 2 - 255);
                int g = Color.clamp(sg + bb * 2 - 255);
                int b = Color.clamp(sb + bb * 2 - 255);

                result.setRGB(x, y, (0xff000000 & srgb) | (r << 16) | (g << 8) | b);
            }
        }
        return result;
    }

    private static int generateBumpPixel(BufferedImage image, int x, int y, float a) {
        float sz = a * getHeight(image, x + 1, y) - a * getHeight(image, x - 1, y);
        float tz = a * getHeight(image, x, y + 1) - a * getHeight(image, x, y - 1);

        float den = MoreMath.sqrt(sz * sz + tz * tz + 1.0f);
        float invDen = den <= 0.0f ? 0.0f : 1.0f / den;

        float nx = -sz * invDen;
        float ny = -tz * invDen;
        float nz = 1.0f;

        return NormalMap.toColor(nx, ny, nz);
    }

    private static float getHeight(BufferedImage image, int x, int y) {
        x = MoreMath.clamp(x, 0, image.getWidth() - 1);
        y = MoreMath.clamp(y, 0, image.getHeight() - 1);
        return image.getRGB(x, y) & 0xff;
    }

    private static int toColor(float x, float y, float z) {
        int r = MoreMath.round(255 * ((x + 1.0f) / 2.0f));
        int g = MoreMath.round(255 * ((y + 1.0f) / 2.0f));
        int b = MoreMath.round(255 * ((z + 1.0f) / 2.0f));
        return 0xff000000 | (r << 16) | (g << 8) | b;
    }

}
