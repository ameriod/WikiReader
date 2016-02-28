package com.nordeck.wiki.reader.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nordeck.wiki.reader.SelectedWiki;
import com.nordeck.wiki.reader.ui.top.ActivityTopPages;
import com.nordeck.wiki.reader.ui.wiki.ActivityWikis;

/**
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
