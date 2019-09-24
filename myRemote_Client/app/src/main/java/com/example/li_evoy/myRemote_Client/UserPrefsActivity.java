package com.example.li_evoy.myRemote_Client;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class UserPrefsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

    }
}
