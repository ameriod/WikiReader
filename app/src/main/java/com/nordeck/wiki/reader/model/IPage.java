package com.nordeck.wiki.reader.model;

import android.os.Parcelable;

/**
 * Created by parker on 9/5/15.
 */
public interface IPage extends Parcelable {

    String getTitle();

    String getId();

    String getImageUrl();

    String getUrl();

    String getSummary();
}
