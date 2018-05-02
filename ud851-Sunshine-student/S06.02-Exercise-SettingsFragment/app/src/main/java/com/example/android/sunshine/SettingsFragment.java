package com.example.android.sunshine;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

import java.util.prefs.Preferences;


public class SettingsFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.app_prefs);

        PreferenceScreen preferenceScreen = getPreferenceScreen();

        for(int i=0; i<preferenceScreen.getPreferenceCount(); i++) {
            Preference pref = preferenceScreen.getPreference(i);
            if(!(pref instanceof CheckBoxPreference)) {
                setPreferenceSummary(pref, null);
            }
        }
    }

    public void setPreferenceSummary(Preference preference, Object o) {
        String prefKey = preference.getKey();

        if(prefKey == getString(R.string.pref_location_key)) {
            preference.setSummary(((EditTextPreference) preference).getText());
        } else if(prefKey == getString(R.string.pref_units_key)) {
            preference.setSummary(((ListPreference) preference).getEntry());
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference pref = findPreference(key);

        if(pref != null) {
            String value = sharedPreferences.getString(key, "");
            setPreferenceSummary(pref, null);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getPreferenceManager()
                .getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getPreferenceManager()
                .getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
