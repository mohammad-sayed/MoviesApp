package com.ms.moviesapp.settings;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.ms.moviesapp.R;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }
}
