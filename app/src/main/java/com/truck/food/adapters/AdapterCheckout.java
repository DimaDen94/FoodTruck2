package com.truck.food.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.truck.food.R;
import com.truck.food.db.Dish;

import java.util.List;

// adapter class for custom order list
public class AdapterCheckout extends RecyclerView.Adapter<AdapterCheckout.MyViewHolder> {
    private List<Dish> dishes;
    private String currency;

    public AdapterCheckout(List<Dish> dishes, String currency) {
        this.dishes = dishes;
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

        final Dish dish = dishes.get(position);

        holder.titleOrder.setText(dish.getMenuName());
        holder.countXPrice.setText(dish.getCount() + " x " + dish.getPrice() + " " + currency);

    }

    @Override
    public int getItemCount() {
        return dishes.size();
    }

}