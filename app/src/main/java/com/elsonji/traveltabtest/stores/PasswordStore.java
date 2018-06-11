package com.elsonji.traveltabtest.stores;

import com.elsonji.traveltabtest.actions.Actions;
import com.elsonji.traveltabtest.actions.Keys;
import com.hardsoftstudio.rxflux.action.RxAction;
import com.hardsoftstudio.rxflux.dispatcher.Dispatcher;
import com.hardsoftstudio.rxflux.store.RxStore;
import com.hardsoftstudio.rxflux.store.RxStoreChange;

public class PasswordStore extends RxStore implements PasswordStoreInterface {
    public static final String PASSWORD_ID = "password_store";
    private static PasswordStore passwordStore;
    private String password;

    public PasswordStore(Dispatcher dispatcher) {
        super(dispatcher);
    }

    public static synchronized PasswordStore getPasswordStore(Dispatcher dispatcher) {
        if (passwordStore == null) {
            passwordStore = new PasswordStore(dispatcher);
        }
        return passwordStore;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void onRxAction(RxAction action) {
        switch (action.getType()) {
            case Actions.GET_PASSWORD_ACTION:
                password = action.get(Keys.PASSWORD);
                break;

            default:
                return;
        }
        postChange(new RxStoreChange(PASSWORD_ID, action));
    }
}
