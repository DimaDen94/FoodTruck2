package com.truck.food.activities;

import java.text.DecimalFormat;
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
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.truck.food.App;
import com.truck.food.Constant;
import com.truck.food.DBHelper;
import com.truck.food.ImageLoader;
import com.truck.food.R;

import com.truck.food.model.menu_detail.MenuDetail;
import com.truck.food.model.menu_detail.PDMenuDatail;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ActivityMenuDetail extends Activity {

    private ImageView imgPreview;
    private TextView txtText, txtSubText;
    private WebView txtDescription;
    private Button btnAdd;
    private ScrollView sclDetail;
    private ProgressBar prgLoading;
    private TextView txtAlert;

    // declare dbhelper object
    private DBHelper dbhelper;

    // declare ImageLoader object
    private ImageLoader imageLoader;

    // declare variables to store menu data
    private int Menu_ID;
    private MenuDetail menuDetail;

    // create price format
    private DecimalFormat formatData = new DecimalFormat("#.##");
    private String Currency;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_detail);

        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.header)));
        bar.setTitle("Описание блюда");
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);

        imgPreview = (ImageView) findViewById(R.id.imgPreview);
        txtText = (TextView) findViewById(R.id.txtText);
        txtSubText = (TextView) findViewById(R.id.txtSubText);
        txtDescription = (WebView) findViewById(R.id.txtDescription);
        btnAdd = (Button) findViewById(R.id.btnAdd);

        sclDetail = (ScrollView) findViewById(R.id.sclDetail);
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

        imageLoader = new ImageLoader(ActivityMenuDetail.this);
        dbhelper = new DBHelper(this);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
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
                Intent iMyOrder = new Intent(ActivityMenuDetail.this, ActivityCart.class);
                startActivity(iMyOrder);
                overridePendingTransition(R.anim.open_next, R.anim.close_next);
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

    // method to show number of order form
    void inputDialog() {

        // open database first
        try {
            dbhelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }

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
                    if (dbhelper.isDataExist(Menu_ID)) {
                        dbhelper.updateData(Menu_ID, quantity, (Double.parseDouble(menuDetail.getPrice()) * quantity));
                    } else {
                        dbhelper.addData(Menu_ID, menuDetail.getMenuName(), quantity, (Double.parseDouble(menuDetail.getPrice()) * quantity));
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

                    imageLoader.DisplayImage(Constant.AdminPageURL + menuDetail.getMenuImage(), imgPreview);

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
        dbhelper.close();
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
