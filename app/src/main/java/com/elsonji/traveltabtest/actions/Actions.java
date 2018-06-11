package com.elsonji.traveltabtest.actions;

import com.hardsoftstudio.rxflux.action.RxAction;

public interface Actions {
    String GET_SSID_ACTION = "get_ssid_action";
    String GET_PASSWORD_ACTION = "get_password_action";

    void getSSID();
    void getPassword();
    boolean retry(RxAction rxAction);
}
