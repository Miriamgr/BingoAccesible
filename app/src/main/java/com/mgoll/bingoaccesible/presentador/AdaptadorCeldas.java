package com.mgoll.bingoaccesible.presentador;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mgoll.bingoaccesible.R;
import com.mgoll.bingoaccesible.modelo.Celda;

/**
 * Created by Miriam on 16/04/2017.
 */

public class AdaptadorCeldas extends BaseAdapter{
    private Context context;
    private Celda[][] carton;

    public AdaptadorCeldas(Context context,Celda[][] carton){
        this.context = context;
        this.carton = carton;
    }

    public int getItemRow(int i){
        if(0 <= i && i <= 8)
            return 0;
        else if (8 < i && i <= 17)
            return 1;
        else
            return 2;
    }

    @Override
    public int getCount() {

        return 27;
    }

    @Override
    public Celda getItem(int i) {

        if(0 <= i && i <= 8)
            return carton[0][i];
        else if (8 < i && i <= 17)
            return carton[1][i-9];
        else
            return carton[2][i-18];

    }

    @Override
    public long getItemId(int i) {
      return getItem(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.elemento_celda, viewGroup, false);
        }

        TextView valorCelda = (TextView) view.findViewById(R.id.tv_celda);
        String contentDes;

        final Celda item = getItem(i);
        valorCelda.setText(item.getValor());
        contentDes = item.getValor();

        if (item.isSeleccionada() && !item.getValor().equalsIgnoreCase(" ")){
            valorCelda.setBackgroundColor(Color.GRAY);
            valorCelda.setTextColor(Color.DKGRAY);
            contentDes = contentDes.concat(", seleccionada");
        }
        else{
            if(item.getValor().equalsIgnoreCase(" ")){
                contentDes = contentDes.concat("Blanco");
            }
            else {
                valorCelda.setBackgroundColor(Color.parseColor("#FFDAB691"));
                valorCelda.setTextColor(Color.WHITE);
                contentDes = contentDes.concat(", no seleccionada");
            }
        }

        valorCelda.setContentDescription(contentDes);

        return view;
    }
}
