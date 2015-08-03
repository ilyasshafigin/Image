/*
 *
 */
package ru.ildev.image.filter;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Класс фильтра, рисующий граници изображению.
 *
 * @author Shafigin Ilyas (Шафигин Ильяс) <Ilyas74>
 * @version 0.0.1
 */
public class BorderFilter extends Filter {

    /* Значения высоты и ширины границ в пикселях. */
    private int leftBorder;
    private int rightBorder;
    private int topBorder;
    private int bottomBorder;

    /**
     * Объект рисования границ.
     */
    private Paint borderPaint = null;
    /**
     * Флаг, определяющий, будут ли при рисовании границ изменяться размеры изображения.
     */
    private boolean resizeImage = false;

    /**
     * Конструктор, устанавливающий значения границам и объект их рисования.
     *
     * @param leftBorder   ширина левой границы.
     * @param topBorder    высота верхней границы.
     * @param rightBorder  ширина правой границы.
     * @param bottomBorder высота нижней границы.
     * @param borderPaint  объект рисования.
     */
    public BorderFilter(int leftBorder, int topBorder, int rightBorder, int bottomBorder, Paint borderPaint) {
        this(leftBorder, topBorder, rightBorder, bottomBorder, borderPaint, false);
    }

    /**
     * Конструктор, устанавливающий значения границам, объект их рисования и флаг изменения размеров изображения.
     *
     * @param leftBorder   ширина левой границы.
     * @param topBorder    высота верхней границы.
     * @param rightBorder  ширина правой границы.
     * @param bottomBorder высота нижней границы.
     * @param borderPaint  объект рисования.
     * @param resizeImage  флаг изменения размеров изображения.
     */
    public BorderFilter(int leftBorder, int topBorder, int rightBorder, int bottomBorder,
                        Paint borderPaint, boolean resizeImage) {
        this.leftBorder = leftBorder;
        this.rightBorder = rightBorder;
        this.topBorder = topBorder;
        this.bottomBorder = bottomBorder;
        this.borderPaint = borderPaint;
        this.resizeImage = resizeImage;
    }

    /**
     * Получает ширину левой границы.
     *
     * @return ширину левой границы.
     */
    public int getLeftBorder() {
        return this.leftBorder;
    }

    /**
     * Устанавливает ширину левой границы.
     *
     * @param leftBorder ширина левой границы.
     */
    public void setLeftBorder(int leftBorder) {
        this.leftBorder = leftBorder;
    }

    /**
     * Получает ширину правой границы.
     *
     * @return ширину правой границы.
     */
    public int getRightBorder() {
        return this.rightBorder;
    }

    /**
     * Устанавливает ширину правой границы.
     *
     * @param rightBorder ширина правой границы.
     */
    public void setRightBorder(int rightBorder) {
        this.rightBorder = rightBorder;
    }

    /**
     * Получает высоту верхней границы.
     *
     * @return высоту верхней границы.
     */
    public int getTopBorder() {
        return this.topBorder;
    }

    /**
     * Устанавливает высоту верхней границы.
     *
     * @param topBorder высота верхней границы.
     */
    public void setTopBorder(int topBorder) {
        this.topBorder = topBorder;
    }

    /**
     * Получает высоту нижней границы.
     *
     * @return высоту нижней границы.
     */
    public int getBottomBorder() {
        return this.bottomBorder;
    }

    /**
     * Устанавливает высоту нижней границы.
     *
     * @param bottomBorder высота нижней границы.
     */
    public void setBottomBorder(int bottomBorder) {
        this.bottomBorder = bottomBorder;
    }

    /**
     * Получает объект рисования границ.
     *
     * @return объект рисования границ.
     */
    public Paint getBorderPaint() {
        return this.borderPaint;
    }

    /**
     * Устанавливает объект рисования границ.
     *
     * @param borderPaint объект рисования границ.
     */
    public void setBorderPaint(Paint borderPaint) {
        this.borderPaint = borderPaint;
    }

    /**
     * Определяет, изменять ли размеры изображения при рисовании границ.
     *
     * @return {@code true}, если будут изменяться размеры изображения.
     */
    public boolean isResizeImage() {
        return this.resizeImage;
    }

    /**
     * Устанавливает флаг изменения размеров изображения при рисовании границ.
     *
     * @param resizeImage флаг.
     */
    public void setResizeImage(boolean resizeImage) {
        this.resizeImage = resizeImage;
    }

    @Override
    public BufferedImage apply(BufferedImage src, BufferedImage dst,
                               int x, int y, int width, int height, int iterations) {
        if (src == null) throw new NullPointerException("src == null");
        if (dst == null) dst = this.createDestination(src, width, height);

        int nx = 0;
        int ny = 0;
        int nw = width;
        int nh = height;

        Graphics2D g = dst.createGraphics();
        if (!this.resizeImage) g.drawRenderedImage(src, null);
        if (this.borderPaint != null) {
            g.setPaint(this.borderPaint);
            if (this.leftBorder > 0) {
                if (this.resizeImage) nx = this.leftBorder;
                g.fillRect(0, 0, this.leftBorder, height);
            }
            if (this.rightBorder > 0) {
                if (this.resizeImage) nw = width - this.rightBorder - nx;
                g.fillRect(width - this.rightBorder, 0, this.rightBorder, height);
            }
            if (this.topBorder > 0) {
                if (this.resizeImage) ny = this.topBorder;
                g.fillRect(this.leftBorder, 0, width - this.leftBorder - this.rightBorder, this.topBorder);
            }
            if (this.bottomBorder > 0) {
                if (this.resizeImage) nh = height - this.bottomBorder - ny;
                g.fillRect(this.leftBorder, height - this.bottomBorder, width - this.leftBorder - this.rightBorder,
                        this.bottomBorder);
            }
        }
        if (this.resizeImage) g.drawImage(src, nx, ny, nw, nh, null);
        g.dispose();
        return dst;
    }

    @Override
    public void apply(int[] in, int[] out, int width, int height) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
