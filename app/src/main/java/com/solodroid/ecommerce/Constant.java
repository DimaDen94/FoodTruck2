package com.solodroid.ecommerce;

import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Constant {

	// API URL configuration

	public static String AdminPageURL = "https://www.foodtruck8.info/panel/";
	static String CategoryAPI = "https://www.foodtruck8.info/panel/api/get-all-category-data.php";
	public static String MenuAPI = "https:/www.foodtruck8.info/panel/api/get-menu-data-by-category-id.php";
	public static String TaxCurrencyAPI = "https://www.foodtruck8.info/panel/api/get-tax-and-currency.php";
	public static String MenuDetailAPI = "https://www.foodtruck8.info/panel/api/get-menu-detail.php";
	public static String SendDataAPI = "https://www.foodtruck8.info/panel/api/add-reservation.php";

	// change this access similar with accesskey in admin panel for security reason
	public static String AccessKey = "12345";

	// database path configuration
	static String DBPath = "/data/data/com.truck.shop/databases/";
	
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
	
	// method to handle images from server
	public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }

}
