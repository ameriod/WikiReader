package com.nordeck.wiki.reader.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nordeck.wiki.reader.SelectedWiki;

/**
 * TODO add a splash screen image?
 * Created by parker on 9/7/15.
 */
public class ActivitySplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SelectedWiki.getInstance().getSelectedWiki() != null) {
            ActivityTopPages.launchActivity(this);
        } else {
            ActivityWikis.launchActivity(this);
        }
        overridePendingTransition(0, 0);
        finish();
    }
}
