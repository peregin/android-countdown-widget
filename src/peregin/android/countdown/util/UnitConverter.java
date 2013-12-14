package peregin.android.countdown.util;

import android.content.Context;

/**
 * User: levi
 * Date: 1/16/13
 * Time: 11:25 PM
 *
 * @author <a href="mailto:lfall@peregin.com">Levente Fall</a>
 */
public class UnitConverter {

    static public int dip2pixel(Context context, int dip) {
        return (int) (dip * context.getResources().getDisplayMetrics().density + 0.5f);
    }
}
