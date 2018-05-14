package com.truck.food.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.truck.food.App;
import com.truck.food.SugarHelper;
import com.truck.food.Constant;
import com.truck.food.R;
import com.truck.food.adapters.AdapterCategoryList;

import com.truck.food.model.category.PDCategory;

import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityCategoryList extends AppCompatActivity {
    private RecyclerView listCategory;
    private ProgressBar prgLoading;
    private TextView txtAlert;

    // declare adapter object to create custom category list
    private AdapterCategoryList adapterCategoryList;
    private GridLayoutManager mLayoutManager;
    private Toolbar toolbar;
    private TextView notifCount;
    private ImageView notifImg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_list);

        initViews();
        initToolbar();
        retrofitRun();

    }

    private void initViews() {
        prgLoading = (ProgressBar) findViewById(R.id.prgLoading);
        listCategory = (RecyclerView) findViewById(R.id.category_recycler_view);
        txtAlert = (TextView) findViewById(R.id.txtAlert);
        mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        listCategory.setLayoutManager(mLayoutManager);
        listCategory.setHasFixedSize(true);
        //listCategory.setItemAnimator(new DefaultItemAnimator());
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.category_list_title);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        final MenuItem menuItem = menu.findItem(R.id.action_cart);
        View actionView = MenuItemCompat.getActionView(menuItem);
        notifCount = (TextView) actionView.findViewById(R.id.cart_badge);
        notifImg = (ImageView) actionView.findViewById(R.id.cart_img);
        notifCount.setText(String.valueOf(SugarHelper.getDishCount()));
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iMyOrder = new Intent(ActivityCategoryList.this, ActivityCart.class);
                startActivity(iMyOrder);
                overridePendingTransition(R.anim.open_next, R.anim.close_next);
            }
        });
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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

                    adapterCategoryList = new AdapterCategoryList(ActivityCategoryList.this, response.body());
                    listCategory.setVisibility(View.VISIBLE);
                    listCategory.setAdapter(adapterCategoryList);

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
    protected void onDestroy() {
        // TODO Auto-generated method stub
        //adapterCategoryList.imageLoader.clearCache();
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

    @Override
    protected void onStart() {
        super.onStart();
        if (notifCount != null)
            notifCount.setText(String.valueOf(SugarHelper.getDishCount()));
    }
}
