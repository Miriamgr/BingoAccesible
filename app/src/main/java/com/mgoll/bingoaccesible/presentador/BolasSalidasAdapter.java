package com.mgoll.bingoaccesible.presentador;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.mgoll.bingoaccesible.R;

import java.util.ArrayList;

/**
 * Created by Miriam on 13/03/2017.
 */

public class BolasSalidasAdapter extends RecyclerView.Adapter<BolasSalidasAdapter.ViewHolder>{

    private ArrayList<String> values = new ArrayList<String>();
    private int anchoTexto;
    private int altoTexto;
    //String[] values = new String[0];
    private LayoutInflater mInflater;


    //Context context1;

    public BolasSalidasAdapter(Context context, ArrayList<String> values2){

        this.mInflater = LayoutInflater.from(context);
        this.values = values2;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;

        public ViewHolder(View v) {

            super(v);

            textView = (TextView) v.findViewById(R.id.tv_bolanumero);
        }
    }

    //Infla el layout de la celda del xml cuando sea necesario
    @Override
    public BolasSalidasAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View view = mInflater.inflate(R.layout.bola_salida, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    //Pone los datos el el textview de cada celda
    @Override
    public void onBindViewHolder(ViewHolder Vholder, int position){

        String bolaN = values.get(position);

        Vholder.textView.setText(bolaN);

        Vholder.textView.setBackgroundColor(Color.LTGRAY);

        Vholder.textView.setWidth(anchoTexto);
        Vholder.textView.setHeight(altoTexto);

        Vholder.textView.setTextSize(20);

        Vholder.textView.setTextColor(Color.BLACK);
    }

    @Override
    public int getItemCount(){

        return values.size();
    }

    public void addBola(String bola){
        //int posicion = values.length;
         values.add(bola);
        this.notifyDataSetChanged();
    }

   public void cambiarTamCeldas(int alto, int ancho){
       this.anchoTexto = ancho;
       this.altoTexto = alto;
   }
}
