package pashmash.tryBounty.util;

import java.text.DecimalFormat;

public class NumberUtil {
    private static final String[] SUFFIXES = new String[]{"", "K", "M", "B", "T", "Q", "QQ"};
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    public static String formatSuffixed(double value) {
        int index = 0;
        while (value >= 1000 && index < SUFFIXES.length - 1) {
            value /= 1000;
            index++;
        }
        return DECIMAL_FORMAT.format(value) + SUFFIXES[index];
    }

    public static double parseSuffixed(String value) {
        value = value.toUpperCase();
        for (int i = SUFFIXES.length - 1; i >= 0; i--) {
            if (value.endsWith(SUFFIXES[i])) {
                try {
                    return Double.parseDouble(value.substring(0, value.length() - SUFFIXES[i].length())) * Math.pow(1000, i);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid number format: " + value);
                }
            }
        }
        throw new IllegalArgumentException("Invalid suffix in number: " + value);
    }


}