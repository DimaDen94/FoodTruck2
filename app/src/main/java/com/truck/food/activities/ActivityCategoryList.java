package com.truck.food.activities;

import java.util.HashMap;
import java.util.Map;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.truck.food.adapters.AdapterCategoryList;
import com.truck.food.App;
import com.truck.food.Constant;
import com.truck.food.R;
import com.truck.food.model.category.PDCategory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityCategoryList extends Activity {
    private RecyclerView listCategory;
    private ProgressBar prgLoading;
    private TextView txtAlert;

    // declare adapter object to create custom category list
    private AdapterCategoryList cla;
    private GridLayoutManager mLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_list);

        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.primary_dark)));
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);
        bar.setTitle("Меню");

        prgLoading = (ProgressBar) findViewById(R.id.prgLoading);
        listCategory = (RecyclerView) findViewById(R.id.category_recycler_view);
        txtAlert = (TextView) findViewById(R.id.txtAlert);
        mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        listCategory.setLayoutManager(mLayoutManager);
        listCategory.setItemAnimator(new DefaultItemAnimator());

        retrofitRun();


    }

    void retrofitRun() {
        Map<String, String> map = new HashMap<String, String>();
        map.put(Constant.AccessKeyParam, Constant.AccessKeyValue);

        prgLoading.setVisibility(View.VISIBLE);
        txtAlert.setVisibility(View.GONE);

        App.getApi().getAllCategoryData(map).enqueue(new Callback<PDCategory>() {
            @Override
            public void onResponse(Call<PDCategory> call, Response<PDCategory> response) {
                //Данные успешно пришли, но надо проверить response.body() на null
                prgLoading.setVisibility(View.GONE);

                // if internet connection and data available show data on list
                // otherwise, show alert text
                if (response.isSuccessful()) {

                    cla = new AdapterCategoryList(ActivityCategoryList.this, response.body());
                    listCategory.setVisibility(View.VISIBLE);
                    listCategory.setAdapter(cla);

                } else {
                    txtAlert.setVisibility(View.VISIBLE);
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
                listCategory.invalidate();
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
