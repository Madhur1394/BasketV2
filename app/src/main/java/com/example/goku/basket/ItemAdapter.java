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

import java.util.List;

/**
 * Created by goku on 4/5/17.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {


    private Context context;
    private ItemList item;
    private List<ItemList> itemList;
    private int position;
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView itemName,itemQuan,itemCost;
        public ImageButton imageButtonMore;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemName = (TextView) itemView.findViewById(R.id.textViewItemTitle);
            itemQuan = (TextView) itemView.findViewById(R.id.textViewItemQuantity);
            itemCost = (TextView) itemView.findViewById(R.id.textViewItemCost);

            imageButtonMore = (ImageButton) itemView.findViewById(R.id.imageButtonMore);
        }
    }

    public ItemAdapter(Context context, List<ItemList> itemList){
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_item_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemAdapter.MyViewHolder holder, int position) {

        this.position = position;
        item = itemList.get(position);
        holder.itemName.setText(item.getItemName());
        holder.itemCost.setText("Total Cost : "+item.getItemCost() * item.getItemQuan() + " Rs.");
        holder.itemQuan.setText("Quantity : "+item.getItemQuan());

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
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
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
        itemList.remove(position);
        notifyItemRemoved(position);
       notifyItemRangeChanged(0, getItemCount());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}