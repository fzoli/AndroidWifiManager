package com.android.wifi;

import static com.android.wifi.Manager.AUTOSTART;
import static com.android.wifi.Manager.CMD_START;
import static com.android.wifi.Manager.CMD_STOP;
import static com.android.wifi.Manager.isPasswordEquals;
import static com.android.wifi.Manager.isRunning;
import static com.android.wifi.ManagerIntentReceiver.PRE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class ManagerPreferenceActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		final CheckBoxPreference prefAutostart = (CheckBoxPreference) findPreference(AUTOSTART);
		prefAutostart.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				validate(new Runnable() {
					
					@Override
					public void run() {
						prefAutostart.setChecked(!prefAutostart.isChecked());
					}
					
				}, prefAutostart.isChecked());
				return false;
			}
			
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.optStartStop:
				validate(new Runnable() {
					
					@Override
					public void run() {
						sendBroadcast(new Intent(PRE + (isRunning() ? CMD_STOP : CMD_START)));
					}
					
				}, isRunning());
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
		getMenuInflater().inflate(isRunning() ? R.menu.menu_stop : R.menu.menu_start, menu);
	}

	private void validate(final Runnable action, boolean validation) {
		if (validation) {
			final AlertDialog.Builder alert = new AlertDialog.Builder(this).setTitle(getText(R.string.password)).setIcon(android.R.drawable.ic_dialog_alert);
	
			final EditText input = new EditText(this);
			input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
			alert.setView(input);
	
			alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int whichButton) {
					if (isPasswordEquals(ManagerPreferenceActivity.this, input.getText().toString())) {
						action.run();
					}
					else {
						Toast.makeText(ManagerPreferenceActivity.this, R.string.wrong_password, Toast.LENGTH_SHORT).show();
					}
				}
				
			});
	
			alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int whichButton) {
					;
				}
				
			});
	
			alert.show();
		}
		else {
			action.run();
		}
	}

}