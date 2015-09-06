package com.nordeck.wiki.reader;

import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by parker on 9/5/15.
 */
public class Utils {

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
