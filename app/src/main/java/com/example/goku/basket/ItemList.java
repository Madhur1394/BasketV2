package com.example.goku.basket;

/**
 * Created by goku on 4/5/17.
 */

public class ItemList {
    private String ItemName;
    private int ItemQuan,ItemCost;

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public int getItemQuan() {
        return ItemQuan;
    }

    public void setItemQuan(int itemQuan) {
        ItemQuan = itemQuan;
    }

    public int getItemCost() {
        return ItemCost;
    }

    public void setItemCost(int itemCost) {
        ItemCost = itemCost;
    }

    public ItemList() {

    }

    public ItemList(String itemName, int itemQuan, int itemCost) {
        ItemName = itemName;
        ItemQuan = itemQuan;
        ItemCost = itemCost;
    }
}
