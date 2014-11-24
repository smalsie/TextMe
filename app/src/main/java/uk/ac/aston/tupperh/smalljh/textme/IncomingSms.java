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


public class IncomingSms extends BroadcastReceiver {
    // Get the object of SmsManager

    Location location;
    private LocationManager locationManager;
    String phoneNo;
    private String provider;
    private LocationListener locationListener;

    //Gets the new text message
    public void onReceive(Context context, Intent intent) {
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

                        locationListener = new LocationListener() {
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
                        };

                        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

                        Criteria criteria = new Criteria();
                        provider = locationManager.getBestProvider(criteria, false);

                        Location location = locationManager.getLastKnownLocation(provider);
                        Log.v("Location Changed", location.getLatitude() + " and " + location.getLongitude());
                        sendLoc(location);
                        locationManager.requestLocationUpdates(provider, 4000, 1, (LocationListener) locationListener);

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
        final SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage("+441355377112", null, "LNG: " + location.getLatitude() + " LAT: " + location.getLongitude(), null, null);
        //sms.sendTextMessage("+441355377112", null, "Hello", null, null);


        locationManager.removeUpdates(locationListener);


    }


}
