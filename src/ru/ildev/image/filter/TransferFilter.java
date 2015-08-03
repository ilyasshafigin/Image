/*
 *
 */
package ru.ildev.image.filter;

import ru.ildev.math.MoreMath;

/**
 * Класс фильтра, изменяющего цвета пикселей изображений в соответствии с цветами таблицы по такой формуле: c[i] =
 * table[с[i]], где c[i] - цвет пикселя по индексом i одного из трех компонентов, table - таблица одного из трех
 * компонентов.
 *
 * @author Shafigin Ilyas (Шафигин Ильяс) <Ilyas74>
 * @version 0.0.1
 */
public class TransferFilter extends ColorFilter {

    /**
     * Таблицы значений компонентов цветов.
     */
    protected int[] rTable, gTable, bTable;
    /**
     * Флаг инициализации таблиц.
     */
    protected boolean initialized = false;

    /**
     * Стандартный конструктор.
     */
    public TransferFilter() {
    }

    /**
     * Конструктор, устанавливающий таблицы.
     *
     * @param rTable таблица красных компонентов.
     * @param gTable таблица зеленых компонентов.
     * @param bTable таблица синих компонентов.
     */
    public TransferFilter(int[] rTable, int[] gTable, int[] bTable) {
        if (rTable == null) throw new NullPointerException("rTable == null");
        if (gTable == null) throw new NullPointerException("gTable == null");
        if (bTable == null) throw new NullPointerException("bTable == null");
        if (rTable.length != 256) throw new IllegalArgumentException("Illegal size rTable");
        if (gTable.length != 256) throw new IllegalArgumentException("Illegal size gTable");
        if (bTable.length != 256) throw new IllegalArgumentException("Illegal size bTable");

        this.rTable = rTable;
        this.gTable = gTable;
        this.bTable = bTable;
        this.initialized = true;
    }

    /**
     * Получает таблицу значений красных компонентов.
     *
     * @return таблицу значений красных компонентов.
     */
    public int[] getRedTable() {
        return this.rTable;
    }

    /**
     * Получает таблицу значений зеленых компонентов.
     *
     * @return таблицу значений зеленых компонентов.
     */
    public int[] getGreenTable() {
        return this.gTable;
    }

    /**
     * Получает таблицу значений синих компонентов.
     *
     * @return таблицу значений синих компонентов.
     */
    public int[] getBlueTable() {
        return this.bTable;
    }

    /**
     * Получает значение таблицы красных компонентов.
     *
     * @param index индекс значения, от 0 до 255.
     * @return значение таблицы красных компонентов.
     */
    public int getRed(int index) {
        if (!this.initialized || index < 0 || index >= 256) {
            return -1;
        } else {
            return this.rTable[index];
        }
    }

    /**
     * Получает значение таблицы зеленых компонентов.
     *
     * @param index индекс значения, от 0 до 255.
     * @return значение таблицы зеленых компонентов.
     */
    public int getGreen(int index) {
        if (!this.initialized || index < 0 || index >= 256) {
            return -1;
        } else {
            return this.gTable[index];
        }
    }

    /**
     * Получает значение таблицы синих компонентов.
     *
     * @param index индекс значения, от 0 до 255.
     * @return значение таблицы синих компонентов.
     */
    public int getBlue(int index) {
        if (!this.initialized || index < 0 || index >= 256) {
            return -1;
        } else {
            return this.bTable[index];
        }
    }

    /**
     * Инициализирует таблицы.
     */
    protected void initialize() {
        this.rTable = this.gTable = this.bTable = this.makeTable();
    }

    /**
     * Создает таблицу.
     *
     * @return таблицу.
     */
    protected int[] makeTable() {
        int[] table = new int[256];
        for (int i = 0; i < 256; i++) {
            table[i] = MoreMath.clamp((int) (255 * this.transfer(i / 255.0f)), 0, 255);
        }
        return table;
    }

    /**
     * Функция, изменяющая значение параметра {@code a}.
     *
     * @param a параметр.
     * @return новое значение.
     */
    protected float transfer(float a) {
        return a;
    }

    @Override
    public void apply(int[] in, int[] out, int width, int height) {
        if (!this.initialized) this.initialize();
        super.apply(in, out, width, height);
    }

    @Override
    protected int apply(int x, int y, int color) {
        if (this.isMonochrome()) {
            return this.bTable[color];
        } else {
            int a = 0xff000000 & color;
            int r = 0xff & (color >> 16);
            int g = 0xff & (color >> 8);
            int b = 0xff & color;

            if (this.isRedChannel()) r = this.rTable[r];
            if (this.isGreenChannel()) g = this.gTable[g];
            if (this.isBlueChannel()) b = this.bTable[b];

            return a | (r << 16) | (g << 8) | b;
        }
    }

}
