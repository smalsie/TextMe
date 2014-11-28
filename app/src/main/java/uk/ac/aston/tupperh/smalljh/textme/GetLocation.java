package uk.ac.aston.tupperh.smalljh.textme;
/**
 *
 * @author Hugh Tupper and Joshua Small
 * @version 24/11/2014
 * Used to get the current location of the phone
 *
 */
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;

public class GetLocation extends Task implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    //Location listener and Location client used if Google Play Services Unavailable
    private LocationListener locationListener;
    private LocationClient locationClient;


    public GetLocation(Context context) {
        super(context);
      }

    @Override
    public void performTask() {
        //If Google Play Services are available
        if (servicesConnected()) {

            locationClient = new LocationClient(context, this, this);

            locationClient.connect();


        } else {
            //Do it the old fashioned way
            locationListener = new MyLocationListener();


            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, false);

            locationManager.requestLocationUpdates(provider, 4000, 1, (LocationListener) locationListener);

        }
    }
    // Check that Google Play services is available
    private boolean servicesConnected() {

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
            Log.d("Location Updates",
                    "Google Play services NOT available.");
            return false;

        }
    }

    //When the location has been found send it back to the server
    @Override
    public void onConnected(Bundle bundle) {

        Location location = locationClient.getLastLocation();
        String message = "Location LAT: " + location.getLatitude() + " LNG: " + location.getLongitude() + " ACC: " + location.getAccuracy();

        sendMessage(message);

    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show();
    }



    /**
     * Class used for finding the location when Google Play Services is not available
     */
    private class MyLocationListener implements LocationListener {


        /**
         * When the location has changed, or in our case when the location has been found
         * @param location : The new location
         */
        @Override
        public void onLocationChanged(Location location) {
            // \Ensure that the location is not null
            if (location != null) {

                String message = "Location LAT: " + location.getLatitude() + " LNG: " + location.getLongitude() + " ACC: " + location.getAccuracy();

                sendMessage(message);

            }

        }

        //Unused
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        //Unused
        @Override
        public void onProviderEnabled(String provider) {}

        //Unused
        @Override
        public void onProviderDisabled(String provider) {}


    }

}
