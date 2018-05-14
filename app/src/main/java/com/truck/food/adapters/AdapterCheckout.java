package com.truck.food.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.truck.food.R;
import com.truck.food.db.DishDB;

import java.util.List;

// adapter class for custom order list
public class AdapterCheckout extends RecyclerView.Adapter<AdapterCheckout.MyViewHolder> {
    private List<DishDB> dishDBs;
    private String currency;

    public AdapterCheckout(List<DishDB> dishDBs, String currency) {
        this.dishDBs = dishDBs;
        this.currency = currency;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView countXPrice;
        public TextView titleOrder;

        public MyViewHolder(View view) {
            super(view);
            countXPrice = (TextView) view.findViewById(R.id.txtCountXPrice);
            titleOrder = (TextView) view.findViewById(R.id.txtTitle);
        }
    }


    @Override
    public AdapterCheckout.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.checkout_item, parent, false);

        return new AdapterCheckout.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AdapterCheckout.MyViewHolder holder, final int position) {

        final DishDB dishDB = dishDBs.get(position);

        holder.titleOrder.setText(dishDB.getMenuName());
        holder.countXPrice.setText(dishDB.getCount() + " x " + dishDB.getPrice() + " " + currency);

    }

    @Override
    public int getItemCount() {
        return dishDBs.size();
    }

}