package uk.ac.aston.tupperh.smalljh.textme;
/**
 * Used to read each sms that comes in
 *
 *
 */

import java.security.Provider;
import java.util.Calendar;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;


public class IncomingSms extends BroadcastReceiver implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {
    // Get the object of SmsManager

    private Location currentLocation;
    private LocationManager locationManager;
    String phoneNo;
    private String provider;
    private LocationListener locationListener;
    private Context context;
    private LocationClient locationClient;

    private final static int
            CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    //Gets the new text message
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();


                    Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);

                    //if the text is from the server
                    if (phoneNumber.equals("+441355377112")) {

                        //need to do stuff!
                        // Show Alert
                        Toast toast = Toast.makeText(context,
                                "senderNum: " + senderNum + ", message: " + message, Toast.LENGTH_LONG);
                        toast.show();

                        phoneNo = senderNum;

                        if (servicesConnected()) {

                            locationClient = new LocationClient(context, this, this);

                            locationClient.connect();

                            Location loc = null;

                            if(locationClient.isConnected()) {
                                loc = locationClient.getLastLocation();

                            }


                            sendLoc(loc);


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


                    } else {
                        //sms.sendTextMessage(phoneNumber, null, "Yo", null, null);

                    }

                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);

        }
    }


    private void sendLoc(Location location) {

        //if(currentLocation

        final SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage("+441355377112", null, "LNG: " + location.getLatitude() + " LAT: " + location.getLongitude(), null, null);
        //sms.sendTextMessage("+441355377112", null, "Hello", null, null);


        //locationManager.removeUpdates(locationListener);


    }

    private static final int THIRTY_SECS = 1000 * 30;


    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
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
