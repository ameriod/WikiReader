package com.nordeck.wiki.reader.presenters;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface NdPresenter<V extends NdView> {

    void bindView(V view);

    void unbindView();

    void onDestroy();

    void onCreate(@Nullable Bundle bundle);

    void onSaveInstanceState(@NonNull Bundle bundle);

}