package com.mgoll.bingoaccesible.presentador;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mgoll.bingoaccesible.R;

public class PrimeraConfiguracionActivity extends AppCompatActivity implements InicioFragment.OnFragmentInteractionListener, ConfiguracionFragment.OnFragmentInteractionListener {


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
                //recoger datos
                finish();
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
