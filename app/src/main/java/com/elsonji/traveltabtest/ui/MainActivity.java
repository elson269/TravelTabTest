package com.elsonji.traveltabtest.ui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.elsonji.traveltabtest.R;
import com.elsonji.traveltabtest.WifiApp;
import com.elsonji.traveltabtest.actions.Actions;
import com.elsonji.traveltabtest.stores.PasswordStore;
import com.elsonji.traveltabtest.stores.SSIDStore;
import com.hardsoftstudio.rxflux.action.RxError;
import com.hardsoftstudio.rxflux.dispatcher.RxViewDispatch;
import com.hardsoftstudio.rxflux.store.RxStore;
import com.hardsoftstudio.rxflux.store.RxStoreChange;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RxViewDispatch {

    private static String TAG = MainActivity.class.getName();

    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 101;
    boolean mWifiEnabled;
    Button mWifiButton;
    WifiManager mWifiManager;
    String mSSID;
    String mPassword;
    List<ScanResult> mNetworkList;
    WifiConfiguration mWifiConfig;
    PasswordStore mPasswordStore;
    SSIDStore mSSIDStore;
    CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.
                ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION);
        } else {
            mNetworkList = mWifiManager.getScanResults();
        }

        WifiApp.get(this).getActionCreator().getSSID();

        mWifiConfig = new WifiConfiguration();

        mWifiButton = findViewById(R.id.wifiButton);
        mWifiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                turnOnWifi();
            }
        });
    }

    void turnOnWifi() {
        WifiApp.get(this).getActionCreator().getPassword();
        mWifiEnabled = mWifiManager.isWifiEnabled();
        if (mWifiEnabled) {
            if (mSSID.equals(getWifiSSID())) {
                Toast.makeText(this, "Your device is already on the desired network.",
                        Toast.LENGTH_SHORT).show();
            } else {
                connectToWifi();
            }
        } else {
            mWifiManager.setWifiEnabled(true);
            connectToWifi();
        }
    }

    private boolean isHexString(String s) {
        if (s.length() == 0 ||
                (s.charAt(0) != '-' && Character.digit(s.charAt(0), 16) == -1))
            return false;
        if (s.length() == 1 && s.charAt(0) == '-')
            return false;

        for (int i = 1; i < s.length(); i++)
            if (Character.digit(s.charAt(i), 16) == -1)
                return false;
        return true;
    }

    private String getWifiSSID() {
        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();

        //getSSID() returns strings with double quotations, so they have to be removed for string comparison.
        return wifiInfo.getSSID().replace("\"", "").trim();
    }

    private void connectToWifi() {
        if (mNetworkList != null) {
            for (ScanResult network : mNetworkList) {
                if (mSSID.equals(network.SSID)) {
                    String capabilities = network.capabilities;
                    if (capabilities.contains("WPA2")) {
                        mWifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                        mWifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                        mWifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                        mWifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                        mWifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                        mWifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                        mWifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                        mWifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                        mWifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

                        mWifiConfig.preSharedKey = "\"".concat(mPassword).concat("\"");

                    } else if (capabilities.contains("WPA")) {
                        mWifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                        mWifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                        mWifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                        mWifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                        mWifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                        mWifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                        mWifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                        mWifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                        mWifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

                        mWifiConfig.preSharedKey = "\"".concat(mPassword).concat("\"");

                    } else if (capabilities.contains("WEP")) {
                        mWifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                        mWifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                        mWifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                        mWifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                        mWifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
                        mWifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                        mWifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                        mWifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                        mWifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);

                        if (isHexString(mPassword)) mWifiConfig.wepKeys[0] = mPassword;
                        else mWifiConfig.wepKeys[0] = "\"".concat(mPassword).concat("\"");
                        mWifiConfig.wepTxKeyIndex = 0;
                    }
                    int networkId = mWifiManager.addNetwork(mWifiConfig);
                    if (networkId != -1) {
                        mWifiManager.enableNetwork(networkId, true);
                        mWifiEnabled = mWifiManager.isWifiEnabled();
                        if (mWifiEnabled) {
                            Toast.makeText(this, "Your device has been successfully connected to "
                                    + mSSID, Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onRxStoreChanged(@NonNull RxStoreChange change) {
        switch (change.getStoreId()) {
            case SSIDStore.SSID_ID:
                switch (change.getRxAction().getType()) {
                    case Actions.GET_SSID_ACTION:
                        mSSID = mSSIDStore.getSSID();
                        mWifiConfig.SSID = "\"".concat(mSSID).concat("\"");
                        mWifiConfig.priority = 40;
                        break;
                }
                break;
            case PasswordStore.PASSWORD_ID:
                switch (change.getRxAction().getType()) {
                    case Actions.GET_PASSWORD_ACTION:
                        mPassword = mPasswordStore.getPassword();
                        break;
                }
        }
    }

    @Override
    public void onRxError(@NonNull RxError error) {
        Throwable throwable = error.getThrowable();
        if (throwable != null) {
            Snackbar.make(mCoordinatorLayout, "An error occured.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Retry", v -> WifiApp.get(this).getActionCreator().retry(error));
            throwable.printStackTrace();
        } else {
            Toast.makeText(this, "unknown error", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRxViewRegistered() {

    }

    @Override
    public void onRxViewUnRegistered() {

    }

    @Nullable
    @Override
    public List<RxStore> getRxStoreListToRegister() {
        mSSIDStore = SSIDStore.getSsidStore(WifiApp.get(this).getRxFlux().getDispatcher());
        mPasswordStore = PasswordStore.getPasswordStore(WifiApp.get(this).getRxFlux().getDispatcher());
        return Arrays.asList(mSSIDStore, mPasswordStore);
    }

    @Nullable
    @Override
    public List<RxStore> getRxStoreListToUnRegister() {
        return null;
    }
}

