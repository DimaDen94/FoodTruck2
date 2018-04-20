package com.truck.food.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.truck.food.Constant;
import com.truck.food.ImageLoader;
import com.truck.food.R;
import com.truck.food.model.category.PDCategory;

// adapter class for custom category list
public class AdapterCategoryList extends BaseAdapter {

    private Context context;
    private ImageLoader imageLoader;
    private PDCategory pDataCategory;

    public AdapterCategoryList(Context context, PDCategory pDataCategory) {
        this.context = context;
        this.pDataCategory = pDataCategory;
        imageLoader = new ImageLoader(context);
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return pDataCategory.getData().size();
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
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.category_list_item, null);
            holder = new ViewHolder();

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.txtText = (TextView) convertView.findViewById(R.id.txtText);
        holder.imgThumb = (ImageView) convertView.findViewById(R.id.imgThumb);


        imageLoader.DisplayImage(Constant.AdminPageURL + pDataCategory.getData().get(position).getCategory().getCategoryImage(), holder.imgThumb);

        return convertView;
    }

    static class ViewHolder {
        TextView txtText;
        ImageView imgThumb;
    }


}