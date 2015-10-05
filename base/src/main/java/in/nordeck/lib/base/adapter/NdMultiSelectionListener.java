package in.nordeck.lib.base.adapter;

import java.util.ArrayList;

public interface NdMultiSelectionListener<T> {

    void toggleSelection(int pos);

    void clearSelection();

    int getSelectedItemCount();

    ArrayList<T> getSelectedItems();

}
