package com.example.goku.basket;

/**
 * Created by goku on 9/5/17.
 */

public interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
