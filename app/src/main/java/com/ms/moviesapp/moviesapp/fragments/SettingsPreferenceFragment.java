package com.ms.moviesapp.moviesapp.fragments;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.View;

import com.ms.moviesapp.moviesapp.R;

/**
 * Created by Mohammad-Sayed-PC on 12/18/2015.
 */
public class SettingsPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    private ListPreference mLPSortedBy;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_settings);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mLPSortedBy = (ListPreference) getPreferenceManager().findPreference(getString(R.string.movies_sorted_by_key));
        mLPSortedBy.setOnPreferenceChangeListener(this);
        setPreferencesCurrentData();
    }

    private void setPreferencesCurrentData() {
        String mLPSortedCurrentValue = PreferenceManager.getDefaultSharedPreferences(mLPSortedBy.getContext()).getString(mLPSortedBy.getKey(), getString(R.string.popularity_desc));
        onPreferenceChange(mLPSortedBy, mLPSortedCurrentValue);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (mLPSortedBy != null && preference.getKey() == mLPSortedBy.getKey()) {
            int preferenceIndex = mLPSortedBy.findIndexOfValue((String) newValue);
            mLPSortedBy.setSummary(mLPSortedBy.getEntries()[preferenceIndex]);
            return true;
        }
        return false;
    }
}
