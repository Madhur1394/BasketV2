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

/**
 * Created by goku on 5/5/17.
 */

public class MyViewHolder extends RecyclerView.Adapter<BasketAdapter> {



    public Context context;
    public ArrayList<Basket> basketArrayList;
    int position;



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
    public void onBindViewHolder(BasketAdapter holder, int position) {
        this.position = position;

        holder.basketName.setText(basketArrayList.get(position).getBasketTitle());
        holder.basketDes.setText(basketArrayList.get(position).getBasketDesciption());
        holder.basketCost.setText(String.valueOf(basketArrayList.get(position).getBasketCost()));

        holder.imageButtonMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);

            }
        });

    }
    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_item, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyViewHolder.MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.itemDel:
                    removeAt(position);
                    Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.itemEdit:
                    Toast.makeText(context, "Edit", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    private void removeAt(int position) {
        basketArrayList.remove(position);
        notifyItemRemoved(position);
        // notifyItemRangeChanged(position, getItemCount());
    }



    @Override
    public int getItemCount() {
        return basketArrayList.size();
    }
}
