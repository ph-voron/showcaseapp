package app.voron.ph.showcaseapp.Utilities;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import app.voron.ph.showcaseapp.System.Consts;

/**
 * Created by TJack on 09.11.2017.
 */

public class FormatHelper {
    public static String formatProductCost(float value){
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.ROOT);
        formatSymbols.setDecimalSeparator('.');
        DecimalFormat format = new DecimalFormat("###.#", formatSymbols);
        String valueStr = format.format(value);
        return String.format("%s %c", valueStr, Consts.CURRENCY_SYMBOL);
    }
}
