package uk.ac.aston.tupperh.smalljh.textme;
/**
 *
 * @author Hugh Tupper and Joshua Small
 * @version 24/11/2014
 * Used to read each sms that comes in
 *
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class IncomingSms extends BroadcastReceiver {

    //Gets the new text message
    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent, so in our case for the messages.
        final Bundle bundle = intent.getExtras();

        //attempt to get the messages
        try {
            //if there are messages
            if (bundle != null) {

                //get the texts
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                //go through each text
                for (int i = 0; i < pdusObj.length; i++) {

                    //The message and the senders phonenumber
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    //Get the message body from the message
                    String message = currentMessage.getDisplayMessageBody();

                    //if the text is from the server
                    if (phoneNumber.equals("+441355377112")) {

                        //Split the message up into words
                        String[] msg = message.split(" ");

                        //Variable for the task
                        Task task = null;

                        //If not null and has the keyword Bananas! get the phones location
                        if ((msg[msg.length - 1] != null) && (msg[msg.length - 1].equals("Bananas!"))) {
                            task = new GetLocation(context);
                        }
                        //If not null and has the keyword Oranges! verify the phone
                        else if ((msg[msg.length - 1] != null) && (msg[msg.length - 1].equals("Oranges!"))) {
                            task = new VerifyPhone(context);
                        }
                        //If a task has been assigned perform it
                        if (task != null)
                            task.performTask();


                    }

                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            //Log any exceptions
            Log.e("SmsReceiver", "Exception smsReceiver" + e);

        }
    }

}