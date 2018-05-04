package com.truck.food.activities;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.truck.food.App;
import com.truck.food.adapters.AdapterCart;
import com.truck.food.Constant;

import com.truck.food.R;
import com.truck.food.db.Dish;
import com.truck.food.model.tax.*;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityCart extends AppCompatActivity {
    private GridLayoutManager mLayoutManager;
    private RecyclerView listOrder;
    private ProgressBar prgLoading;
    private TextView txtTotalLabel, txtTotal, txtAlert;
    private FloatingActionButton toCheckout;


    // declate dbhelper and adapter objects
    private List<Dish> dishes;
    private AdapterCart mola;

    // declare static variable
    public static String Currency;

    double Total_price;

    // create price format
    DecimalFormat formatData = new DecimalFormat("#.##");

    PDTax pData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_layout);

        initToolbar();
        initViews();

        //it's sugar, baby
        dishes = Dish.listAll(Dish.class);

        // call asynctask class to request tax and currency data from server
        retrofitRun();
        // event listener to handle clear button when clicked

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.menu_cart);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                showClearDialog();
                return true;

            }
        });
    }

    private void initViews() {
        mLayoutManager = new GridLayoutManager(this, 1);
        // connect view objects with xml id
//        imgNavBack = (ImageButton) findViewById(R.id.imgNavBack);
        toCheckout = (FloatingActionButton) findViewById(R.id.btnToCheckout);
        prgLoading = (ProgressBar) findViewById(R.id.prgLoading);
        listOrder = (RecyclerView) findViewById(R.id.listOrder);
        txtTotalLabel = (TextView) findViewById(R.id.txtTotalLabel);
        txtTotal = (TextView) findViewById(R.id.txtTotal);
        txtAlert = (TextView) findViewById(R.id.txtAlert);


        listOrder.setLayoutManager(mLayoutManager);
        listOrder.setItemAnimator(new DefaultItemAnimator());


        toCheckout.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                // close database and back to previous page
                Intent iReservation = new Intent(ActivityCart.this, ActivityCheckout.class);
                startActivity(iReservation);
                overridePendingTransition(R.anim.open_next, R.anim.close_next);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cart, menu);
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

        App.getApi().getTaxCurrency(map).enqueue(new Callback<PDTax>() {
            @Override
            public void onResponse(Call<PDTax> call, Response<PDTax> response) {
                prgLoading.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    pData = response.body();
                    Currency = pData.getData().get(1).getTaxOnCurrency().getValue();
                    mola = new AdapterCart(ActivityCart.this, dishes, txtTotal, Currency);
                    new getDataTask().execute();
                } else {
                    txtAlert.setVisibility(View.VISIBLE);
                    txtAlert.setText(R.string.alert);
                }
            }

            @Override
            public void onFailure(Call<PDTax> call, Throwable t) {
                txtAlert.setVisibility(View.VISIBLE);
                txtAlert.setText(R.string.alert);
            }
        });
    }

    // method to create dialog
    void showClearDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.confirm);

        builder.setMessage(getString(R.string.clear_all_order));

        builder.setCancelable(false);
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

                Dish.deleteAll(Dish.class);
                listOrder.invalidate();
                //new getDataTask().execute();


            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                // close dialog
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }


    // asynctask class to handle parsing json in background
    public class getDataTask extends AsyncTask<Void, Void, Void> {

        // show progressbar first
        getDataTask() {
            if (!prgLoading.isShown()) {
                prgLoading.setVisibility(View.VISIBLE);
                listOrder.setVisibility(View.GONE);
                txtAlert.setVisibility(View.GONE);
            }
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            // get data from database
            getDataFromDatabase();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            // show data
            txtTotal.setText(getTotal() + " " + Currency);
            txtTotalLabel.setText(getString(R.string.total_order));
            prgLoading.setVisibility(View.GONE);
            // if data available show data on list
            // otherwise, show alert text
            if (dishes.size() > 0) {
                listOrder.setVisibility(View.VISIBLE);
                listOrder.setAdapter(mola);
            } else {
                txtAlert.setVisibility(View.VISIBLE);
            }

        }
    }

    // method to get data from server
    public void getDataFromDatabase() {
        Total_price = 0;
        // store data to arraylist variables
        for (int i = 0; i < dishes.size(); i++) {
            Dish dish = dishes.get(i);
            Total_price += Double.parseDouble(dish.getPrice()) * dish.getCount();
        }
        // count total order
        Total_price -= (Total_price);
        Total_price = Double.parseDouble(formatData.format(Total_price));
    }

    // when back button pressed close database and back to previous page
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        Intent intent = new Intent(ActivityCart.this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.open_main, R.anim.close_next);
    }


    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        // Ignore orientation change to keep activity from restarting
        super.onConfigurationChanged(newConfig);
    }


    int getTotal(){
        int t = 0;
        // store data to arraylist variables
        for (int i = 0; i < dishes.size(); i++) {
            Dish dish = dishes.get(i);
            t += Double.parseDouble(dish.getPrice())*dish.getCount();
        }
        return t;
    }
}
