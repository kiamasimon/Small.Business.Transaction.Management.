package com.example.developer.calvin.Models;

import android.app.Activity;

/**
 * Created by Developer on 1/11/2019.
 */

public class Spinner1 extends Activity {
    private int customerid;
    private String customer_name;


    public Spinner1(int customerid, String customer_name) {
        this.customerid = customerid;
        this.customer_name = customer_name;
    }

    public int getCustomerid() {
        return customerid;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomerid(int customerid) {
        this.customerid = customerid;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }
}
