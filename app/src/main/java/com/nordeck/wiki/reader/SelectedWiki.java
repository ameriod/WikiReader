package com.nordeck.wiki.reader;

import android.content.Context;
import android.support.annotation.Nullable;

import com.nordeck.wiki.reader.model.WikiDetail;

/**
 * Created by parker on 9/6/15.
 */
public class SelectedWiki {

    private static WikiDetail selectedWiki;

    private static SelectedWiki ourInstance;

    public static SelectedWiki getInstance() {
        return ourInstance;
    }

    public static void setInstance(SelectedWiki ourInstance) {
        SelectedWiki.ourInstance = ourInstance;
    }

    public SelectedWiki(Context context) {
        selectedWiki = PreferenceUtils.getSelectedWiki(context.getApplicationContext());
    }

    public static void setSelectedWiki(@Nullable WikiDetail selectedWiki, Context context) {
        SelectedWiki.selectedWiki = selectedWiki;
        PreferenceUtils.setSelectedWiki(context.getApplicationContext(), selectedWiki);
    }

    public static WikiDetail getSelectedWiki() {
        return selectedWiki;
    }
}
