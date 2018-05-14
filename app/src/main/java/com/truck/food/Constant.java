package com.truck.food;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Constant {

	// API URL configuration

	public static final String BaseURL = "https://www.foodtruck8.info";

	public static final String CategoryAPI = "/panel/api/get-all-category-data.php";
	public static final String MenuAPI = "/panel/api/get-menu-data-by-category-id.php";
	public static final String AllTheDishesAPI = "/panel/api/take-all-the-dishes.php";
	public static final String MenuDetailAPI = "panel/api/get-menu-detail.php";
	public static final String AdminPageURL = "https://www.foodtruck8.info/panel/";

	//public static final String SendDataAPI = "/panel/api/get-re.php";
	public static final String SendDataAPI = "/panel/api/add_user.php";
	public static final String SendOrderAPI = "/panel/api/add_order.php";


	// accesskey in admin panel for security reason
	public static String AccessKeyParam = "accesskey";
	public static String AccessKeyValue = "12345";



    // method to check internet connection
	public static boolean isNetworkAvailable(Activity activity) {
		ConnectivityManager connectivity = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
