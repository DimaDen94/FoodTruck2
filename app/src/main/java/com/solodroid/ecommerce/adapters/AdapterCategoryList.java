package com.solodroid.ecommerce.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.solodroid.ecommerce.Constant;
import com.solodroid.ecommerce.ImageLoader;
import com.solodroid.ecommerce.R;
import com.solodroid.ecommerce.model.PDataCategory;

// adapter class for custom category list
public class AdapterCategoryList extends BaseAdapter {

    private Context context;
    public ImageLoader imageLoader;
    PDataCategory pDataCategory;

    public AdapterCategoryList(Context context, PDataCategory pDataCategory) {
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