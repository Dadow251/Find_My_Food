package fr.damiens.find_my_food;

public class FoodItem {

    String description;
    double price;
    String market;

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
}
