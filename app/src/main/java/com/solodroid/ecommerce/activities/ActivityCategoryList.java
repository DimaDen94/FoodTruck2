package com.solodroid.ecommerce.activities;

import java.util.HashMap;
import java.util.Map;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.solodroid.ecommerce.adapters.AdapterCategoryList;
import com.solodroid.ecommerce.App;
import com.solodroid.ecommerce.Constant;
import com.solodroid.ecommerce.R;
import com.solodroid.ecommerce.model.category.PDCategory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityCategoryList extends Activity {
    GridView listCategory;
    ProgressBar prgLoading;
    TextView txtAlert;

    // declare adapter object to create custom category list
    AdapterCategoryList cla;

    PDCategory pDataCategory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_list);

        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.header)));
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);
        bar.setTitle("Меню");

        prgLoading = (ProgressBar) findViewById(R.id.prgLoading);
        listCategory = (GridView) findViewById(R.id.listCategory);
        txtAlert = (TextView) findViewById(R.id.txtAlert);


        retrofitRun();


        // event listener to handle list when clicked
        listCategory.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                // TODO Auto-generated method stub
                // go to menu page
                Intent iMenuList = new Intent(ActivityCategoryList.this, ActivityMenuList.class);
                iMenuList.putExtra("category_id", pDataCategory.getData().get(position).getCategory().getCategoryId());

                startActivity(iMenuList);
                overridePendingTransition(R.anim.open_next, R.anim.close_next);
            }
        });

    }

    void retrofitRun() {
        Map<String, String> map = new HashMap<String, String>();
        map.put(Constant.AccessKeyParam, Constant.AccessKeyValue);

        prgLoading.setVisibility(0);
        txtAlert.setVisibility(8);

        App.getApi().getAllCategoryData(map).enqueue(new Callback<PDCategory>() {
            @Override
            public void onResponse(Call<PDCategory> call, Response<PDCategory> response) {
                //Данные успешно пришли, но надо проверить response.body() на null
                prgLoading.setVisibility(8);

                // if internet connection and data available show data on list
                // otherwise, show alert text
                if (response.isSuccessful()) {
                    pDataCategory = response.body();
                    cla = new AdapterCategoryList(ActivityCategoryList.this, response.body());
                    listCategory.setVisibility(0);
                    listCategory.setAdapter(cla);

                } else {
                    txtAlert.setVisibility(0);
                }
            }

            @Override
            public void onFailure(Call<PDCategory> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.cart:
                // refresh action
                Intent iMyOrder = new Intent(ActivityCategoryList.this, ActivityCart.class);
                startActivity(iMyOrder);
                overridePendingTransition(R.anim.open_next, R.anim.close_next);
                return true;

            case R.id.refresh:
                listCategory.invalidateViews();
                clearData();
                retrofitRun();
                return true;

            case android.R.id.home:
                // app icon in action bar clicked; go home
                this.finish();
                overridePendingTransition(R.anim.open_main, R.anim.close_next);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // clear arraylist variables before used
    void clearData() {

    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        //cla.imageLoader.clearCache();
        listCategory.setAdapter(null);
        super.onDestroy();
    }


    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        // Ignore orientation change to keep activity from restarting
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.open_main, R.anim.close_next);
    }

}
