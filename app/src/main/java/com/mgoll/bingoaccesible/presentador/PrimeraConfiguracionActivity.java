package com.mgoll.bingoaccesible.presentador;

import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;

import com.mgoll.bingoaccesible.R;

import static com.mgoll.bingoaccesible.presentador.PreferencesFragment.KEY_PREF_MODO;
import static com.mgoll.bingoaccesible.presentador.PreferencesFragment.KEY_PREF_NOMBRE;
import static com.mgoll.bingoaccesible.presentador.PreferencesFragment.KEY_PREF_VELOCIDAD;

public class PrimeraConfiguracionActivity extends AppCompatActivity implements InicioFragment.OnFragmentInteractionListener, ConfiguracionFragment.OnFragmentInteractionListener {


    private SharedPreferences sp;
    private String fragmento;
    Button boton_saltar, boton_siguiente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primera_configuracion);

        boton_saltar = (Button) findViewById(R.id.boton_saltar);
        boton_siguiente = (Button) findViewById(R.id.boton_paso_siguiente);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        InicioFragment init = new InicioFragment();

        if(init!= null){
            ft.replace(R.id.fragmento_config, init, "inicio" );
            ft.commit();
        }
        fragmento = "inicio";
        fm.executePendingTransactions();

        boton_saltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                botonPulsado("saltar");
            }
        });
        boton_siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                botonPulsado("siguiente");
            }
        });

        sp = PreferenceManager.getDefaultSharedPreferences(this);
    }

    private void botonPulsado(String boton) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if(boton.equals("saltar"))
            finish();
        else{
            if(fragmento.equals("inicio")){
                ConfiguracionFragment config = new ConfiguracionFragment();

                if(config != null){
                    ft.replace(R.id.fragmento_config, config , "configuracion");
                    ft.commit();
                }
                fm.executePendingTransactions();
                fragmento = "configuracion";
            }
            else{
                finish();
            }
        }
    }

    @Override
    public void onFragmentInteraction(String name, int velocidad, String modo) {
        FragmentManager fm = getSupportFragmentManager();
        SharedPreferences.Editor editor = sp.edit();

        if(name != null)
            editor.putString(KEY_PREF_NOMBRE, name);
        if(velocidad!= -1)
            editor.putInt(KEY_PREF_VELOCIDAD, velocidad);
        if(modo!= null){
            if(modo.equals("true"))
                editor.putBoolean(KEY_PREF_MODO, true);
            else
                editor.putBoolean(KEY_PREF_MODO, false);
        }
        editor.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
