/*
 *
 */
package ru.ildev.image.filter;

import ru.ildev.color.Color;
import ru.ildev.math.MoreMath;

/**
 * Класс фильтра, выполняющий обратную функцию размытия, т.е. он уменьшает размытие изображение по Гауссу.
 *
 * @author Ilyas74
 * @version 0.1.2
 */
public class UnsharpFilter extends GaussianBlurFilter {

    /**  */
    private float amount = 0.5f;
    /**  */
    private int threshold = 1;

    /**
     * Стандартный конструктор.
     */
    public UnsharpFilter() {
        super(2);
    }

    /**
     * @param amount
     * @param threshold
     */
    public UnsharpFilter(float amount, int threshold) {
        super(2);
        this.amount = amount;
        this.threshold = threshold;
    }

    /**
     * @param threshold
     */
    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    /**
     * @return
     */
    public int getThreshold() {
        return this.threshold;
    }

    /**
     * @param amount
     */
    public void setAmount(float amount) {
        this.amount = amount;
    }

    /**
     * @return
     */
    public float getAmount() {
        return this.amount;
    }

    @Override
    public void apply(int[] in, int[] out, int width, int height) {
        int[] out2 = new int[width * height];
        super.apply(in, out2, width, height);

        if (this.isMonochrome()) {
            float a = 1.0f * this.amount;

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int index = x + y * width;
                    int color1 = in[index];
                    int color2 = out2[index];

                    if (MoreMath.abs(color1 - color2) >= this.threshold) {
                        out[index] = Color.clamp((int) ((a + 1.0) * (color1 - color2) + color2));
                    } else {
                        out[index] = color1;
                    }
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

                    if (MoreMath.abs(a1 - a2) >= this.threshold) a1 = Color.clamp((int) ((a + 1.0) * (a1 - a2) + a2));
                    if (MoreMath.abs(r1 - r2) >= this.threshold) r1 = Color.clamp((int) ((a + 1.0) * (r1 - r2) + r2));
                    if (MoreMath.abs(g1 - g2) >= this.threshold) g1 = Color.clamp((int) ((a + 1.0) * (g1 - g2) + g2));
                    if (MoreMath.abs(b1 - b2) >= this.threshold) b1 = Color.clamp((int) ((a + 1.0) * (b1 - b2) + b2));

                    out[index] = (a1 << 24) | (r1 << 16) | (g1 << 8) | b1;
                }
            }
        }
    }

}
