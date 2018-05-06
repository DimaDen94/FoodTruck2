package com.truck.food.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.truck.food.R;

public class ActivityContactUs extends AppCompatActivity {

	private EditText body;
	private Button send;
	private Toolbar toolbar;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.AppDefault);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_us);


		initToolbar();

		body = (EditText) findViewById(R.id.body);
		send = (Button) findViewById(R.id.send);

	}
	private void initToolbar() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle(R.string.call_us_title);
		setSupportActionBar(toolbar);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem menuItem) {
				Intent iMyOrder = new Intent(ActivityContactUs.this, ActivityCart.class);
				startActivity(iMyOrder);
				overridePendingTransition(R.anim.open_next, R.anim.close_next);
				return true;
			}
		});
	}
	public void SendBtn(View v) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("plain/text");
		intent.putExtra(Intent.EXTRA_EMAIL,
				new String[] { getResources().getString(R.string.email_address) });

		intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));

		String body_email = body.getText().toString() + "\n\n Sent from Ecommerce Android App";
		intent.putExtra(Intent.EXTRA_TEXT, body_email);
		startActivityForResult(Intent.createChooser(intent, "Send email..."), 100);
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
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent intent = new Intent(ActivityContactUs.this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
		overridePendingTransition(R.anim.open_main, R.anim.close_next);
	}

}
