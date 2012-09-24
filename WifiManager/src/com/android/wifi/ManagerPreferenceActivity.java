package com.android.wifi;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class ManagerPreferenceActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferences);
		addPreferencesFromResource(R.xml.preferences);
	}

}