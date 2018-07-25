package com.zchu.debugdb;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amitshekhar.DebugDB;
import com.willowtreeapps.hyperion.plugin.v1.PluginModule;

public class DebugDBModule extends PluginModule {

    @Nullable
    @Override
    public View createPluginView(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.hdbfmanager_item_plugin_layout, parent, false);
        TextView textView = view.findViewById(R.id.tv_address_log);
        textView.setText(DebugDB.getAddressLog());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                try {
                    Intent intent = WebActivity.newIntent(context, getAddressUrl(context), "Android Debug Database");
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        return view;
    }

    private static String getAddressUrl(Context context) {
        int portNumber;
        try {
            portNumber = Integer.valueOf(context.getString(com.amitshekhar.R.string.PORT_NUMBER));
        } catch (NumberFormatException ex) {
            portNumber = 8080;
        }

        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
        @SuppressLint("DefaultLocale") final String formattedIpAddress = String.format("%d.%d.%d.%d",
                (ipAddress & 0xff),
                (ipAddress >> 8 & 0xff),
                (ipAddress >> 16 & 0xff),
                (ipAddress >> 24 & 0xff));
        return "http://" + formattedIpAddress + ":" + portNumber;
    }

    private static boolean deviceCanHandleIntent(Context context, Intent intent) {
        try {
            return !context.getPackageManager().queryIntentActivities(intent, 0).isEmpty();
        } catch (NullPointerException e) {
            return false;
        }
    }
}
