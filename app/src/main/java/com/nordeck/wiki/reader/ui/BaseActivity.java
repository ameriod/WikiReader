package com.nordeck.wiki.reader.ui;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by parker on 9/4/15.
 */
public class BaseActivity extends AppCompatActivity {

    protected void displayErrorMessage(@Nullable String message) {
        if (TextUtils.isEmpty(message)) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
