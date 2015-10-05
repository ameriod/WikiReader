package in.nordeck.lib.base;

import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by parker on 10/4/15.
 */
public class NdUtils {

    public static void setViewVisibility(@Nullable View v, boolean show) {
        if (v == null) {
            return;
        }
        if (show) {
            v.setVisibility(View.VISIBLE);
        } else {
            v.setVisibility(View.GONE);
        }
    }
}
