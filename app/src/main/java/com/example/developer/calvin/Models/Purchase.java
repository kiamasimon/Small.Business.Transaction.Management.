package com.example.developer.calvin.Models;

public class Purchase {
    private int productid;
    private String product_name;
    private String received_date;
    private String buying_price;
    private String selling_price;

    public Purchase(int productid,String product_name, String received_date, String buying_price, String selling_price){
        this.productid = productid;
        this.product_name = product_name;
        this.received_date = received_date;
        this.buying_price = buying_price;
        this.selling_price = selling_price;
    }

    public int getProductid() {
        return productid;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getReceived_date() {
        return received_date;
    }

    public String getBuying_price() {
        return buying_price;
    }

    public String getSelling_price() {
        return selling_price;
    }
}
