package com.nordeck.wiki.reader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.nordeck.lib.core.NdPreferenceUtils;
import com.nordeck.wiki.reader.model.WikiDetail;

public class PreferenceUtils extends NdPreferenceUtils {
    public static final String PREFS_NAME = "wiki_reader_prefs";

    public static PreferenceUtils newInstance(Context context) {
        return new PreferenceUtils(context);
    }

    private PreferenceUtils(@NonNull Context context) {
        super(context, PREFS_NAME);
    }

    private static final String KEY_SELECTED_WIKI_JSON = "key_selected_wiki_json";


    public void setSelectedWiki(@Nullable WikiDetail wiki) {
        if (wiki != null) {
            try {
                getEditor().putString(KEY_SELECTED_WIKI_JSON, new Gson().toJson(wiki)).commit();
            } catch (JsonParseException e) {
                getEditor().putString(KEY_SELECTED_WIKI_JSON, "");
            }
        } else {
            getEditor().putString(KEY_SELECTED_WIKI_JSON, "");
        }

    }

    @Nullable
    public WikiDetail getSelectedWiki() {
        String json = getPrefs().getString(KEY_SELECTED_WIKI_JSON, "");
        Gson gson = new Gson();
        try {
            return gson.fromJson(json, WikiDetail.class);
        } catch (JsonParseException e) {
            return null;
        }
    }
}
