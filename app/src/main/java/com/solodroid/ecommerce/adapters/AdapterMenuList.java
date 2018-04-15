package com.solodroid.ecommerce.adapters;

import android.app.Activity;
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
import com.solodroid.ecommerce.activities.ActivityMenuList;
import com.solodroid.ecommerce.model.menu.PDMenu;

// adapter class fro custom menu list
public class AdapterMenuList extends BaseAdapter {

    private Context context;
    public ImageLoader imageLoader;
    PDMenu menu;
    private String currency;

    public AdapterMenuList(Context context, PDMenu menu, String currency) {
        this.context = context;
        imageLoader = new ImageLoader(context);
        this.menu = menu;
        this.currency = currency;

    }

    public int getCount() {
        // TODO Auto-generated method stub
        return menu.getData().size();
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
            convertView = inflater.inflate(R.layout.menu_list_item, null);
            holder = new ViewHolder();

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtText = (TextView) convertView.findViewById(R.id.txtText);
        holder.txtSubText = (TextView) convertView.findViewById(R.id.txtSubText);
        holder.imgThumb = (ImageView) convertView.findViewById(R.id.imgThumb);

        holder.txtText.setText(menu.getData().get(position).getMenu().getMenuName());
        holder.txtSubText.setText(menu.getData().get(position).getMenu().getPrice() + " " + currency);

        imageLoader.DisplayImage(Constant.AdminPageURL + menu.getData().get(position).getMenu().getMenuImage(), holder.imgThumb);

        return convertView;
    }

    static class ViewHolder {
        TextView txtText, txtSubText;
        ImageView imgThumb;
    }


}