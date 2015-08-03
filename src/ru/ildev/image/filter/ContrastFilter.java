/*
 *
 */
package ru.ildev.image.filter;

/**
 * Фильтр, изменяющий контрастность изображения.
 *
 * @author Ilyas74
 * @version 0.1.0
 */
public class ContrastFilter extends TransferFilter {

    /**
     * Фактор яркости.
     */
    private float brightness = 1.0f;
    /**
     * Фактор контрастности.
     */
    private float contrast = 1.0f;

    /**
     * Стандартный конструктор.
     *
     * @param brightness фактор яркости.
     * @param contrast   фактор контрастности.
     */
    public ContrastFilter(float brightness, float contrast) {
        this.brightness = brightness;
        this.contrast = contrast;
    }

    /**
     * Получает фактор яркости.
     *
     * @return фактор яркости.
     */
    public float getBrightness() {
        return this.brightness;
    }

    /**
     * Устанавливает фактор контрастности.
     *
     * @param brightness фактор контрастности.
     */
    public void setBrightness(float brightness) {
        this.brightness = brightness;
    }

    /**
     * Получает фактор яркконтрастностиости.
     *
     * @return фактор контрастности.
     */
    public float getContrast() {
        return this.contrast;
    }

    /**
     * Устанавливает фактор контрастности.
     *
     * @param contrast фактор контрастности.
     */
    public void setContrast(float contrast) {
        this.contrast = contrast;
    }

    @Override
    protected float transfer(float a) {
        a *= this.brightness;
        a = (a - 0.5f) * this.contrast + 0.5f;
        return a;
    }

}
