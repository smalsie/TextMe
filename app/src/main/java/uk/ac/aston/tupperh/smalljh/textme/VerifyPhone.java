package uk.ac.aston.tupperh.smalljh.textme;

import android.content.Context;
import android.telephony.SmsManager;

/**
 * Created by joshuahugh on 25/11/14.
 */
public class VerifyPhone {
    Context context;

    public VerifyPhone(Context context) {
        this.context = context;

        final SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage("+441355377112", null, "Verify", null, null);

    }


}
