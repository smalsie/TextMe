package uk.ac.aston.tupperh.smalljh.textme;

import android.content.Context;

/**
 * Created by joshuahugh on 27/11/14.
 */
public abstract class Task {

    protected Context context;


    public Task(Context context) {
        this.context = context;
    }

    public abstract void performTask();
}
