package com.example.ptmanagment.component;

public class Order {
    private String meal;
    private String rest;
    private String drink;

    //region Constructors
    public Order( String meal, String rest, String drink) {
        this.meal = meal;
        this.rest = rest;
        this.drink = drink;
    }

    public Order() {
    }
    //endregion

    //region Getters Setters
    public String getMeal() {
        return meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }

    public String getRest() {
        return rest;
    }

    public void setRest(String rest) {
        this.rest = rest;
    }

    public String getDrink() {
        return drink;
    }

    public void setDrink(String drink) {
        this.drink = drink;
    }

    //endregion

    //region Methods


    //endregion
}
