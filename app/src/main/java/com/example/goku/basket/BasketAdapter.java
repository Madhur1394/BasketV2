package com.example.goku.basket;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by goku on 5/5/17.
 */

public class BasketAdapter extends RecyclerView.ViewHolder {

    public TextView basketName,basketDes,basketCost;

    public ImageButton imageButtonMore;

    public BasketAdapter(View itemView) {
        super(itemView);
        basketName=(TextView) itemView.findViewById(R.id.textViewBasketTitle);
        basketDes=(TextView) itemView.findViewById(R.id.textViewBasketDescription);
        basketCost=(TextView) itemView.findViewById(R.id.textViewBasketCost);
        imageButtonMore = (ImageButton) itemView.findViewById(R.id.imageButtonMore_1);
    }






}
