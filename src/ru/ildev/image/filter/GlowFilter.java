/*
 *
 */
package ru.ildev.image.filter;

import ru.ildev.math.MoreMath;

/**
 * Класс фильтра, увеличивающего свечение изображения.
 *
 * @author Ilyas74
 * @version 0.1.2
 */
public class GlowFilter extends GaussianBlurFilter {

    /**  */
    private float amount = 0.5f;

    /**
     * Стандартный конструктор.
     */
    public GlowFilter() {
        super(2);
    }

    /**
     * @param amount
     */
    public GlowFilter(float amount) {
        super(2);
        this.amount = amount;
    }

    /**
     * @return
     */
    public float getAmount() {
        return this.amount;
    }

    /**
     * @param amount
     */
    public void setAmount(float amount) {
        this.amount = amount;
    }

    @Override
    public void apply(int[] in, int[] out, int width, int height) {
        int[] out2 = new int[width * height];
        super.apply(in, out2, width, height);

        boolean rchannel = this.isRedChannel();
        boolean gchannel = this.isGreenChannel();
        boolean bchannel = this.isBlueChannel();
        boolean achannel = this.isAlphaChannel();
        boolean monochrome = this.isMonochrome();

        if (monochrome) {
            float a = 1.0f * this.amount;

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int index = x + y * width;
                    int color1 = in[index];
                    int color2 = out2[index];

                    out[index] = MoreMath.clamp((int) (color1 + a * color2), 0, 255);
                }
            }
        } else {
            float a = 4.0f * this.amount;

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int index = x + y * width;

                    int rgb1 = in[index];
                    int a1 = 0xff & (rgb1 >> 24);
                    int r1 = 0xff & (rgb1 >> 16);
                    int g1 = 0xff & (rgb1 >> 8);
                    int b1 = 0xff & rgb1;

                    int rgb2 = out2[index];
                    int a2 = 0xff & (rgb2 >> 24);
                    int r2 = 0xff & (rgb2 >> 16);
                    int g2 = 0xff & (rgb2 >> 8);
                    int b2 = 0xff & rgb2;

                    a1 = MoreMath.clamp((int) (a1 + a * a2), 0, 255);
                    r1 = MoreMath.clamp((int) (r1 + a * r2), 0, 255);
                    g1 = MoreMath.clamp((int) (g1 + a * g2), 0, 255);
                    b1 = MoreMath.clamp((int) (b1 + a * b2), 0, 255);

                    out[index] = (a1 << 24) | (r1 << 16) | (g1 << 8) | b1;
                }
            }
        }
    }

}
