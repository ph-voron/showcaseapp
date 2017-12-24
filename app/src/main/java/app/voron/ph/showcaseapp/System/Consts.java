package app.voron.ph.showcaseapp.System;

import android.support.v4.util.Pair;

/**
 * Created by TJack on 22.10.2017.
 */

public class Consts {
    //
    public static final char CURRENCY_SYMBOL = '\u20BD';
    //
    public static final String KEY_PRODUCT_ID = "KEY_PRODUCT_ID";
    public static final String KEY_DATA = "KEY_DATA";
    public static final String KEY_PREVIEW_BITMAP = "KEY_PREVIEW_BITMAP";
    //
    public static final Pair[] SHOWCASE_CATEGORY_TAGS = new Pair[] {
            new Pair<>("mainCourse", "Основные блюда"),
            new Pair<>("soup", "Супы"),
            new Pair<>("salad", "Салаты"),
            new Pair<>("snacks", "Закуски"),
            new Pair<>("dessert", "Десерты"),
            new Pair<>("beverage", "Напитки")
    };
}
