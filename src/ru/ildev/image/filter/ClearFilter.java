/*
 *
 */
package ru.ildev.image.filter;

/**
 * Фильтр, очищающий изображение, т.е. заменяет цвет каждого цвета на черный.
 *
 * @author Ilyas74
 * @version 0.1.1
 */
public class ClearFilter extends ColorFilter {

    @Override
    protected int apply(int x, int y, int color) {
        return 0;
    }

}
