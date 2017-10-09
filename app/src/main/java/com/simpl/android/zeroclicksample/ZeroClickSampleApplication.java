package com.simpl.android.zeroclicksample;

import android.app.Application;

import com.simpl.android.zeroClickSdk.Simpl;


public class ZeroClickSampleApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        Simpl.init(this);
        Simpl.getInstance().runInSandboxMode();
    }
}
