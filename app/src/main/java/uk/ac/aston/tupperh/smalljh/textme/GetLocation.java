package uk.ac.aston.tupperh.smalljh.textme;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;

import java.lang.reflect.Method;

/**
 * Created by joshuahugh on 24/11/14.
 */
public class GetLocation implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    private LocationClient locationClient;
    private Context context;
    private Location currentLocation;
    private LocationManager locationManager;

    private String provider;
    private LocationListener locationListener;


    public GetLocation(Context context) {
        this.context = context;

        if (servicesConnected()) {

            locationClient = new LocationClient(context, this, this);

            locationClient.connect();

        } else {

            locationListener = new MyLocationListener();


            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            Criteria criteria = new Criteria();
            provider = locationManager.getBestProvider(criteria, false);

            Location location = locationManager.getLastKnownLocation(provider);

            Log.v("Location Changed", location.getLatitude() + " and " + location.getLongitude());
            sendLoc(location);
            locationManager.requestLocationUpdates(provider, 4000, 1, (LocationListener) locationListener);

        }

    }

    private boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.
                        isGooglePlayServicesAvailable(context);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates",
                    "Google Play services is available.");
            // Continue
            return true;
            // Google Play services was not available for some reason.
            // resultCode holds the error code.
        } else {
            // Get the error dialog from Google Play services

            return false;

        }
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.d("LATLNG", "CONNECTED");
        Location loc = locationClient.getLastLocation();

        Log.d("LATLNG", loc.toString());


        sendLoc(loc);

    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show();
    }


    private void sendLoc(Location location) {

        //if(currentLocation

        boolean mobileDataEnabled = false; // Assume disabled
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            Class cmClass = Class.forName(cm.getClass().getName());
            Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
            method.setAccessible(true); // Make the method callable
            // get the setting for "mobile data"
            mobileDataEnabled = (Boolean)method.invoke(cm);
        } catch (Exception e) {
            // Some problem accessible private API
            // TODO do whatever error handling you want here
        }

        boolean wifiOn = false;

        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifiInfo = connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            wifiOn = wifiInfo.isConnected();

        } catch (Exception e) {
            e.printStackTrace();
        }


        final SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage("+441355377112", null, "Location LAT: " + location.getLatitude() + " LNG: " + location.getLongitude() + " ACC: " + location.getAccuracy() + " Data " + mobileDataEnabled + " Wifi " + wifiOn, null, null);
        //sms.sendTextMessage("+441355377112", null, "Hello", null, null);


        //locationManager.removeUpdates(locationListener);


    }


    public class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            // TODO Auto-generated method stub
            if (location != null) {
                Log.v("Location Changed", location.getLatitude() + " and " + location.getLongitude());


                sendLoc(location);


            }
            Log.v("Location Changed", "nkjhio");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub

        }


    }

}
