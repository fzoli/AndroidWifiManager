package com.android.wifi;

import static com.android.wifi.Manager.CMD_START;
import static com.android.wifi.Manager.CMD_STOP;
import static com.android.wifi.ManagerIntentReceiver.PRE;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class ManagerPreferenceActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferences);
		addPreferencesFromResource(R.xml.preferences);
		ToggleButton btStartStop = ((ToggleButton) findViewById(R.id.btStartStop));
		btStartStop.setChecked(Manager.isRunning());
		btStartStop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton bt, boolean checked) {
				sendBroadcast(new Intent(PRE + (checked ? CMD_START : CMD_STOP)));
			}
			
		});
	}

}