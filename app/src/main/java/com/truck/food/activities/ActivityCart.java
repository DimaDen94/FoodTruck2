package com.truck.food.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
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
import android.widget.TextView;

import com.truck.food.R;
import com.truck.food.SugarHelper;
import com.truck.food.adapters.AdapterCart;
import com.truck.food.db.DishDB;

import java.util.List;

public class ActivityCart extends AppCompatActivity {
    private GridLayoutManager mLayoutManager;
    private RecyclerView listOrder;
    private ProgressBar prgLoading;
    private TextView txtTotalLabel, txtTotal, txtAlert;
    private FloatingActionButton toCheckout;
    private List<DishDB> dishDBs;
    private AdapterCart adapterCart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_layout);

        //it's sugar, baby
        dishDBs = DishDB.listAll(DishDB.class);
        initToolbar();
        initViews();
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
        adapterCart = new AdapterCart(ActivityCart.this, dishDBs, txtTotal, getString(R.string.currency));



        txtTotal.setText(SugarHelper.getTotal()+ " " + getString(R.string.currency));
        txtTotalLabel.setText(getString(R.string.total_order));
        prgLoading.setVisibility(View.GONE);
        // if data available show data on list
        // otherwise, show alert text
        if (dishDBs.size() > 0) {
            listOrder.setVisibility(View.VISIBLE);
            listOrder.setAdapter(adapterCart);
        } else {
            txtAlert.setVisibility(View.VISIBLE);
        }
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

    // method to create dialog
    void showClearDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertDialogCustom);
        builder.setTitle(R.string.confirm);


        builder.setMessage(getString(R.string.clear_all_order));

        builder.setCancelable(false);
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

                DishDB.deleteAll(DishDB.class);
                dishDBs.clear();
                listOrder.invalidate();
                adapterCart.notifyDataSetChanged();
                txtTotal.setText(SugarHelper.getTotal()+ " " + getString(R.string.currency));

            }
        });

        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                // close dialog
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

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
}
