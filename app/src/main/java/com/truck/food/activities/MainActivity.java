package com.truck.food.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.truck.food.App;
import com.truck.food.Constant;
import com.truck.food.GridViewItem;
import com.truck.food.R;
import com.truck.food.adapters.AdapterGridView;
import com.truck.food.db.DishDB;
import com.truck.food.db.FacilitiesDB;
import com.truck.food.db.UserDB;
import com.truck.food.model.Dish;
import com.truck.food.model.Facilities;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("NewApi")
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private GridView gridview;
    private AdapterGridView gridviewAdapter;
    private ArrayList<GridViewItem> data = new ArrayList<GridViewItem>();
    TextView name;
    TextView email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        initToolbar();
        initNavigationView();

        // get screen device width and height
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        // checking internet connection
        if (!Constant.isNetworkAvailable(MainActivity.this)) {
            Toast.makeText(MainActivity.this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }

        initGridView();
        checkNotification();
        loadObjects();
    }

    private void checkNotification() {
        // TODO Auto-generated method stub

        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.notification_layout);
        final TextView textView = (TextView) findViewById(R.id.notification_tv);

        App.getApi().checkNotification().enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.body().equals("")) {
                    linearLayout.setVisibility(View.GONE);
                } else {
                    linearLayout.setVisibility(View.VISIBLE);
                    textView.setText(response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                linearLayout.setVisibility(View.GONE);
            }
        });

    }

    private void initGridView() {
        gridview = (GridView) findViewById(R.id.gridView1);
        gridview.setOnItemClickListener(this);
        data.add(new GridViewItem(getResources().getString(R.string.menu_product), getResources().getDrawable(R.drawable.ic_product)));
        data.add(new GridViewItem(getResources().getString(R.string.menu_cart), getResources().getDrawable(R.drawable.ic_cart)));
        data.add(new GridViewItem(getResources().getString(R.string.menu_checkout), getResources().getDrawable(R.drawable.ic_checkout)));
        data.add(new GridViewItem(getResources().getString(R.string.menu_profile), getResources().getDrawable(R.drawable.ic_info)));
        gridviewAdapter = new AdapterGridView(this, R.layout.main_grid_item, data);
        gridview.setAdapter(gridviewAdapter);
    }


    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
    }

    private void initNavigationView() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.view_navigation_open, R.string.view_navigation_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);

        View header = navigationView.getHeaderView(0);
        name = (TextView) header.findViewById(R.id.nav_name);
        email = (TextView) header.findViewById(R.id.nav_phone_number);

        setLastUser();


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                drawer.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        break;
                    case R.id.product:
                        startActivity(new Intent(MainActivity.this, ActivityCategoryList.class));
                        overridePendingTransition(R.anim.open_next, R.anim.close_next);
                        break;
                    case R.id.cart:
                        startActivity(new Intent(MainActivity.this, ActivityCart.class));
                        overridePendingTransition(R.anim.open_next, R.anim.close_next);
                        break;
                    case R.id.checkout:
                        startActivity(new Intent(MainActivity.this, ActivityCheckout.class));
                        overridePendingTransition(R.anim.open_next, R.anim.close_next);
                        break;
                    case R.id.about:
                        startActivity(new Intent(MainActivity.this, ActivityAbout.class));
                        overridePendingTransition(R.anim.open_next, R.anim.close_next);
                        break;
                    case R.id.contact:
                        startActivity(new Intent(MainActivity.this, ActivityContactUs.class));
                        overridePendingTransition(R.anim.open_next, R.anim.close_next);
                        break;
                    case R.id.exit:
                        finish();
                        overridePendingTransition(R.anim.open_next, R.anim.close_next);
                        break;
                }
                return true;
            }
        });
    }

    private void setLastUser() {
        List<UserDB> userDBs = UserDB.listAll(UserDB.class);
        if (userDBs.size() != 0) {
            for (UserDB userDB : userDBs) {
                if (userDB.isLast()) {
                    name.setText(userDB.getfName());
                    email.setText(userDB.getPhoneNumber());
                    break;
                }
            }
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            startActivity(new Intent(this, ActivityCategoryList.class));
            overridePendingTransition(R.anim.open_next, R.anim.close_next);
        } else if (position == 1) {
            startActivity(new Intent(this, ActivityCart.class));
            overridePendingTransition(R.anim.open_next, R.anim.close_next);
        } else if (position == 2) {
            startActivity(new Intent(this, ActivityCheckout.class));
            overridePendingTransition(R.anim.open_next, R.anim.close_next);
        } else if (position == 3) {
            startActivity(new Intent(this, ActivityAbout.class));
            overridePendingTransition(R.anim.open_next, R.anim.close_next);
        }
    }

    private void loadObjects() {

        App.getApi().getObjects().enqueue(new Callback<Facilities>() {
            @Override
            public void onResponse(Call<Facilities> call, Response<Facilities> response) {
                if (response.isSuccessful()) {
                    String[] facilities = response.body().getObjects();
                    FacilitiesDB.deleteAll(FacilitiesDB.class);
                    for (String f : facilities) {
                        FacilitiesDB fac = new FacilitiesDB(f);
                        FacilitiesDB.save(fac);
                    }

                } else {

                }
            }

            @Override
            public void onFailure(Call<Facilities> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        setLastUser();
    }
}
