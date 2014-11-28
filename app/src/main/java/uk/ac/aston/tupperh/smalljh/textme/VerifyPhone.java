package uk.ac.aston.tupperh.smalljh.textme;
/**
 *
 * @author Hugh Tupper and Joshua Small
 * @version 24/11/2014 *
 * Used as to verify the users phone
 *
 */
import android.content.Context;

public class VerifyPhone extends Task {

    public VerifyPhone(Context context) {
        super(context);
    }

    @Override
    public void performTask() {
        //We just need to send the word verify back to verify the phone
        sendMessage("Verify");
    }


}
