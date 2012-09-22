package com.android.wifi;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;

public class Manager extends Service {

	public static final String COMMAND = "command";

	private static final String AUTOSTART = "autostart";
	private static final String TAG = "wifi manager";

	private static final int PERIOD = 1000;

	private static Timer timer;
	private static TimerTask task;
	private static WifiManager wfm;

	private static void start() {
		if (timer != null) return;
		timer = new Timer();
		task = new TimerTask() {

			@Override
			public void run() {
				try {
					wfm.setWifiEnabled(true);
				}
				catch (Exception ex) {
				}
			}

		};
		timer.schedule(task, 0, PERIOD);
	}

	private static void stop() {
		if (timer == null) return;
		task.cancel();
		task = null;
		timer = null;
	}

	private boolean isAutostart() {
		return getSharedPreferences().getBoolean(AUTOSTART, true);
	}
	
	private void setAutostart(boolean value) {
		Editor editor = getSharedPreferences().edit();
		editor.putBoolean(AUTOSTART, value);
		editor.commit();
	}
	
	private SharedPreferences getSharedPreferences() {
		return getSharedPreferences("preferences", Context.MODE_APPEND);
	}
	
	@Override
	public IBinder onBind(Intent i) {
		return null; 
	}

	@Override
	public void onDestroy() {
		stop();
		Log.i(TAG, "service stop");
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (wfm == null) {
			wfm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		}
		if (intent.hasExtra(COMMAND)) {
			String action = intent.getExtras().getString(COMMAND);
			if ("start".equals(action)) {
				Log.i(TAG, "service start");
				start();
			}
			else if ("stop".equals(action)) {
				Log.i(TAG, "service stop");
				stop();
			}
			else if ("autostart.on".equals(action)) {
				Log.i(TAG, "autostart enabled");
				setAutostart(true);
			}
			else if ("autostart.off".equals(action)) {
				Log.i(TAG, "autostart disabled");
				setAutostart(false);
			}
		}
		else {
			if (isAutostart()) {
				Log.i(TAG, "service autostart");
				start();
			}
			else {
				Log.i(TAG, "skip autostart");
			}
		}
		return START_STICKY;
	}

}