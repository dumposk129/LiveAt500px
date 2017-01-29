package com.dump129.liveat500px;

import android.app.Application;

import com.dump129.liveat500px.manager.Contextor;

/**
 * Created by Dump129 on 1/29/2017.
 */

public class LiveAt500pxApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Contextor.getInstance().init(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
