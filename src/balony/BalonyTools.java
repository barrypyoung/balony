/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package balony;

/**
 *
 * @author Barry Young
 */
public class BalonyTools {

    private final static char SYMBOL[] = {'M', 'D', 'C', 'L', 'X', 'V', 'I'};
    private final static int VALUE[] = {1000, 500, 100, 50, 10, 5, 1};
    private static final int[] NUMBERS = {1000, 900, 500, 400, 100, 90,
        50, 40, 10, 9, 5, 4, 1};
    private static final String[] LETTERS = {"M", "CM", "D", "CD", "C", "XC",
        "L", "XL", "X", "IX", "V", "IV", "I"};

    public static Float[] getMinMax(Object[] a) {
        float min = Float.MAX_VALUE;
        float max = -Float.MAX_VALUE;
        float value;

        for (Object a1 : a) {
            value = (Float) a1;
            if (value < min) {
                min = value;
            }
            if (value > max) {
                max = value;
            }
        }

        Float[] minAndMax = new Float[2];
        minAndMax[0] = min;
        minAndMax[1] = max;
        return minAndMax;
    }

    public static int romanValueOf(String roman) {
        roman = roman.toUpperCase();
        if (roman.length() == 0) {
            return 0;
        }
        for (int i = 0; i < SYMBOL.length; i++) {
            int pos = roman.indexOf(SYMBOL[i]);
            if (pos >= 0) {
                return VALUE[i] - romanValueOf(roman.substring(0, pos)) + romanValueOf(roman.substring(pos + 1));
            }
        }
        throw new IllegalArgumentException("Invalid Roman Symbol.");
    }

    public String convertToRoman(int N) {
        String roman = "";
        for (int i = 0; i < NUMBERS.length; i++) {
            while (N >= NUMBERS[i]) {
                roman += LETTERS[i];
                N -= NUMBERS[i];
            }
        }
        return roman;
    }
}
