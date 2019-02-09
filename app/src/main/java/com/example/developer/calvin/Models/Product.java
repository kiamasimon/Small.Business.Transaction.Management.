package com.example.developer.calvin.Models;

/**
 * Created by Developer on 1/10/2019.
 */

public class Product {
    private int productid;
    private String product_name;
    private String received_date;
    private String expiry_date;
    private String number_of_items;
    private String price_per_commodity;
    private String sellingprice;

    public Product(int productid,  String product_name, String received_date, String expiry_date, String number_of_items, String price_per_commodity, String sellingprice){
        this.productid = productid;
        this.product_name = product_name;
        this.received_date = received_date;
        this.expiry_date = expiry_date;
        this.number_of_items = number_of_items;
        this.price_per_commodity = price_per_commodity;
        this.sellingprice = sellingprice;
    }

    public int getProductid() {
        return productid;
    }

    public String getNumber_of_items() {
        return number_of_items;
    }

    public String getPrice_per_commodity() {
        return price_per_commodity;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getReceived_date() {
        return received_date;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    public String getSellingprice() {
        return sellingprice;
    }
}
