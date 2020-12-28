package com.example.baforecast.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import androidx.core.app.NotificationCompat;
import com.example.baforecast.R;

public class NetworkReceiver extends BroadcastReceiver {
    private int messageId = 2000;

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "2");

        if (action.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
            if (intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false)) {
                builder
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Broadcast Receiver")
                    .setContentText("Wi-fi is connected");

            } else {
                builder
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Broadcast Receiver")
                    .setContentText("Wi-fi connection is lost");
            }
        }

        NotificationManager notificationManager =
            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(messageId++, builder.build());
    }
}