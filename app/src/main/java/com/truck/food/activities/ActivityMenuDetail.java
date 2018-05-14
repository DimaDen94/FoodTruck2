package com.truck.food.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.truck.food.App;
import com.truck.food.SugarHelper;
import com.truck.food.Constant;
import com.truck.food.R;
import com.truck.food.db.DishDB;
import com.truck.food.model.menu_detail.MenuDetail;
import com.truck.food.model.menu_detail.PDMenuDatail;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ActivityMenuDetail extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView imgPreview;
    private Button btnPlus;
    private Button btnMin;
    private TextView txtSubText, txtDescription, txtNumbeOfServings;
    private FloatingActionButton btnAdd;
    private LinearLayout sclDetail;
    private ProgressBar prgLoading;
    private TextView txtAlert;

    private int numberOfServings;

    // declare variables to store menu data
    private int Menu_ID;
    private MenuDetail menuDetail;

    // create price format
    private DecimalFormat formatData = new DecimalFormat("#.##");
    private TextView notifCount;
    private ImageView notifImg;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dish_detail);
        //initToolbar();
        // get menu id that sent from previous page
        Intent iGet = getIntent();
        Menu_ID = iGet.getIntExtra("menu_id", 0);
        initViews();
        retrofitRun();
    }


    private void initToolbar(String menuName) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(menuName);
        toolbar.setTitleTextColor(getResources().getColor(R.color.cardview_light_background));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void initViews() {
        numberOfServings = 1;
        txtNumbeOfServings = (TextView) findViewById(R.id.d_count);
        txtNumbeOfServings.setText(String.valueOf(numberOfServings));

        imgPreview = (ImageView) findViewById(R.id.backdrop);
        txtSubText = (TextView) findViewById(R.id.txtSubText);
        txtDescription = (TextView) findViewById(R.id.txtDescription);

        btnAdd = (FloatingActionButton) findViewById(R.id.btnAdd);
        btnPlus = (Button) findViewById(R.id.btn_i_count);
        btnMin = (Button) findViewById(R.id.btn_d_count);

        sclDetail = (LinearLayout) findViewById(R.id.lytContent);
        prgLoading = (ProgressBar) findViewById(R.id.prgLoading);
        txtAlert = (TextView) findViewById(R.id.txtAlert);


        final View content = findViewById(R.id.coordinator);
        // event listener to handle add button when clicked
        btnAdd.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                // show input dialog
                if (Integer.parseInt(menuDetail.getQuantity()) >= numberOfServings) {
                    DishDB dishDB = new DishDB(menuDetail.getDescription(),
                            menuDetail.getServeFor(),
                            menuDetail.getQuantity(),
                            menuDetail.getMenuImage(),
                            menuDetail.getMenuId(),
                            menuDetail.getPrice(),
                            menuDetail.getMenuName(),
                            numberOfServings);
                    dishDB.save();
                    Snackbar snackbar = Snackbar.make(content, "Ваш заказ добавлен в корзину", Snackbar.LENGTH_SHORT);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(getResources().getColor(R.color.primary_dark));
                    snackbar.show();
                    notifCount.setText(String.valueOf(SugarHelper.getDishCount()));

                } else {
                    Snackbar snackbar = Snackbar.make(content, "У нас нет столько порций", Snackbar.LENGTH_SHORT);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(getResources().getColor(R.color.primary_dark));
                    snackbar.show();
                }
            }
        });

        btnPlus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                numberOfServings++;
                txtNumbeOfServings.setText(String.valueOf(numberOfServings));
            }
        });
        btnMin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numberOfServings > 1) {
                    numberOfServings--;
                    txtNumbeOfServings.setText(String.valueOf(numberOfServings));
                }
            }
        });
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
                Intent iMyOrder = new Intent(ActivityMenuDetail.this, ActivityCart.class);
                startActivity(iMyOrder);
                overridePendingTransition(R.anim.open_next, R.anim.close_next);
            }
        });



        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    void retrofitRun() {
        Map<String, String> map = new HashMap<String, String>();
        map.put(Constant.AccessKeyParam, Constant.AccessKeyValue);
        map.put("menu_id", String.valueOf(Menu_ID));

        prgLoading.setVisibility(View.VISIBLE);
        txtAlert.setVisibility(View.GONE);

        App.getApi().getMenuDetail(map).enqueue(new Callback<PDMenuDatail>() {
            @Override
            public void onResponse(Call<PDMenuDatail> call, Response<PDMenuDatail> response) {
                //Данные успешно пришли, но надо проверить response.body() на null
                prgLoading.setVisibility(View.GONE);

                // if internet connection and data available show data on list
                // otherwise, show alert text
                if (response.isSuccessful()) {

                    menuDetail = response.body().getData().get(0).getMenuDetail();

                    double price = Double.valueOf(menuDetail.getPrice());

                    sclDetail.setVisibility(View.VISIBLE);

                    Glide.with(ActivityMenuDetail.this)
                            .load(Constant.AdminPageURL + menuDetail.getMenuImage())
                            .thumbnail(0.01f)
                            .crossFade()
                            .into(imgPreview);

                    //imageLoader.DisplayImage(Constant.AdminPageURL + menuDetail.getMenuImage(), imgPreview);
                    initToolbar(menuDetail.getMenuName());

                    txtSubText.setText("Цена : " + formatData.format(price) + " " + getString(R.string.currency) + "\n" + "Осталось : " + menuDetail.getQuantity() + " порций");
                    txtDescription.setText(Html.fromHtml(menuDetail.getDescription()).toString());

                } else {
                    txtAlert.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<PDMenuDatail> call, Throwable t) {

            }
        });
    }

    // close database before back to previous page
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.open_main, R.anim.close_next);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        //imageLoader.clearCache();
        super.onDestroy();
    }


    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        // Ignore orientation change to keep activity from restarting
        super.onConfigurationChanged(newConfig);
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (notifCount != null)
            notifCount.setText(String.valueOf(SugarHelper.getDishCount()));
    }
}
