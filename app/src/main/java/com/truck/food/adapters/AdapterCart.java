package com.truck.food.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.truck.food.Constant;
import com.truck.food.R;
import com.truck.food.db.DishDB;

import java.util.List;

// adapter class for custom order list
public class AdapterCart extends RecyclerView.Adapter<AdapterCart.MyViewHolder> {
    private List<DishDB> dishDBs;
    private Context context;
    private TextView txtTotal;
    private String currency;

    public AdapterCart(Context context, List<DishDB> dishDBs, TextView txtTotal, String currency) {
        this.context = context;
        this.dishDBs = dishDBs;
        this.txtTotal = txtTotal;
        this.currency = currency;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;
        public Button plus;
        public Button min;
        public TextView priceOrder;
        public TextView titleOrder;
        public TextView dCount;

        public MyViewHolder(View view) {
            super(view);
            img = (ImageView) view.findViewById(R.id.photo_order);
            plus = (Button) view.findViewById(R.id.btn_i_count);
            min = (Button) view.findViewById(R.id.btn_d_count);
            priceOrder = (TextView) view.findViewById(R.id.price_order);
            titleOrder = (TextView) view.findViewById(R.id.title_order);
            dCount = (TextView) view.findViewById(R.id.d_count);
        }
    }


    @Override
    public AdapterCart.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item, parent, false);

        return new AdapterCart.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AdapterCart.MyViewHolder holder, final int position) {

        final DishDB dishDB = dishDBs.get(position);

        Glide.with(context)
                .load(Constant.AdminPageURL + dishDB.getMenuImage())
                .thumbnail(0.01f)
                .crossFade()
                .into(holder.img);


        holder.titleOrder.setText(dishDB.getMenuName());
        holder.priceOrder.setText(dishDB.getPrice() + " " + currency);
        holder.dCount.setText(String.valueOf(dishDB.getCount()));

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dishDB.getCount() < 9) {
                    dishDB.setCount(dishDB.getCount() + 1);
                    dishDB.save();
                    txtTotal.setText(getTotal() + " " + currency);
                    notifyDataSetChanged();
                }
            }
        });
        holder.min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dishDB.getCount() > 1) {
                    dishDB.setCount(dishDB.getCount() - 1);
                    dishDB.save();
                    txtTotal.setText(getTotal() + " " + currency);
                    notifyDataSetChanged();
                }else {

                    dishDBs.remove(dishDB);
                    dishDB.delete();
                    //notifyDataSetChanged();
                    txtTotal.setText(getTotal() + " " + currency);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, dishDBs.size());

                }
            }
        });

    }
    int getTotal(){
        int t = 0;
        // store data to arraylist variables
        for (int i = 0; i < dishDBs.size(); i++) {
            DishDB dishDB = dishDBs.get(i);
            t += Double.parseDouble(dishDB.getPrice())* dishDB.getCount();
        }
        return t;
    }

    @Override
    public int getItemCount() {
        return dishDBs.size();
    }

}