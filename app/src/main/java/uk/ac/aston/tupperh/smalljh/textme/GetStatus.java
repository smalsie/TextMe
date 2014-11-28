package uk.ac.aston.tupperh.smalljh.textme;
/**
 *
 * @author Hugh Tupper and Joshua Small
 * @version 24/11/2014
 * Used to get the status of the phones WiFi and Mobile Data
 *
 */
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import java.lang.reflect.Method;


public class GetStatus extends Task {

    public GetStatus(Context context) {
        super(context);
    }

    @Override
    public void performTask() {

        /*
         * Find out if the Mobile data is enabled
         */
        boolean mobileDataEnabled = false; // Assume disabled
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            Class cmClass = Class.forName(cm.getClass().getName());
            Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
            method.setAccessible(true); // Make the method callable
            // get the setting for "mobile data"
            mobileDataEnabled = (Boolean)method.invoke(cm);
        } catch (Exception e) {}

        /*
         * Find out if the WiFi is enabled
         */
        boolean wifiOn = false;

        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifiInfo = connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            wifiOn = wifiInfo.isConnected();

        } catch (Exception e) {}

        String status = " Data " + mobileDataEnabled + " Wifi " + wifiOn;

        sendMessage(status);

    }
}
