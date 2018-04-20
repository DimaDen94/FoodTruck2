package com.truck.food.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.truck.food.R;
import com.truck.food.activities.ActivityCart;


import java.util.ArrayList;

// adapter class for custom order list
public class AdapterCart extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<Integer> menuId;
    private ArrayList<String> menuName;
    private ArrayList<Integer> quantity;
    private ArrayList<Double> subTotalPrice;

    public AdapterCart(Context context, ArrayList<Integer> menuId, ArrayList<String> menuName, ArrayList<Integer> quantity, ArrayList<Double> subTotalPrice) {
        inflater = LayoutInflater.from(context);
        this.menuId = menuId;
        this.menuName = menuName;
        this.quantity = quantity;
        this.subTotalPrice = subTotalPrice;
    }



    public int getCount() {
        // TODO Auto-generated method stub
        return menuId.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.order_list_item, null);
            holder = new ViewHolder();
            holder.txtMenuName = (TextView) convertView.findViewById(R.id.txtMenuName);
            holder.txtQuantity = (TextView) convertView.findViewById(R.id.txtQuantity);
            holder.txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.txtMenuName.setText(menuName.get(position));
        holder.txtQuantity.setText(String.valueOf(quantity.get(position)));
        holder.txtPrice.setText(subTotalPrice.get(position) + " " + ActivityCart.Currency);

        return convertView;
    }

    static class ViewHolder {
        TextView txtMenuName, txtQuantity, txtPrice;
    }


}