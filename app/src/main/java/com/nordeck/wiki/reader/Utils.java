package com.nordeck.wiki.reader;

import android.view.View;

/**
 * Created by parker on 9/5/15.
 */
public class Utils {

    public static void setViewVisibility(View v, boolean show) {
        if (show) {
            v.setVisibility(View.VISIBLE);
        } else {
            v.setVisibility(View.GONE);
        }
    }
}
