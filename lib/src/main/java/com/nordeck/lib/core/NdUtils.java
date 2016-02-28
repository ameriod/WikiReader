package com.nordeck.lib.core;

import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by parker on 9/5/15.
 */
public final class NdUtils {

    private NdUtils() {
        throw new IllegalStateException("Can not create an instance of a utils class");
    }

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
