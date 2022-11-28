package fr.damiens.find_my_food;

import static android.content.ContentValues.TAG;

import android.util.Log;

public class FoodItem {

    private String description;
    private double price;
    private String market;
    private String url;

    public FoodItem(){}

    public FoodItem(String description, double price, String market){
        this.description = description;
        this.price = price;
        this.market = market;
    }

    public FoodItem(String description, double price, String market, String url) {
        this.description = description;
        this.price = price;
        this.market = market;
        this.url = url;
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

    public String getUrl() {
        return url;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void print(){
        Log.d(TAG, "///" + description + ", " + price + ", " + market);
    }

    public String toString(){
        return description + "///" + price + "///" + market + "///" + url;
    }
}
