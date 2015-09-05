package com.nordeck.wiki.reader.adapters.base;

import java.util.ArrayList;

public interface NdMultiSelectionListener<T> {

    public void toggleSelection(int pos);

    public void clearSelection();

    public int getSelectedItemCount();

    public ArrayList<T> getSelectedItems();

}
