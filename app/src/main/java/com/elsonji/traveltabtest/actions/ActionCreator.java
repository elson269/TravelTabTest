package com.elsonji.traveltabtest.actions;

import com.hardsoftstudio.rxflux.action.RxAction;
import com.hardsoftstudio.rxflux.action.RxActionCreator;
import com.hardsoftstudio.rxflux.dispatcher.Dispatcher;
import com.hardsoftstudio.rxflux.util.SubscriptionManager;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.elsonji.traveltabtest.actions.Keys.PASSWORD;
import static com.elsonji.traveltabtest.actions.Keys.SSID;

public class ActionCreator extends RxActionCreator implements Actions {

    private Observable<String> SSIDObservable = getSSIDObservable();
    private Observable<String> passwordObservable = getPasswordObservable();

    public ActionCreator(Dispatcher dispatcher, SubscriptionManager manager) {
        super(dispatcher, manager);
    }

    @Override
    public void getSSID() {
        final RxAction SSIDAction = newRxAction(GET_SSID_ACTION);
        if (hasRxAction(SSIDAction)) return;

        addRxAction(SSIDAction, SSIDObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ssid -> postRxAction(newRxAction(GET_SSID_ACTION, SSID, ssid)),
                        throwable -> postError(SSIDAction, throwable)));

    }

    @Override
    public void getPassword() {
        final RxAction passwordAction = newRxAction(GET_PASSWORD_ACTION);
        if (hasRxAction(passwordAction)) return;

        addRxAction(passwordAction, passwordObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(password -> postRxAction(newRxAction(GET_PASSWORD_ACTION, PASSWORD, password)),
                        throwable -> postError(passwordAction, throwable)));
    }


    @Override
    public boolean retry(RxAction rxAction) {
        if (hasRxAction(rxAction))
        return true;

        switch (rxAction.getType()) {
            case GET_SSID_ACTION:
                getSSID();
                return true;

            case GET_PASSWORD_ACTION:
                getPassword();
                return true;
        }
        return false;
    }

    private Observable<String> getSSIDObservable() {
        return Observable.just("TravelTab");
    }

    private Observable<String> getPasswordObservable() {
        return Observable.just("123456789");
    }

}
