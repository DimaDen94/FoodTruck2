package com.solodroid.ecommerce.activities;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import com.solodroid.ecommerce.App;
import com.solodroid.ecommerce.adapters.AdapterMenuList;
import com.solodroid.ecommerce.Constant;
import com.solodroid.ecommerce.R;
import com.solodroid.ecommerce.model.menu.PDMenu;
import com.solodroid.ecommerce.model.tax.PDTax;



import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityMenuList extends Activity {
	
	private ListView listMenu;
	private ProgressBar prgLoading;
	private EditText edtKeyword;
	private ImageButton btnSearch;
	private TextView txtAlert;
	
	// declare static variable to store tax and currency symbol
	private static String Currency;

	private AdapterMenuList mla;
	

	


	private int Category_ID;


	private PDTax pdTax;
	private PDMenu pdMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_list);
        
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.header)));
        bar.setTitle("Меню");
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);
        prgLoading = (ProgressBar) findViewById(R.id.prgLoading);
        listMenu = (ListView) findViewById(R.id.listMenu);
        edtKeyword = (EditText) findViewById(R.id.edtKeyword);
        btnSearch = (ImageButton) findViewById(R.id.btnSearch);
        txtAlert = (TextView) findViewById(R.id.txtAlert);

        
        // get category id and category name that sent from previous page
        Intent iGet = getIntent();
        Category_ID = iGet.getIntExtra("category_id",0);
        //Category_name = iGet.getStringExtra("category_name");

		// set category name to textview
//        txtTitle.setText(Category_name);
        

       
        // call asynctask class to request tax and currency data from server
        retrofitGetTaxAndGetMenuRun();
		
        // event listener to handle search button when clicked
		btnSearch.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {

				retrofitGetTaxAndGetMenuRun();
			}
		});
		
		// event listener to handle list when clicked
		listMenu.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				// go to menu detail page
				Intent iDetail = new Intent(ActivityMenuList.this, ActivityMenuDetail.class);
				iDetail.putExtra("menu_id", Integer.parseInt(pdMenu.getData().get(position).getMenu().getMenuId()));
				iDetail.putExtra("currency", Currency);
				startActivity(iDetail);
				overridePendingTransition(R.anim.open_next, R.anim.close_next);
			}
		});       
        
    }
	void retrofitGetTaxAndGetMenuRun() {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constant.AccessKeyParam, Constant.AccessKeyValue);

		prgLoading.setVisibility(0);
		txtAlert.setVisibility(8);

		App.getApi().getTaxCurrency(map).enqueue(new Callback<PDTax>() {
			@Override
			public void onResponse(Call<PDTax> call, Response<PDTax> response) {
				prgLoading.setVisibility(8);
				if (response.isSuccessful()) {
					pdTax = response.body();
					//Tax = Double.valueOf(pdTax.getData().get(0).getTaxOnCurrency().getValue());
					Currency = pdTax.getData().get(1).getTaxOnCurrency().getValue();
					if((Currency != null)){
						retrofitGetMenuRun();
					}else{
						txtAlert.setVisibility(0);
					}
				} else {
					txtAlert.setVisibility(0);
				}
			}

			@Override
			public void onFailure(Call<PDTax> call, Throwable t) {
				txtAlert.setVisibility(0);
			}
		});
	}
	void retrofitGetMenuRun() {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constant.AccessKeyParam, Constant.AccessKeyValue);
		map.put("category_id", String.valueOf(Category_ID));

		prgLoading.setVisibility(0);
		txtAlert.setVisibility(8);

		App.getApi().getMenu(map).enqueue(new Callback<PDMenu>() {
			@Override
			public void onResponse(Call<PDMenu> call, Response<PDMenu> response) {
				//Данные успешно пришли, но надо проверить response.body() на null
				prgLoading.setVisibility(8);

				// if internet connection and data available show data on list
				// otherwise, show alert text
				if (response.isSuccessful()) {
					pdMenu = response.body();
					mla = new AdapterMenuList(ActivityMenuList.this,response.body(),Currency);
					listMenu.setVisibility(0);
					listMenu.setAdapter(mla);

				} else {
					txtAlert.setVisibility(0);
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
			listMenu.invalidateViews();

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
