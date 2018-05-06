package com.truck.food.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.truck.food.Constant;
import com.truck.food.GridViewItem;
import com.truck.food.R;
import com.truck.food.adapters.AdapterGridView;
import com.truck.food.db.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    }

    private void initGridView() {
        gridview = (GridView) findViewById(R.id.gridView1);
        gridview.setOnItemClickListener(this);
        data.add(new GridViewItem(getResources().getString(R.string.menu_product), getResources().getDrawable(R.drawable.ic_product)));
        data.add(new GridViewItem(getResources().getString(R.string.menu_cart), getResources().getDrawable(R.drawable.ic_cart)));
        data.add(new GridViewItem(getResources().getString(R.string.menu_checkout), getResources().getDrawable(R.drawable.ic_checkout)));
        data.add(new GridViewItem(getResources().getString(R.string.menu_profile), getResources().getDrawable(R.drawable.ic_profile)));
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

        View header=navigationView.getHeaderView(0);
/*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
        name = (TextView)header.findViewById(R.id.nav_name);
        email = (TextView)header.findViewById(R.id.nav_phone_number);

        setLastUser();


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                drawer.closeDrawers();
                switch (menuItem.getItemId()){
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
                        MainActivity.this.finish();
                        overridePendingTransition(R.anim.open_next, R.anim.close_next);
                        break;
                }
                return true;
            }
        });
    }

    private void setLastUser() {
        List<User> users = User.listAll(User.class);
        if (users.size() != 0) {
            for (User user : users) {
                if (user.isLast()) {
                    name.setText(user.getfName());
                    email.setText(user.getPhoneNumber());
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

    @Override
    protected void onStart() {
        super.onStart();
        setLastUser();
    }
}
