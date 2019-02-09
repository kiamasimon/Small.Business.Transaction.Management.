package com.example.developer.calvin.Adapters;

/**
 * Created by Developer on 1/11/2019.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.developer.calvin.Models.Spinner1;
import com.example.developer.calvin.R;

import java.util.List;
public class SpinnerAdapter1 extends BaseAdapter{
    private LayoutInflater layoutInflater;
    private List<Spinner1> listData;
    private Context context;
    public SpinnerAdapter1(Context context, List<Spinner1> listData) {
        this.context = context;
        layoutInflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listData = listData;
    }
    @Override
    public int getCount() {
        return listData.size();
    }
    @Override
    public Object getItem(int position) {
        return (Spinner1)listData.get(position);
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder spinnerHolder;
        if(convertView == null){
            spinnerHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.spinner_rowitem, parent, false);
            spinnerHolder.spinnerItemList = (TextView)convertView.findViewById(R.id.spinner_list_item);
            convertView.setTag(spinnerHolder);
        }else{
            spinnerHolder = (ViewHolder)convertView.getTag();
        }
        spinnerHolder.spinnerItemList.setText(listData.get(position).getCustomer_name());
        spinnerHolder.Id = listData.get(position).getCustomerid();
        return convertView;
    }
    class ViewHolder{
        TextView spinnerItemList;
        int Id;
    }
}
