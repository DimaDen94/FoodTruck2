package com.solodroid.ecommerce;

import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Constant {

	// API URL configuration

	public static final String BaseURL = "https://www.foodtruck8.info";

	public static final String CategoryAPI = "/panel/api/get-all-category-data.php";
	public static final String TaxCurrencyAPI = "/panel/api/get-tax-and-currency.php";
	public static final String MenuAPI = "/panel/api/get-menu-data-by-category-id.php";
	public static final String MenuDetailAPI = "panel/api/get-menu-detail.php";
	public static final String AdminPageURL = "https://www.foodtruck8.info/panel/";

	public static final String SendDataAPI = "https://www.foodtruck8.info/panel/api/add-reservation.php";

	// accesskey in admin panel for security reason
	public static String AccessKeyParam = "accesskey";
	public static String AccessKeyValue = "12345";

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
