package com.android.wifi;

import static com.android.wifi.Manager.CMD_START;
import static com.android.wifi.Manager.CMD_STOP;
import static com.android.wifi.ManagerIntentReceiver.PRE;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuItem;

public class ManagerPreferenceActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.optStartStop:
				sendBroadcast(new Intent(PRE + (Manager.isRunning() ? CMD_STOP : CMD_START)));
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        inflateMenu(menu);
		return true;
	}
	
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		inflateMenu(menu);
		return true;
	}
	
	private void inflateMenu(Menu menu) {
		menu.clear();
		getMenuInflater().inflate(Manager.isRunning() ? R.menu.menu_stop : R.menu.menu_start, menu);
	}
	
}