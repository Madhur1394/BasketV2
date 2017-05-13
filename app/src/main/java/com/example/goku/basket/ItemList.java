package com.example.goku.basket;

/**
 * Created by goku on 4/5/17.
 */

public class ItemList {
    private String ItemName;
    private int ItemQuan;
    private int ItemCost;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    private int itemId;

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

    public ItemList(int itemId, String itemName, int itemQuan, int itemCost) {
        this.itemId = itemId;
        ItemName = itemName;
        ItemQuan = itemQuan;
        ItemCost = itemCost;
    }
}