package com.truck.food.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.truck.food.R;

public class ActivityConfirmMessage extends AppCompatActivity {
	
	// declare view objects
//	ImageButton imgNavBack;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm);
        

        
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

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
			Intent intent = new Intent(ActivityConfirmMessage.this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |  Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
			overridePendingTransition(R.anim.open_main, R.anim.close_next);
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}
    
    @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	super.onBackPressed();
    	Intent intent = new Intent(ActivityConfirmMessage.this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |  Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
		overridePendingTransition(R.anim.open_main, R.anim.close_next);
    }
}
