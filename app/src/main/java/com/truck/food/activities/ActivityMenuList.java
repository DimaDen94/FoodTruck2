package com.truck.food.activities;

import java.util.HashMap;
import java.util.Map;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class ActivityMenuList extends Activity {
	
	private RecyclerView listMenu;
	private ProgressBar prgLoading;
	private EditText edtKeyword;
	private ImageButton btnSearch;
	private TextView txtAlert;
	
	// declare static variable to store tax and currency symbol
	private static String Currency;

	private AdapterMenuList mla;

	private int Category_ID;

	private PDTax pdTax;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_list);
        
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.primary_dark)));
        bar.setTitle("Меню");
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);
        prgLoading = (ProgressBar) findViewById(R.id.prgLoading);
        listMenu = (RecyclerView) findViewById(R.id.menu_recycler_view);
        edtKeyword = (EditText) findViewById(R.id.edtKeyword);
        btnSearch = (ImageButton) findViewById(R.id.btnSearch);
        txtAlert = (TextView) findViewById(R.id.txtAlert);


        Intent iGet = getIntent();
        Category_ID = iGet.getIntExtra("category_id",0);
// call asynctask class to request tax and currency data from server

		GridLayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);

		listMenu.setLayoutManager(mLayoutManager);
		listMenu.setItemAnimator(new DefaultItemAnimator());



		retrofitGetTaxAndGetMenuRun();

		// event listener to handle search button when clicked
		btnSearch.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {

				retrofitGetTaxAndGetMenuRun();
			}
		});
		

        
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_category, menu);
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
			Intent iMyOrder = new Intent(ActivityMenuList.this, ActivityCart.class);
			startActivity(iMyOrder);
			overridePendingTransition (R.anim.open_next, R.anim.close_next);
			return true;
			
		case R.id.refresh:
			listMenu.invalidate();

			retrofitGetTaxAndGetMenuRun();
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
