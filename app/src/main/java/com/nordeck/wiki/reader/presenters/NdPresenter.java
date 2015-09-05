package com.nordeck.wiki.reader.presenters;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface NdPresenter<V extends NdView> {

    public void bindView(V view);

    public void unbindView();

    public void onDestroy();

    public void onCreate(@Nullable Bundle bundle);

    public void onSaveInstanceState(@NonNull Bundle bundle);

}