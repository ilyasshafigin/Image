/*
 *
 */
package ru.ildev.image.filter;

import ru.ildev.color.Color;

/**
 * Класс фильтра, добавляющего тени непрозрачным частям изображений.
 *
 * @author Shafigin Ilyas (Шафигин Ильяс) <Ilyas74>
 * @version 0.0.0
 */
public class ShadowFilter extends Filter {

    /**
     * Смещение тени по оси Ox.
     */
    private float offsetX = 0.0f;
    /**
     * Смещение тени по оси Oy.
     */
    private float offsetY = 0.0f;
    /**
     * Радиус размытия тени.
     */
    private int blurRadius = 5;
    /**
     * Цвет тени.
     */
    private int color = 0xff000000;
    /**
     * Прозрачность тени.
     */
    private float opacity = 1.0f;

    private BandFilter bandFilter;
    private OffsetFilter offsetFilter;
    private GaussianBlurFilter blurFilter;
    private boolean init = false;

    /**
     * Стандартный конструктор.
     *
     * @param offsetX    смещение тени по оси Ox.
     * @param offsetY    смещение тени по оси Oy.
     * @param blurRadius радиус размытия тени.
     * @param color      цвет тени.
     * @param opacity    прозрачность тени.
     */
    public ShadowFilter(float offsetX, float offsetY, int blurRadius, int color, float opacity) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.blurRadius = blurRadius;
        this.color = color;
        this.opacity = opacity;
        this.init = false;
    }

    /**
     * Получает смещение тени по оси Ox.
     *
     * @return смещение тени по оси Ox.
     */
    public float getOffsetX() {
        return this.offsetX;
    }

    /**
     * Устанавливает смещение тени по оси Ox.
     *
     * @param offsetX смещение тени по оси Ox.
     */
    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
        this.init = false;
    }

    /**
     * Получает смещение тени по оси Oy.
     *
     * @return смещение тени по оси Oy.
     */
    public float getOffsetY() {
        return this.offsetY;
    }

    /**
     * Устанавливает смещение тени по оси Oy.
     *
     * @param offsetY смещение тени по оси Oy.
     */
    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
        this.init = false;
    }

    /**
     * Получает радиус размытия тени.
     *
     * @return радиус размытия тени.
     */
    public int getBlurRadius() {
        return this.blurRadius;
    }

    /**
     * Устанавливает радиус размытия тени.
     *
     * @param blurRadius радиус размытия тени.
     */
    public void setBlurRadius(int blurRadius) {
        this.blurRadius = blurRadius;
        this.init = false;
    }

    /**
     * Получает цвет тени.
     *
     * @return цвет тени.
     */
    public int getColor() {
        return this.color;
    }

    /**
     * Устанавливает цвет тени.
     *
     * @param color цвет тени.
     */
    public void setColor(int color) {
        this.color = color;
        this.init = false;
    }

    /**
     * Получает прозрачность тени.
     *
     * @return прозрачность тени.
     */
    public float getOpacity() {
        return this.opacity;
    }

    /**
     * Устанавливает прозрачность тени.
     *
     * @param opacity прозрачность тени.
     */
    public void setOpacity(float opacity) {
        this.opacity = opacity;
        this.init = false;
    }

    private void init() {
        boolean rchannel = this.isRedChannel();
        boolean gchannel = this.isGreenChannel();
        boolean bchannel = this.isBlueChannel();
        boolean achannel = this.isAlphaChannel();
        boolean monochrome = this.isMonochrome();

        this.bandFilter = new BandFilter(new float[]{
                (0xff & (this.color >> 24)) / 255.0f, 0.0f, 0.0f, 0.0f,
                (0xff & (this.color >> 16)) / 255.0f, 0.0f, 0.0f, 0.0f,
                (0xff & (this.color >> 8)) / 255.0f, 0.0f, 0.0f, 0.0f,
                (0xff & this.color) / 255.0f, 0.0f, 0.0f, 0.0f
        });
        this.offsetFilter = new OffsetFilter(this.offsetX, this.offsetY,
                TransformFilter.CROP_EDGES,
                TransformFilter.NEAREST_NEIGHBOUR_INTERPOLATION);
        this.blurFilter = new GaussianBlurFilter(this.blurRadius,
                ConvolveFilter.CROP_EDGES);

        this.bandFilter.setChannels(rchannel, gchannel, bchannel, achannel, monochrome);
        this.offsetFilter.setChannels(rchannel, gchannel, bchannel, achannel, monochrome);
        this.blurFilter.setChannels(rchannel, gchannel, bchannel, achannel, monochrome);
        this.init = true;
    }

    @Override
    public void apply(int[] in, int[] out, int width, int height) {
        if (!this.init) this.init();

        int size = width * height;
        int[] shadow = new int[size];
        int[] tmp = new int[size];

        this.bandFilter.apply(in, shadow, width, height);
        System.arraycopy(shadow, 0, tmp, 0, size);
        this.offsetFilter.apply(tmp, shadow, width, height);
        System.arraycopy(shadow, 0, tmp, 0, size);
        this.blurFilter.apply(tmp, shadow, width, height);

        for (int i = 0; i < size; i++) {
            int a = Color.clamp((int) ((0xff & (in[i] >> 24)) + this.opacity * (0xff & (shadow[i] >> 24))));
            int r = Color.clamp((int) ((0xff & (in[i] >> 16)) + this.opacity * (0xff & (shadow[i] >> 16))));
            int g = Color.clamp((int) ((0xff & (in[i] >> 8)) + this.opacity * (0xff & (shadow[i] >> 8))));
            int b = Color.clamp((int) ((0xff & in[i]) + this.opacity * (0xff & shadow[i])));
            out[i] = (a << 24) | (r << 16) | (g << 8) | b;
        }
    }

}
