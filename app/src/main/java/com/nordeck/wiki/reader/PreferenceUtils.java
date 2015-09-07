package com.nordeck.wiki.reader;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.nordeck.wiki.reader.model.WikiDetail;

public class PreferenceUtils {
    public static final String PREFS_NAME = "wiki_reader_prefs";

    private static final String KEY_SELECTED_WIKI_JSON = "key_selected_wiki_json";

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        return getPrefs(context).edit();
    }

    public static void setSelectedWiki(Context context, @Nullable WikiDetail wiki) {
        if (wiki != null) {
            try {
                getEditor(context).putString(KEY_SELECTED_WIKI_JSON, new Gson().toJson(wiki));
            } catch (JsonParseException e) {
                getEditor(context).putString(KEY_SELECTED_WIKI_JSON, "");
            }
        } else {
            getEditor(context).putString(KEY_SELECTED_WIKI_JSON, "");
        }

    }

    @Nullable
    public static WikiDetail getSelectedWiki(Context context) {
        String json = getPrefs(context).getString(KEY_SELECTED_WIKI_JSON, "");
        Gson gson = new Gson();
        try {
            return gson.fromJson(json, WikiDetail.class);
        } catch (JsonParseException e) {
            return null;
        }
    }
}
