package uk.ac.aston.tupperh.smalljh.textme;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.preference.Preference;

/**
 * Created by joshuahugh on 25/11/14.
 */
public class ChangePassword extends DeviceAdminReceiver {

    private Context context;

    @Override
    public void onEnabled(Context context, Intent intent) {
        this.context = context;
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        this.context = context;
        return "Hello2context"; //.getString(R.string.admin_receiver_status_disable_warning);
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        this.context = context;
        //showToast(context, context.getString(R.string.admin_receiver_status_disabled));
    }

    @Override
    public void onPasswordChanged(Context context, Intent intent) {
        this.context = context;
        //showToast(context, context.getString(R.string.admin_receiver_status_pw_changed));
    }

    public void changePassword() {

    }


}
