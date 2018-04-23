package com.truck.food.activities;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.SQLException;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.truck.food.App;
import com.truck.food.Constant;
import com.truck.food.DBHelper;
import com.truck.food.R;
import com.truck.food.model.Order;
import com.truck.food.model.tax.PDTax;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//import android.widget.CheckBox;

public class ActivityCheckout extends FragmentActivity {

    private Button btnSend;

    private EditText edtFName, edtLName, edtPhone, edtOrderList, edtComment, edtEmail;
    private ScrollView sclDetail;
    private ProgressBar prgLoading;
    private TextView txtAlert;
    private Spinner spinFacility;

    // declare dbhelper object
    private DBHelper dbhelper;
    ArrayList<ArrayList<Object>> data;


    // declare static variables to store tax and currency data
    private static double Tax;
    private static String Currency;


    // create price format
    private DecimalFormat formatData = new DecimalFormat("#.##");

    private String Result;
    private PDTax pdTax;
    private Order order;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout);

        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.primary_dark)));
        bar.setTitle("Информация о клиенте");
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);

        edtFName = (EditText) findViewById(R.id.edtFName);
        edtLName = (EditText) findViewById(R.id.edtLName);
        edtPhone = (EditText) findViewById(R.id.edtPhone);
        spinFacility = (Spinner) findViewById(R.id.spinner);

        edtOrderList = (EditText) findViewById(R.id.edtOrderList);

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtComment = (EditText) findViewById(R.id.edtComment);

        btnSend = (Button) findViewById(R.id.btnSend);
        sclDetail = (ScrollView) findViewById(R.id.sclDetail);
        prgLoading = (ProgressBar) findViewById(R.id.prgLoading);
        txtAlert = (TextView) findViewById(R.id.txtAlert);


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.facilities, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinFacility.setAdapter(adapter);

        order = new Order();
        dbhelper = new DBHelper(this);
        // open database
        try {
            dbhelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }


        retrofitRun();


        // event listener to handle send button when pressed
        btnSend.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                ProgressDialog dialog;
                dialog = ProgressDialog.show(ActivityCheckout.this, "",
                        getString(R.string.sending_alert), true);

                getOrder();

                if (order.getFacility().equalsIgnoreCase("") ||
                        order.getfName().equalsIgnoreCase("") ||
                        order.getlName().equalsIgnoreCase("") ||
                        order.getPhone().equalsIgnoreCase("")) {
                    Toast.makeText(ActivityCheckout.this, R.string.form_alert, Toast.LENGTH_SHORT).show();
                } else if ((data.size() == 0)) {
                    Toast.makeText(ActivityCheckout.this, R.string.order_alert, Toast.LENGTH_SHORT).show();
                } else {

                    App.getApi().sendData(getMap()).enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            Result = response.body().toString();
                            resultAlert(Result);
                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {
                            Toast.makeText(ActivityCheckout.this, R.string.failed_alert, Toast.LENGTH_SHORT).show();
                        }
                    });

                    dialog.dismiss();
                }
            }
        });
    }

    private Map<String, String> getMap() {
        String date = new SimpleDateFormat("yyyy.MM.dd  hh:mm:ss").format(Calendar.getInstance().getTime());
        Map<String, String> map = new HashMap<String, String>();
        map.put("facility", order.getFacility());
        map.put("fname", order.getfName());
        map.put("lname", order.getlName());
        map.put("date_n_time", date);
        map.put("phone", order.getPhone());
        map.put("order_list", order.getOrderList());
        map.put("comment", order.getComment());
        map.put("email", order.getEmail());
        return map;
    }

    private void getOrder() {
        order.setFacility(spinFacility.getSelectedItem().toString());
        order.setfName(edtFName.getText().toString());
        order.setlName(edtLName.getText().toString());
        order.setPhone(edtPhone.getText().toString());
        order.setOrderList(edtOrderList.getText().toString());
        order.setComment(edtComment.getText().toString());
        order.setEmail(edtEmail.getText().toString());
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

    void retrofitRun() {
        Map<String, String> map = new HashMap<String, String>();
        map.put(Constant.AccessKeyParam, Constant.AccessKeyValue);

        prgLoading.setVisibility(View.VISIBLE);
        txtAlert.setVisibility(View.GONE);

        App.getApi().getTaxCurrency(map).enqueue(new Callback<PDTax>() {
            @Override
            public void onResponse(Call<PDTax> call, Response<PDTax> response) {
                //Данные успешно пришли, но надо проверить response.body() на null
                prgLoading.setVisibility(View.GONE);

                // if internet connection and data available show data on list
                // otherwise, show alert text
                if (response.isSuccessful()) {
                    pdTax = response.body();
                    Tax = Double.parseDouble(pdTax.getData().get(0).getTaxOnCurrency().getValue());
                    Currency = pdTax.getData().get(1).getTaxOnCurrency().getValue();
                    new getDataTask().execute();
                } else {
                    txtAlert.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<PDTax> call, Throwable t) {

            }
        });
    }


    // asynctask class to get data from database in background
    public class getDataTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            getDataFromDatabase();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            // hide progressbar and show reservation form
            prgLoading.setVisibility(View.GONE);
            sclDetail.setVisibility(View.VISIBLE);

        }
    }


    // method to show toast message
    public void resultAlert(String HasilProses) {
        if (HasilProses.trim().equalsIgnoreCase("OK")) {
            Toast.makeText(ActivityCheckout.this, R.string.ok_alert, Toast.LENGTH_SHORT).show();
            Intent i = new Intent(ActivityCheckout.this, ActivityConfirmMessage.class);
            startActivity(i);
            overridePendingTransition(R.anim.open_next, R.anim.close_next);
            finish();
        } else if (HasilProses.trim().equalsIgnoreCase("Failed")) {
            Toast.makeText(ActivityCheckout.this, R.string.failed_alert, Toast.LENGTH_SHORT).show();
        } else {
            Log.d("HasilProses", HasilProses);
        }
    }

    // method to post data to server


    // method to get data from database
    public void getDataFromDatabase() {

        data = dbhelper.getAllData();

        double Order_price = 0;
        double Total_price = 0;
        double tax = 0;

        // store all data to variables
        for (int i = 0; i < data.size(); i++) {
            ArrayList<Object> row = data.get(i);

            String Menu_name = row.get(1).toString();
            String Quantity = row.get(2).toString();
            double Sub_total_price = Double.parseDouble(formatData.format(Double.parseDouble(row.get(3).toString())));
            Order_price += Sub_total_price;

            // calculate order price
            order.setOrderList("" + Quantity + " " + Menu_name + " " + Sub_total_price + " " + Currency + ",\n");
        }

        if (order.getOrderList()==null||order.getOrderList().equalsIgnoreCase("")) {
            order.setOrderList(getString(R.string.no_order_menu));
        }

        tax = Double.parseDouble(formatData.format(Order_price * (Tax / 100)));
        Total_price = Double.parseDouble(formatData.format(Order_price - tax));
        order.setOrderList(order.getOrderList() + "\nЗаказ: " + Order_price + " " + Currency +
                "\nИтого: " + Total_price + " " + Currency);
        edtOrderList.setText(order.getOrderList());
    }

    // method to format date
    private static String pad(int c) {
        if (c >= 10) {
            return String.valueOf(c);
        } else {
            return "0" + String.valueOf(c);
        }
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
