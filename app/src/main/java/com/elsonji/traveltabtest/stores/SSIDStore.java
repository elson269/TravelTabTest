package com.elsonji.traveltabtest.stores;

import com.elsonji.traveltabtest.actions.Actions;
import com.elsonji.traveltabtest.actions.Keys;
import com.hardsoftstudio.rxflux.action.RxAction;
import com.hardsoftstudio.rxflux.dispatcher.Dispatcher;
import com.hardsoftstudio.rxflux.store.RxStore;
import com.hardsoftstudio.rxflux.store.RxStoreChange;

public class SSIDStore extends RxStore implements SSIDStoreInterface {

    public static final String SSID_ID = "ssid_Store";
    private static SSIDStore ssidStore;
    private String ssid;

    public SSIDStore(Dispatcher dispatcher) {
        super(dispatcher);
    }

    public static synchronized SSIDStore getSsidStore(Dispatcher dispatcher) {
        if (ssidStore == null) {
            ssidStore = new SSIDStore(dispatcher);
        }
        return ssidStore;
    }

    @Override
    public String getSSID() {
        return ssid;
    }

    @Override
    public void onRxAction(RxAction action) {
        switch (action.getType()) {
            case Actions.GET_SSID_ACTION:
                ssid = action.get(Keys.SSID);
                break;

            default:
                return;
        }
        postChange(new RxStoreChange(SSID_ID, action));
    }
}
