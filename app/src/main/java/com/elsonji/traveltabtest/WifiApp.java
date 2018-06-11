package com.elsonji.traveltabtest;

import android.app.Application;
import android.content.Context;

import com.elsonji.traveltabtest.actions.ActionCreator;
import com.hardsoftstudio.rxflux.RxFlux;
import com.hardsoftstudio.rxflux.util.LogLevel;

public class WifiApp extends Application {
    private RxFlux rxFlux;

    private ActionCreator actionCreator;

    public static WifiApp get(Context context) {
        return (WifiApp) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        RxFlux.LOG_LEVEL = BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE;
        rxFlux = RxFlux.init(this);
        actionCreator = new ActionCreator(rxFlux.getDispatcher(), rxFlux.getSubscriptionManager());
    }

    public RxFlux getRxFlux() {
        return rxFlux;
    }

    public ActionCreator getActionCreator() {
        return actionCreator;
    }
}
