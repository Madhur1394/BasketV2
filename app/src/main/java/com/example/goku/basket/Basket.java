package com.example.goku.basket;

import java.io.Serializable;
import java.util.List;

/**
 * Created by goku on 5/5/17.
 */

public class Basket implements Serializable {
    public String basketTitle;
    public String basketDesciption;
    public String basketId;
    public int basketCost;
    public List<ItemList> itemLists;

    public String getBasketId() {
        return basketId;
    }

    public void setBasketId(String basketId) {
        this.basketId = basketId;
    }




    public Basket() {
    }

    public Basket(String basketTitle, String basketDesciption, int basketCost, List<ItemList> itemLists) {

        this.basketTitle = basketTitle;
        this.basketDesciption = basketDesciption;
        this.basketCost = basketCost;
        this.itemLists = itemLists;
    }

    public String getBasketTitle() {
        return basketTitle;
    }

    public void setBasketTitle(String basketTitle) {
        this.basketTitle = basketTitle;
    }

    public String getBasketDesciption() {
        return basketDesciption;
    }

    public void setBasketDesciption(String basketDesciption) {
        this.basketDesciption = basketDesciption;
    }

    public int getBasketCost() {
        return basketCost;
    }

    public void setBasketCost(int basketCost) {
        this.basketCost = basketCost;
    }

    public List<ItemList> getItemLists() {
        return itemLists;
    }

    public void setItemLists(List<ItemList> itemLists) {
        this.itemLists = itemLists;
    }
}