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
import com.truck.food.activities.ActivityMenuList;
import com.truck.food.model.category.PDCategory;

// adapter class for custom category list
public class AdapterCategoryList extends RecyclerView.Adapter<AdapterCategoryList.MyViewHolder> {

    private Context context;
    private PDCategory pDataCategory;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        public TextView title;

        public MyViewHolder(View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.imgThumb);
            title = (TextView) view.findViewById(R.id.txtText);

        }
    }

    public AdapterCategoryList(Context context, PDCategory pDataCategory) {
        this.context = context;
        this.pDataCategory = pDataCategory;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent iMenuList = new Intent(context, ActivityMenuList.class);
                iMenuList.putExtra("category_id", pDataCategory.getData().get(position).getCategory().getCategoryId());
                Activity activity = (Activity) context;
                context.startActivity(iMenuList);
                activity.overridePendingTransition(R.anim.open_next, R.anim.close_next);
            }
        });


        Glide.with(context)
                .load(Constant.AdminPageURL + pDataCategory.getData().get(position).getCategory().getCategoryImage())
                .thumbnail(0.01f)
                .crossFade()
                .into(holder.thumbnail);

        holder.title.setText(pDataCategory.getData().get(position).getCategory().getCategoryName());

    }

    @Override
    public int getItemCount() {
        return pDataCategory.getData().size();
    }


}