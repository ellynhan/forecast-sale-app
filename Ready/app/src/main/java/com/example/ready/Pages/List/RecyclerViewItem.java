package com.example.ready.Pages.List;

public class RecyclerViewItem {
    String menuName;
    int quantity;
    public RecyclerViewItem(String name, int nums){
        this.menuName = name;
        this.quantity = nums;
    }
    public String getMenuName(){
        return menuName;
    }
    public int getQuantity(){
        return quantity;
    }
}
