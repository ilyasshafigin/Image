/*
 *
 */
package ru.ildev.image.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Фильтр, содержащий в себе список фильтов, которые будет применят для изображения.
 *
 * @author Shafigin Ilyas (Шафигин Ильяс) <Ilyas74>
 * @version 0.0.3
 */
public class CombinedFilter extends Filter {

    /**
     * Список фильтров.
     */
    protected List<Filter> filters = new ArrayList<>();

    /**
     * Стандартный конструктор.
     */
    public CombinedFilter() {
    }

    /**
     * Конструктор, добавляющий первый фильтр.
     *
     * @param filter фильтр.
     */
    public CombinedFilter(Filter filter) {
        if (filter != null) {
            this.filters.add(filter);
        }
    }

    /**
     * Конструктор, добавляющий фильтры.
     *
     * @param filters массив фильтров.
     */
    public CombinedFilter(Filter... filters) {
        if (filters != null) {
            this.filters.addAll(Arrays.asList(filters));
        }
    }

    /**
     * Конструктор, добавляющий фильтры.
     *
     * @param filters массив фильтров.
     */
    public CombinedFilter(Collection<? extends Filter> filters) {
        if (filters != null) {
            this.filters.addAll(filters);
        }
    }

    /**
     * Получает список добавленных фильтров.
     *
     * @return список фильтров.
     */
    public List<Filter> getFilters() {
        return this.filters;
    }

    /**
     * Добавляет фильтр.
     *
     * @param filter фильтр.
     */
    public void addFilter(Filter filter) {
        if (filter == null) return;
        this.filters.add(filter);
    }

    /**
     * Удаляет фильтр.
     *
     * @param filter фильтр.
     */
    public void removeFilter(Filter filter) {
        if (filter == null) return;
        this.filters.remove(filter);
    }

    /**
     * Очищает список фильтров.
     */
    public void clear() {
        this.filters.clear();
    }

    @Override
    public void apply(int[] in, int[] out, int width, int height) {
        if (!this.filters.isEmpty()) {
            int length = width * height;
            int[] t = new int[length];

            // Применяем фильтры.
            for (Filter filter : this.filters) {
                filter.apply(in, out, width, height);

                System.arraycopy(out, 0, t, 0, length);
                System.arraycopy(out, 0, in, 0, length);
                System.arraycopy(t, 0, in, 0, length);
            }
        }
    }

}
