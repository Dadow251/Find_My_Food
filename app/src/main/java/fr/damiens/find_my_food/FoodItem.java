package fr.damiens.find_my_food;

import static android.content.ContentValues.TAG;

import android.util.Log;

public class FoodItem {

    private String description;
    private double price;
    private String market;

    public FoodItem(String description, double price, String market){
        this.description = description;
        this.price = price;
        this.market = market;
    }

    public String getDescription(){
        return this.description;
    }

    public double getPrice() {
        return price;
    }

    public String getMarket() {
        return market;
    }

    public void print(){
        Log.d(TAG, "///" + description + ", " + price + ", " + market);
    }

    public String toString(){
        return description + " - " + price + " - " + market;
    }
}
