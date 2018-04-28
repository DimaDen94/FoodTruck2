package com.truck.food.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.truck.food.App;
import com.truck.food.Constant;
import com.truck.food.R;
import com.truck.food.db.Dish;
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
    private TextView txtText, txtSubText;
    private WebView txtDescription;
    private FloatingActionButton btnAdd;
    private LinearLayout sclDetail;
    private ProgressBar prgLoading;
    private TextView txtAlert;


    // declare variables to store menu data
    private int Menu_ID;
    private MenuDetail menuDetail;

    // create price format
    private DecimalFormat formatData = new DecimalFormat("#.##");
    private String Currency;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dish_detail);

        initToolbar();

        imgPreview = (ImageView) findViewById(R.id.imgPreview);
        txtText = (TextView) findViewById(R.id.txtText);
        txtSubText = (TextView) findViewById(R.id.txtSubText);
        txtDescription = (WebView) findViewById(R.id.txtDescription);
        btnAdd = (FloatingActionButton) findViewById(R.id.btnAdd);

        sclDetail = (LinearLayout) findViewById(R.id.lytContent);
        prgLoading = (ProgressBar) findViewById(R.id.prgLoading);
        txtAlert = (TextView) findViewById(R.id.txtAlert);

        // get screen device width and height
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int wPix = dm.widthPixels;
        int hPix = wPix / 2 + 50;

        // change menu image width and height
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(wPix, hPix);
        imgPreview.setLayoutParams(lp);


        // get menu id that sent from previous page
        Intent iGet = getIntent();
        Menu_ID = iGet.getIntExtra("menu_id", 0);
        Currency = iGet.getStringExtra("currency");

        // call asynctask class to request data from server
        //new getDataTask().execute();
        retrofitRun();

        // event listener to handle add button when clicked
        btnAdd.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                // show input dialog
                inputDialog();
            }
        });

    }


    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.menu_detail_title);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent iMyOrder = new Intent(ActivityMenuDetail.this, ActivityCart.class);
                startActivity(iMyOrder);
                overridePendingTransition(R.anim.open_next, R.anim.close_next);
                return true;

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    // method to show number of order form
    void inputDialog() {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(R.string.order);
        alert.setMessage(R.string.number_order);
        alert.setCancelable(false);
        final EditText edtQuantity = new EditText(this);
        int maxLength = 3;
        edtQuantity.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        edtQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(edtQuantity);

        alert.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String temp = edtQuantity.getText().toString();
                int quantity = 0;

                // when add button clicked add menu to order table in database
                if (!temp.equalsIgnoreCase("")) {
                    quantity = Integer.parseInt(temp);
                    if (Dish.findById(Dish.class, Menu_ID) != null) {
                        Dish dish = Dish.findById(Dish.class, Menu_ID);
                        dish.setCount(quantity);
                    } else {
                        Dish dish = new Dish(menuDetail.getDescription(),
                                menuDetail.getServeFor(),
                                menuDetail.getQuantity(),
                                menuDetail.getMenuImage(),
                                menuDetail.getMenuId(),
                                menuDetail.getPrice(),
                                menuDetail.getMenuName(),
                                quantity);
                        dish.save();
                    }
                } else {
                    dialog.cancel();
                }
            }
        });

        alert.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                // when cancel button clicked close dialog
                dialog.cancel();
            }
        });

        alert.show();
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

                    txtText.setText(menuDetail.getMenuName());
                    txtSubText.setText("Цена : " + formatData.format(price) + " " + Currency + "\n" + "Осталось : " + menuDetail.getQuantity() + " порций");
                    txtDescription.loadDataWithBaseURL("", menuDetail.getDescription(), "text/html", "UTF-8", "");

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


}
