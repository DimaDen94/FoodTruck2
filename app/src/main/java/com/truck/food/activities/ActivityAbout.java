package com.truck.food.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.truck.food.SugarHelper;
import com.truck.food.R;

public class ActivityAbout extends AppCompatActivity {
	private TextView notifCount;
	private ImageView notifImg;
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		initToolbar();

	}

	private void initToolbar() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle(R.string.about_title);
		setSupportActionBar(toolbar);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
		final MenuItem menuItem = menu.findItem(R.id.action_cart);
		View actionView = MenuItemCompat.getActionView(menuItem);
		notifCount = (TextView) actionView.findViewById(R.id.cart_badge);
		notifImg = (ImageView) actionView.findViewById(R.id.cart_img);
		notifCount.setText(String.valueOf(SugarHelper.getDishCount()));
		notifImg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent iMyOrder = new Intent(ActivityAbout.this, ActivityCart.class);
				startActivity(iMyOrder);
				overridePendingTransition(R.anim.open_next, R.anim.close_next);
			}
		});
        return true;
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.open_main, R.anim.close_next);
	}
	@Override
	protected void onStart() {
		super.onStart();
		if (notifCount != null)
			notifCount.setText(String.valueOf(SugarHelper.getDishCount()));
	}
}