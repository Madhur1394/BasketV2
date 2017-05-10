package com.example.goku.basket;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by goku on 5/5/17.
 */

public class MyViewHolder extends RecyclerView.Adapter<BasketAdapter> implements ItemTouchHelperAdapter {



    public Context context;
    public ArrayList<Basket> basketArrayList;
    static int position;



    public MyViewHolder(Context context, ArrayList<Basket> basketArrayList) {
        this.context = context;
        this.basketArrayList = basketArrayList;
    }




    @Override
    public BasketAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.basket_list,parent,false);
        return new BasketAdapter(v);
    }

    @Override
    public void onBindViewHolder(final BasketAdapter holder, final int position) {


        holder.basketName.setText(basketArrayList.get(position).getBasketTitle());
        holder.basketDes.setText(basketArrayList.get(position).getBasketDesciption());
        holder.basketCost.setText(String.valueOf("Total cost: "+basketArrayList.get(position).getBasketCost()));
    }

    @Override
    public void onItemDismiss(int position) {
        Basket basket = basketArrayList.get(position);
        basketArrayList.remove(position);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("baskets").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(basket.getBasketId());
        databaseReference.removeValue();
        notifyItemRemoved(position);
        notifyItemRangeChanged(0,getItemCount());

    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(basketArrayList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(basketArrayList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;

    }

    @Override
    public int getItemCount() {
        return basketArrayList.size();
    }
}