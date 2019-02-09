package com.example.developer.calvin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.developer.calvin.Models.Home;
import com.example.developer.calvin.Models.Purchase;
import com.example.developer.calvin.R;
import com.example.developer.calvin.TransactionEditActivity;

import java.util.List;

/**
 * Created by Developer on 1/10/2019.
 */

public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.HomeViewHolder>{
    private Context mCtx;
    private List<Purchase> transactionList;
    String Id;

    public PurchaseAdapter (Context mCtx, List<Purchase> transactionList){
        this.mCtx = mCtx;
        this.transactionList = transactionList;
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.purchase_cardview, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeViewHolder holder, int position) {
        Purchase purchase = transactionList.get(position);

        holder.id = purchase.getProductid();
        holder.textViewName.setText(purchase.getProduct_name());
        holder.textViewDate.setText(purchase.getReceived_date());
        holder.textViewBuying.setText(purchase.getBuying_price());
        holder.textViewSelling.setText(purchase.getSelling_price());

    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    class HomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private int id;
        TextView textViewName,textViewDate,textViewBuying,textViewSelling;
        public HomeViewHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewBuying = itemView.findViewById(R.id.textViewBuying);
            textViewSelling = itemView.findViewById(R.id.textViewSelling);

        }
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mCtx,TransactionEditActivity.class);
            intent.putExtra("purchase_id", id);
            mCtx.startActivity(intent);
        }
    }
}
