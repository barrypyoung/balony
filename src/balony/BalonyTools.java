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

    public static Float[] getMinMax(Object[] a) {
        float min = Float.MAX_VALUE;
        float max = -Float.MAX_VALUE;
        float value;
        
        for (int i = 0; i < a.length; i++) {
            value = (Float)a[i];
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
}
