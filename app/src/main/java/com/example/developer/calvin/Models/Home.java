package com.example.developer.calvin.Models;

/**
 * Created by Developer on 1/10/2019.
 */

public class Home {
    private int transactionid;
    private String amount;
    private String comment;
    private String customer;

    public Home(int transactionid,String comment, String amount, String customer){
        this.transactionid = transactionid;
        this.amount = amount;
        this.comment = comment;
        this.customer = customer;
    }

    public int getTransactionid() {
        return transactionid;
    }

    public String getAmount() {
        return amount;
    }

    public String getCustomer() {
        return customer;
    }

    public String getComment() {
        return comment;
    }
}
