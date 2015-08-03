/*
 *
 */
package ru.ildev.image.filter;

import ru.ildev.geom.Transform3;
import ru.ildev.geom.Vector3;

/**
 * @author Shafigin Ilyas (Шафигин Ильяс) <Ilyas74>
 * @version 0.0.0
 */
public class Transform3DFilter extends TransformFilter {

    /**  */
    protected Transform3 transform3 = new Transform3();

    private float hw;
    private float hh;

    private final Vector3 tmp = new Vector3();
    private final float distance = 1.0f;

    /**
     * Конструктор, устанавливающий объект трансформации.
     *
     * @param transform3 объект трансформации.
     */
    public Transform3DFilter(Transform3 transform3) {
        if (transform3 == null) throw new NullPointerException("transform3 == null");
        this.transform3 = transform3;
    }

    /**
     * Конструктор, устанавливающий объект трансформации.
     *
     * @param transform3 объект трансформации.
     * @param edgeAction действие с краями.
     */
    public Transform3DFilter(Transform3 transform3, int edgeAction) {
        if (transform3 == null) throw new NullPointerException("transform3 == null");
        this.transform3 = transform3;
        this.edgeAction = edgeAction;
    }

    /**
     * Конструктор, устанавливающий объект трансформации.
     *
     * @param transform3    объект трансформации.
     * @param edgeAction    действие с краями.
     * @param interpolation тип интерполяции изображений.
     */
    public Transform3DFilter(Transform3 transform3, int edgeAction, int interpolation) {
        if (transform3 == null) throw new NullPointerException("transform3 == null");
        this.transform3 = transform3;
        this.edgeAction = edgeAction;
        this.interpolation = interpolation;
    }

    /**
     * Получает объект трансформации.
     *
     * @return объект трансформации.
     */
    public Transform3 getTransform3D() {
        return this.transform3;
    }

    /**
     * Устанавливает объект трансформации.
     *
     * @param transform3 объект трансформации.
     */
    public void setTransform3D(Transform3 transform3) {
        if (transform3 == null) throw new NullPointerException("transform3 == null");
        this.transform3 = transform3;
    }

    @Override
    public void apply(int[] in, int[] out, int width, int height) {
        this.hw = width * 0.5f;
        this.hh = height * 0.5f;

        super.apply(in, out, width, height);
    }

    @Override
    protected void transformPixel(int x, int y, float[] out) {
        this.tmp.set(x - this.hw, y - this.hh, 0.0f);
        this.transform3.transform(this.tmp);

        out[0] = this.distance * this.tmp.x / -this.tmp.z + this.hw;
        out[1] = this.distance * -this.tmp.y / -this.tmp.z + this.hh;
    }

}
