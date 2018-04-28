package com.truck.food.activities;

import java.util.HashMap;
import java.util.Map;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.truck.food.App;
import com.truck.food.adapters.AdapterMenuList;
import com.truck.food.Constant;
import com.truck.food.R;
import com.truck.food.model.menu.PDMenu;
import com.truck.food.model.tax.PDTax;



import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityMenuList extends AppCompatActivity {
	
	private RecyclerView listMenu;
	private ProgressBar prgLoading;
	private TextView txtAlert;
	private Toolbar toolbar;
	// declare static variable to store tax and currency symbol
	private static String Currency;

	private AdapterMenuList mla;

	private int Category_ID;

	private PDTax pdTax;

    @Override
    public void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_list);

        initViews();
		initToolbar();

        Intent iGet = getIntent();
        Category_ID = iGet.getIntExtra("category_id",0);
		// call asynctask class to request tax and currency data from server

		retrofitGetTaxAndGetMenuRun();
    }

	private void initToolbar() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle(R.string.menu_list_title);
		setSupportActionBar(toolbar);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem menuItem) {
				Intent iMyOrder = new Intent(ActivityMenuList.this, ActivityCart.class);
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

	private void initViews() {
		prgLoading = (ProgressBar) findViewById(R.id.prgLoading);
		listMenu = (RecyclerView) findViewById(R.id.menu_recycler_view);
		txtAlert = (TextView) findViewById(R.id.txtAlert);
		GridLayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
		listMenu.setLayoutManager(mLayoutManager);
		listMenu.setItemAnimator(new DefaultItemAnimator());
	}

	void retrofitGetTaxAndGetMenuRun() {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constant.AccessKeyParam, Constant.AccessKeyValue);

		prgLoading.setVisibility(View.VISIBLE);
		txtAlert.setVisibility(View.GONE);

		App.getApi().getTaxCurrency(map).enqueue(new Callback<PDTax>() {
			@Override
			public void onResponse(Call<PDTax> call, Response<PDTax> response) {
				prgLoading.setVisibility(View.GONE);
				if (response.isSuccessful()) {
					pdTax = response.body();
					//Tax = Double.valueOf(pdTax.getData().get(0).getTaxOnCurrency().getValue());
					Currency = pdTax.getData().get(1).getTaxOnCurrency().getValue();
					if((Currency != null)){
						retrofitGetMenuRun();
					}else{
						txtAlert.setVisibility(View.VISIBLE);
					}
				} else {
					txtAlert.setVisibility(View.GONE);
				}
			}

			@Override
			public void onFailure(Call<PDTax> call, Throwable t) {
				txtAlert.setVisibility(View.VISIBLE);
			}
		});
	}
	void retrofitGetMenuRun() {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constant.AccessKeyParam, Constant.AccessKeyValue);
		map.put("category_id", String.valueOf(Category_ID));

		prgLoading.setVisibility(View.VISIBLE);
		txtAlert.setVisibility(View.GONE);

		App.getApi().getMenu(map).enqueue(new Callback<PDMenu>() {
			@Override
			public void onResponse(Call<PDMenu> call, Response<PDMenu> response) {
				//Данные успешно пришли, но надо проверить response.body() на null
				prgLoading.setVisibility(View.GONE);

				// if internet connection and data available show data on list
				// otherwise, show alert text
				if (response.isSuccessful()) {
					mla = new AdapterMenuList(ActivityMenuList.this,response.body(),Currency);
					listMenu.setVisibility(View.VISIBLE);
					listMenu.setAdapter(mla);

				} else {
					txtAlert.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onFailure(Call<PDMenu> call, Throwable t) {

			}
		});
	}

    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	//mla.imageLoader.clearCache();
    	listMenu.setAdapter(null);
    	super.onDestroy();
    }

    @Override
	public void onConfigurationChanged(final Configuration newConfig)
	{
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
}
