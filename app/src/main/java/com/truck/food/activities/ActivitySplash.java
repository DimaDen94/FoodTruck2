package com.truck.food.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.truck.food.R;

public class ActivitySplash extends AppCompatActivity {
	ImageView splash;
    @Override
    public void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
		//menyembunyikan title bar di layar acitivy
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);

		//membuat activity menjadi fullscreen
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//        WindowManager.LayoutParams.FLAG_FULLSCREEN);

		/** Sets a layout for this activity */
		setContentView(R.layout.splash);
		splash = (ImageView) findViewById(R.id.imageProgress);

		RotateAnimation anim = new RotateAnimation(0f, 150f, 200f, 200f);
		anim.setInterpolator(new LinearInterpolator());
		anim.setRepeatCount(Animation.INFINITE);
		anim.setDuration(700);


		splash.startAnimation(anim);




        
        /** Creates a count down timer, which will be expired after 3000 milliseconds */
        new CountDownTimer(3000,1000) {
        	
        	/** This method will be invoked on finishing or expiring the timer */
			@Override
			public void onFinish() {
				/** Creates an intent to start new activity */
				Intent intent = new Intent(getBaseContext(), MainActivity.class);
				
				//memulai activity baru ketika waktu timer habis
				startActivity(intent);
				
				//menutup layar activity
				finish();
				
			}

			/** This method will be invoked in every 1000 milli seconds until 
			* this timer is expired.Because we specified 1000 as tick time 
			* while creating this CountDownTimer
			*/
			@Override
			public void onTick(long millisUntilFinished) {
								
			}
		}.start();
        
    }
}