package com.mgoll.bingoaccesible.presentador;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.SeekBarPreference;
import android.support.v7.preference.SwitchPreferenceCompat;

import com.mgoll.bingoaccesible.R;


public class PreferencesFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String KEY_PREF_NOMBRE = "edittext_preference_nombre";
    public static final String KEY_PREF_VELOCIDAD = "seekbar_preference_velocidad";
    public static final String KEY_PREF_MODO = "switch_preference_modo";



    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        //add xml
        setPreferencesFromResource(R.xml.preferencias, s);

        Preference button = findPreference(getString(R.string.borrar_config));
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                PreferenceManager.setDefaultValues(getActivity().getApplicationContext(), R.xml.preferencias, true);
                return true;
            }
        });

        onSharedPreferenceChanged(null, "");
    }
    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (sharedPreferences != null) {
            String nombre = sharedPreferences.getString(KEY_PREF_NOMBRE, "-");
            Boolean modo = sharedPreferences.getBoolean(KEY_PREF_MODO, true);
            int velocidad = sharedPreferences.getInt(KEY_PREF_VELOCIDAD, 5);
            MainActivity ma = (MainActivity) getActivity();

            ma.actualizarPreferencias(nombre, velocidad, modo, null);
        }
    }


}

