package com.nordeck.lib.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

public class NdPreferenceUtils {

    private String prefsName;
    private Context context;

    public NdPreferenceUtils(@NonNull Context context, @NonNull String prefsName) {
        this.prefsName = prefsName;
        this.context = context.getApplicationContext();
    }

    protected SharedPreferences getPrefs() {
        return context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
    }

    protected SharedPreferences.Editor getEditor() {
        return getPrefs().edit();
    }


}
