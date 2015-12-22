package com.example.constantine.shoppinglist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.constantine.shoppinglist.R;
import com.example.constantine.shoppinglist.model.Purchase;

import java.util.ArrayList;

public class PurchaseListAdapter extends ArrayAdapter<Purchase>{
    private ArrayList<Purchase> purchaseList;

    public PurchaseListAdapter(Context context, ArrayList<Purchase> purchaseList) {
        super(context, R.layout.list_view, purchaseList);
        this.purchaseList = purchaseList;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_view, parent, false);
        }

        Purchase currentPurchase = purchaseList.get(position);

        TextView name = (TextView)view.findViewById(R.id.txtSumPurchases);
        name.setText(currentPurchase.getName());
        TextView price = (TextView)view.findViewById(R.id.price);
        price.setText(String.valueOf(currentPurchase.getPrice()));

        return view;
    }
}
