package uk.ac.aston.tupperh.smalljh.textme;

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


public class IncomingSms extends BroadcastReceiver implements LocationListener {
    // Get the object of SmsManager

    Location location;
    private LocationManager locationManager;
    String phoneNo;


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

                    Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);

                    if(phoneNumber.equals("+447854977333")) {


                        // Show Alert
                        Toast toast = Toast.makeText(context,
                                "senderNum: "+ senderNum + ", message: " + message, Toast.LENGTH_LONG);
                        toast.show();

                        phoneNo = senderNum;

                        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

                        Location location = locationManager.getLastKnownLocation(Context.LOCATION_SERVICE);
                        if(location != null && (location.getTime() > Calendar.getInstance().getTimeInMillis() - 2 * 60 * 1000)) {
                            // Do something with the recent location fix
                            //  otherwise wait for the update below
                            sendLoc(location);
                        }
                        else {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                            Log.v("Location Changed", "nkjhio");
                        }

                    } else {
                        //sms.sendTextMessage(phoneNumber, null, "Yo", null, null);

                    }

                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        if (location != null) {
            Log.v("Location Changed", location.getLatitude() + " and " + location.getLongitude());
            locationManager.removeUpdates(this);

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

    private void sendLoc(Location location) {
        final SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNo, null, "LNG: " + location.getLatitude() + " LAT: " + location.getLongitude(), null, null);
    }


}
