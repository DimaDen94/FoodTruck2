package com.truck.food.activities;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.SQLException;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.truck.food.App;
import com.truck.food.adapters.AdapterCart;
import com.truck.food.Constant;
import com.truck.food.DBHelper;
import com.truck.food.R;
import com.truck.food.model.tax.*;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityCart extends Activity {

    private ListView listOrder;
    private ProgressBar prgLoading;
    private TextView txtTotalLabel, txtTotal, txtAlert;
    private Button btnClear, Checkout;
    private RelativeLayout lytOrder;

    // declate dbhelper and adapter objects
    private DBHelper dbhelper;
    private AdapterCart mola;


    // declare static variables to store tax and currency data
    static double Tax;
    public static String Currency;

    // declare arraylist variable to store data
    ArrayList<ArrayList<Object>> data;
    private ArrayList<Integer> menuId = new ArrayList<Integer>();
    private ArrayList<String> menuName = new ArrayList<String>();
    private ArrayList<Integer> quantity = new ArrayList<Integer>();
    private ArrayList<Double> subTotalPrice = new ArrayList<Double>();

    double Total_price;
    final int CLEAR_ALL_ORDER = 0;
    final int CLEAR_ONE_ORDER = 1;
    int FLAG;
    int ID;

    // create price format
    DecimalFormat formatData = new DecimalFormat("#.##");

    PDTax pData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.your_order);

        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.header)));
        bar.setTitle("Мои заказы");
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);

        // connect view objects with xml id
//        imgNavBack = (ImageButton) findViewById(R.id.imgNavBack);
        Checkout = (Button) findViewById(R.id.Checkout);
        prgLoading = (ProgressBar) findViewById(R.id.prgLoading);
        listOrder = (ListView) findViewById(R.id.listOrder);
        txtTotalLabel = (TextView) findViewById(R.id.txtTotalLabel);
        txtTotal = (TextView) findViewById(R.id.txtTotal);
        txtAlert = (TextView) findViewById(R.id.txtAlert);
        btnClear = (Button) findViewById(R.id.btnClear);
        lytOrder = (RelativeLayout) findViewById(R.id.lytOrder);



        dbhelper = new DBHelper(this);

        // open database
        try {
            dbhelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }

        // call asynctask class to request tax and currency data from server
        retrofitRun();
        // event listener to handle clear button when clicked
        btnClear.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                // show confirmation dialog
                showClearDialog(CLEAR_ALL_ORDER, 1111);
            }
        });

        // event listener to handle list when clicked
        listOrder.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                // show confirmation dialog
                showClearDialog(CLEAR_ONE_ORDER, menuId.get(position));
            }
        });


        Checkout.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                // close database and back to previous page
                dbhelper.close();
                Intent iReservation = new Intent(ActivityCart.this, ActivityCheckout.class);
                startActivity(iReservation);
                overridePendingTransition(R.anim.open_next, R.anim.close_next);
            }
        });

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
                    mola = new AdapterCart(ActivityCart.this, menuId,menuName,quantity,subTotalPrice);
                    Tax = Double.parseDouble(pData.getData().get(0).getTaxOnCurrency().getValue());
                    Currency = pData.getData().get(1).getTaxOnCurrency().getValue();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case android.R.id.home:
                // app icon in action bar clicked; go home
                this.finish();
                overridePendingTransition(R.anim.open_main, R.anim.close_next);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // method to create dialog
    void showClearDialog(int flag, int id) {
        FLAG = flag;
        ID = id;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.confirm);
        switch (FLAG) {
            case 0:
                builder.setMessage(getString(R.string.clear_all_order));
                break;
            case 1:
                builder.setMessage(getString(R.string.clear_one_order));
                break;
        }
        builder.setCancelable(false);
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                switch (FLAG) {
                    case 0:
                        // clear all menu in order table
                        dbhelper.deleteAllData();
                        listOrder.invalidateViews();
                        clearData();
                        new getDataTask().execute();
                        break;
                    case 1:
                        // clear selected menu in order table
                        dbhelper.deleteData(ID);
                        listOrder.invalidateViews();
                        clearData();
                        new getDataTask().execute();
                        break;
                }

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

    // clear arraylist variables before used
    void clearData() {
        menuId.clear();
        menuName.clear();
        quantity.clear();
        subTotalPrice.clear();
    }

    // asynctask class to handle parsing json in background
    public class getDataTask extends AsyncTask<Void, Void, Void> {

        // show progressbar first
        getDataTask() {
            if (!prgLoading.isShown()) {
                prgLoading.setVisibility(View.VISIBLE);
                lytOrder.setVisibility(View.GONE);
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
            txtTotal.setText(Total_price + " " + Currency);
            txtTotalLabel.setText(getString(R.string.total_order) + " (Tax " + Tax + "%)");
            prgLoading.setVisibility(View.GONE);
            // if data available show data on list
            // otherwise, show alert text
            if (menuId.size() > 0) {
                lytOrder.setVisibility(View.VISIBLE);
                listOrder.setAdapter(mola);
            } else {
                txtAlert.setVisibility(View.VISIBLE);
            }

        }
    }

    // method to get data from server
    public void getDataFromDatabase() {

        Total_price = 0;
        clearData();
        data = dbhelper.getAllData();

        // store data to arraylist variables
        for (int i = 0; i < data.size(); i++) {
            ArrayList<Object> row = data.get(i);

            menuId.add(Integer.parseInt(row.get(0).toString()));
            menuName.add(row.get(1).toString());
            quantity.add(Integer.parseInt(row.get(2).toString()));
            subTotalPrice.add(Double.parseDouble(formatData.format(Double.parseDouble(row.get(3).toString()))));
            Total_price += subTotalPrice.get(i);
        }

        // count total order
        Total_price -= (Total_price * (Tax / 100));
        Total_price = Double.parseDouble(formatData.format(Total_price));
    }

    // when back button pressed close database and back to previous page
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        dbhelper.close();
        finish();
        overridePendingTransition(R.anim.open_main, R.anim.close_next);
    }


    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        // Ignore orientation change to keep activity from restarting
        super.onConfigurationChanged(newConfig);
    }


}
