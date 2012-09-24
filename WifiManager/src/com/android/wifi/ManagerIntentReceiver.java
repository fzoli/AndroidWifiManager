package com.android.wifi;

import static com.android.wifi.Manager.COMMAND;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * You can start/stop service from shell using:
 * am broadcast -a com.android.wifimanager.start
 * am broadcast -a com.android.wifimanager.stop
 * 
 * You can enable/disable autostart from shell using:
 * am broadcast -a com.android.wifimanager.autostart.on
 * am broadcast -a com.android.wifimanager.autostart.off
 */
public class ManagerIntentReceiver extends BroadcastReceiver {

	public static final String PRE = "com.android.wifimanager.";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action == null) action = "";
		Intent serviceIntent = new Intent(context, Manager.class);
		if (action.equals("android.intent.action.BOOT_COMPLETED")) {
			context.startService(serviceIntent);
		}
		else if (action.startsWith(PRE)) {
			serviceIntent.putExtra(COMMAND, action.substring(24));
		}
		context.startService(serviceIntent);
	}

}