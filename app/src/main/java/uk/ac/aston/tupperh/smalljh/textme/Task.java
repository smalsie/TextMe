package uk.ac.aston.tupperh.smalljh.textme;
/**
 *
 * @author Hugh Tupper and Joshua Small
 * @version 24/11/2014 *
 * Used as a placeholder for each task
 *
 */

import android.content.Context;
import android.telephony.SmsManager;

public abstract class Task {
    //The apps context
    protected Context context;

    /**
     * Constructor     *
     * @param context : the apps context
     */
    public Task(Context context) {
        this.context = context;
    }

    /**
     * Perform the task
     */
    public abstract void performTask();

    /**
     * Used to send a text message back to the server
     * @param message : The message to send
     */
    public void sendMessage(String message) {

        final SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage("+441355377112", null, message, null, null);

    }
}
