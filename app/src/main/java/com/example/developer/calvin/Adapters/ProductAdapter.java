package com.example.developer.calvin.Adapters;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.developer.calvin.Models.Home;
import com.example.developer.calvin.Models.Product;
import com.example.developer.calvin.ProductEditActivity;
import com.example.developer.calvin.R;
import com.example.developer.calvin.TransactionEditActivity;
import com.example.developer.calvin.ViewProductsActivity;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by Developer on 1/10/2019.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{
    private Context mCtx;
    private List<Product> productList;
    String current_date,Expiry_date;

    public ProductAdapter (Context mCtx, List<Product> productList){
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.product_cardview, parent, false);

        return new ProductViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.id = product.getProductid();
        holder.textViewProductName.setText(product.getProduct_name());
        holder.textViewStock.setText(product.getNumber_of_items());
        holder.textViewPrice.setText(product.getPrice_per_commodity() + "Ksh");
        holder.textViewDate.setText(product.getReceived_date());

        Expiry_date = product.getExpiry_date();
        current_date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        try {
            //Dates to compare
            String CurrentDate=  Expiry_date;
            String FinalDate =  current_date;

            Date date1;
            Date date2;

            SimpleDateFormat dates = new SimpleDateFormat("dd/MM/yyyy");

            //Setting dates
            date1 = dates.parse(CurrentDate);
            date2 = dates.parse(FinalDate);

            //Comparing dates
            long difference = Math.abs(date1.getTime() - date2.getTime());
            long differenceDates = difference / (24 * 60 * 60 * 1000);

            //Convert long to String
            String dayDifference = Long.toString(differenceDates);
            holder.difference.setText(dayDifference);

            if (differenceDates >= 14){
                holder.difference.setBackgroundColor(Color.GREEN);
            }else if (differenceDates >= 5){
                holder.difference.setBackgroundColor(Color.YELLOW);
            }else if (differenceDates <7){
                holder.difference.setBackgroundColor(Color.RED);
                addNotification();
            }
            Log.e("HERE","HERE: " + dayDifference);

        } catch (Exception exception) {
            Log.e("DIDN'T WORK", "exception " + exception);
        }



    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addNotification() {

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(mCtx)
                        .setSmallIcon(R.drawable.ic_menu_cart)
                        .setContentTitle("Expiry Alert")
                        .setVibrate(new long[]{1000,1000,1000,1000,1000})
                        .setContentText("You have items in stock that are about to expire");

        Intent notificationIntent = new Intent(mCtx, ViewProductsActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(mCtx, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private int id;
        TextView textViewStock, textViewProductName, textViewPrice,textViewDate,difference;
        public ProductViewHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            textViewProductName = itemView.findViewById(R.id.textViewProductName);
            textViewStock = itemView.findViewById(R.id.textViewStock);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            difference = itemView.findViewById(R.id.difference);

        }
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mCtx, ProductEditActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("product_id",id);
            mCtx.startActivity(intent);
        }
    }
}
