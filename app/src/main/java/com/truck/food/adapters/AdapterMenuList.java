package com.truck.food.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.truck.food.Constant;
import com.truck.food.R;
import com.truck.food.activities.ActivityMenuDetail;
import com.truck.food.model.menu.PDMenu;

// adapter class fro custom menu list
public class AdapterMenuList extends RecyclerView.Adapter<AdapterMenuList.ViewHolder> {

    private Context context;
    private PDMenu menu;
    private String currency;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView menuImageView;
        public TextView txtTitle;
        public TextView txtPrice;

        public ViewHolder(View view) {
            super(view);
            menuImageView = (ImageView) view.findViewById(R.id.imgMenu);
            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            txtPrice = (TextView) view.findViewById(R.id.txtPrice);

        }
    }

    public AdapterMenuList(Context context, PDMenu menu, String currency) {
        this.context = context;
        this.menu = menu;
        this.currency = currency;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_list_item, parent, false);

        return new ViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.menuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent iDetail = new Intent(context, ActivityMenuDetail.class);
                iDetail.putExtra("menu_id", Integer.parseInt(menu.getData().get(position).getMenu().getMenuId()));
                iDetail.putExtra("currency", currency);
                context.startActivity(iDetail);
                Activity activity = (Activity) context;
                activity.overridePendingTransition(R.anim.open_next, R.anim.close_next);

            }
        });


        holder.txtTitle.setText(menu.getData().get(position).getMenu().getMenuName());
        holder.txtPrice.setText(menu.getData().get(position).getMenu().getPrice());
        Glide.with(context)
                .load(Constant.AdminPageURL + menu.getData().get(position).getMenu().getMenuImage())
                .thumbnail(0.01f)
                .crossFade()
                .into(holder.menuImageView);



    }

    @Override
    public int getItemCount()
    {
        try {
            return menu.getData().size();
        }catch (NullPointerException e){

        }
        return 0;
    }


}