package com.example.constantine.shoppinglist.model;

public class Purchase {
    private String name;
    private double price;
    private int id;

    public Purchase(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getId() {
        return id;
    }
}
