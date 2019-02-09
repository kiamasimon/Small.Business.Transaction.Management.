package com.example.developer.calvin.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.developer.calvin.R;
import com.example.developer.calvin.ViewProductsActivity;

public class ProductFragment extends Fragment {

    Button bnViewAdd, bnViewView;

    public static ProductFragment newInstance() {
        ProductFragment fragment = new ProductFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(R.string.Product);
        View view = inflater.inflate(R.layout.fragment_product, container, false);


        bnViewView = view.findViewById(R.id.bnViewView);

        bnViewView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ViewProductsActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }

}