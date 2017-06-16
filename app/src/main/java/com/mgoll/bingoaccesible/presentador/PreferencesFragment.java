package com.mgoll.bingoaccesible.presentador;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.SeekBarPreference;
import android.support.v7.preference.SwitchPreferenceCompat;

import com.mgoll.bingoaccesible.R;


public class PreferencesFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String KEY_PREF_NOMBRE = "edittext_preference_nombre";
    public static final String KEY_PREF_VELOCIDAD = "seekbar_preference_velocidad";
    public static final String KEY_PREF_MODO = "switch_preference_modo";
    public static final String KEY_PREF_DIFICULTAD ="list_preference_dificultad";
    public static final String KEY_PREF_CATEGORIA = "categoria_invisible";
    public static final String KEY_PREF_PMBJ = "preference_pmbj";
    public static final String KEY_PREF_PMCJ = "preference_pmcj";
    public static final String KEY_PREF_PMCOJ = "preference_pmcoj";
    public static final String KEY_PREF_PT = "preference_pt";
    public static final String KEY_PREF_BJ = "preference_bj";
    public static final String KEY_PREF_LJ ="preference_lj";

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        //add xml
        setPreferencesFromResource(R.xml.preferencias, s);

        Preference button = findPreference(getString(R.string.borrar_config));
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(KEY_PREF_NOMBRE, "Usuario");
                editor.putBoolean(KEY_PREF_MODO, true);
                editor.putInt(KEY_PREF_VELOCIDAD, 5);
                editor.putString(KEY_PREF_DIFICULTAD, "2");
                editor.commit();

                onCreate(null);

                return true;
            }
        });

        PreferenceCategory category = (PreferenceCategory) findPreference(KEY_PREF_CATEGORIA);

        for(int i = category.getPreferenceCount()-1; i>=0; i--){
            //getPreferenceScreen().removePreference(category.getPreference(i));
            category.removePreference(category.getPreference(i));
        }

        getPreferenceScreen().removePreference(category);

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
            int dificultad = Integer.parseInt(sharedPreferences.getString(KEY_PREF_DIFICULTAD, "2"));
            MainActivity ma = (MainActivity) getActivity();

            ma.actualizarPreferencias(nombre, velocidad, modo, null, dificultad);
        }
    }


}

