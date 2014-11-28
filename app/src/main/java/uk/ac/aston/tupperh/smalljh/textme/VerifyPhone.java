package uk.ac.aston.tupperh.smalljh.textme;

import android.content.Context;
import android.telephony.SmsManager;

/**
 * Created by joshuahugh on 25/11/14.
 */
public class VerifyPhone extends Task {

    public VerifyPhone(Context context) {
        super(context);
    }

    @Override
    public void performTask() {
        final SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage("+441355377112", null, "Verify", null, null);
    }


}
