package com.example.developer.calvin.Models;

import java.text.DecimalFormat;

public class Cart {
    private int cartID;
    private int userID;
    private int productID;
    private String productTitle;
    private double productPrice;
    private String image;
    private int quantity;
    private int orderedQuantity;
    DecimalFormat df = new DecimalFormat("#0.00");

    public Cart(int cartID, int userID, int productID, String productTitle, double productPrice, String image, int quantity, int orderedQuantity) {
        this.cartID = cartID;
        this.userID = userID;
        this.productID = productID;
        this.productTitle = productTitle;
        this.productPrice = productPrice;
        this.image = image;
        this.quantity = quantity;
        this.orderedQuantity = orderedQuantity;
    }

    public int getCartID() {
        return cartID;
    }

    public void setCartID(int cartID) {
        this.cartID = cartID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public String getProductPriceS(){
        return df.format(productPrice);
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getOrderedQuantity() {
        return orderedQuantity;
    }

    public void setOrderedQuantity(int orderedQuantity) {
        this.orderedQuantity = orderedQuantity;
    }
}
