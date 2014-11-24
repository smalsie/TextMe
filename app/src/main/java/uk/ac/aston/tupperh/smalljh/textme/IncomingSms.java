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


public class IncomingSms extends BroadcastReceiver {
    // Get the object of SmsManager


    String phoneNo;
    private Context context;

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
                        phoneNo = senderNum;

                        String[] msg = message.split(" ");

                        Log.d("msg", msg.length + msg[7] + " " + message);

                        if((msg[14] != null) && (msg[14].equals("Bananas!"))) {
                            new GetLocation(context);
                        }




                    }

                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);

        }
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




}
