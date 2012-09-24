package com.android.wifi;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class Manager extends Service {

	public static final String COMMAND = "command";

	public static final String CMD_START = "start";
	public static final String CMD_STOP = "stop";
	
	public static final String AUTOSTART = "autostart";
	private static final String PASSWORD = "password";
	private static final String TAG = "wifimanager";

	private static final int PERIOD = 1000;

	private static Timer timer;
	private static TimerTask task;
	private static WifiManager wfm;

	public static boolean isRunning() {
		return timer != null;
	}
	
	public static SharedPreferences getSharedPreferences(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	public static boolean isPasswordEquals(Context context, String password) {
		return getSharedPreferences(context).getString(PASSWORD, sha("admin")).equals(sha(password));
	}
	
	private static void setPassword(Context context, String password) {
		getSharedPreferences(context).edit().putString(PASSWORD, sha(password)).commit();
	}
	
	@SuppressWarnings("deprecation")
	private static String sha(String s) {
		return new String(Hex.encodeHex(DigestUtils.sha(s)));
	}
	
	private static void start() {
		if (isRunning()) return;
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
		if (!isRunning()) return;
		task.cancel();
		task = null;
		timer = null;
	}

	private boolean isAutostart() {
		return getSharedPreferences(this).getBoolean(AUTOSTART, true);
	}
	
	private void setAutostart(boolean value) {
		getSharedPreferences(this).edit().putBoolean(AUTOSTART, value).commit();
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
			if (CMD_START.equals(action)) {
				Log.i(TAG, "service start");
				start();
			}
			else if (CMD_STOP.equals(action)) {
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
			else if ("set".equals(action)) {
				if (intent.hasExtra(PASSWORD)) {
					Log.i(TAG, "password changed");
					setPassword(this, intent.getExtras().getString(PASSWORD));
				}
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