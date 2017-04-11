package com.mgoll.bingoaccesible.presentador;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mgoll.bingoaccesible.R;
import com.mgoll.bingoaccesible.modelo.Bombo;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BomboFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BomboFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BomboFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Bombo bombo;
    private String mParam2;
    private View vista;
    private TextView tv;
    private int posicion_actual;
    private int numero_bolas;
    private int ultimaBola; //Ultima bola salida, nos servirá para actualizar las vistas de cartón.


    private OnFragmentInteractionListener mListener;

    public BomboFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BomboFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BomboFragment newInstance(String param1, String param2) {
        BomboFragment fragment = new BomboFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bombo = new Bombo();
        bombo.inicializa_bombo();
        posicion_actual = bombo.getPosicion_actual();
        numero_bolas = bombo.getNumbolas();

        if (getArguments() != null) {
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        vista = inflater.inflate(R.layout.fragment_bombo, container, false); //"Inflamos" la vista del fragment
        ((Button) vista.findViewById(R.id.boton_parar)) //Establecemos el listener del botón pulsado
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View vista) {
                        botonPulsado(vista);
                    }
                });
        ((Button) vista.findViewById(R.id.boton_anterior)) //Establecemos el listener del botón pulsado
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View vista) {
                        botonPulsado(vista);
                    }
                });
        ((Button) vista.findViewById(R.id.boton_continuar)) //Establecemos el listener del botón pulsado
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View vista) {
                        botonPulsado(vista);
                    }
                });
        ((Button) vista.findViewById(R.id.boton_siguiente)) //Establecemos el listener del botón pulsado
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View vista) {
                        botonPulsado(vista);
                    }
                });
        tv = ((TextView) vista.findViewById(R.id.tv_bola_actual));

        return vista;// devolvemos la vista
    }

    // TODO: Rename method, update argument and hook method into UI event
   private void botonPulsado(View v) {

        if (mListener != null) {
            if(((Button) v).getText().equals("Atras")){
                mListener.onFragmentInteraction("ATRAS");
            }
            else if(((Button) v).getText().equals("Siguiente")){
                mListener.onFragmentInteraction("SIG");
            }
            else if(((Button) v).getText().equals("Parar")){
                mListener.onFragmentInteraction("PARAR");
            }
            else if(((Button) v).getText().equals("Continuar")){
                mListener.onFragmentInteraction("CONT");
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void bolaAnterior() {
        final int bola;

        Context c = getActivity().getApplicationContext();
        CharSequence text;
        int duration;
        Toast toast;

        if(numero_bolas == 0){
            text = "Aún no han salido bolas";
            duration = Toast.LENGTH_SHORT;
            toast = Toast.makeText(c, text, duration);
            toast.show();
        }
        else {
            if(posicion_actual<2){
                text = "No hay más bolas anteriores";
                duration = Toast.LENGTH_SHORT;
                toast = Toast.makeText(c, text, duration);
                toast.show();
            }
            else{
                posicion_actual = posicion_actual -2;
                bombo.setPosicion_actual(posicion_actual);
                bola = bombo.getBola(posicion_actual);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv.setText(Integer.toString(bola));
                    }
                });
                posicion_actual++;
                bombo.setPosicion_actual(posicion_actual);
            }
        }
    }

    public boolean bolaSiguiente() {
        final int bola;
        final Context c;
        final CharSequence text;
        final int duration;
        if(posicion_actual == bombo.getMaxBolas()){
            c = getActivity().getApplicationContext();
            text = "Ya no quedan bolas que mostrar";
            duration = Toast.LENGTH_SHORT;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast toast = Toast.makeText(c, text, duration);
                    toast.show();
                }
            });
            return true;
        }
        else{
            bola = bombo.getBola(posicion_actual);
            ultimaBola = -1;

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv.setText(Integer.toString(bola));
                }
            });
            posicion_actual++;
            bombo.setPosicion_actual(posicion_actual);
            if(posicion_actual>numero_bolas) {
                ultimaBola = bola;
                numero_bolas++;
                bombo.setNumbolas(numero_bolas);
            }
            return false;
        }
    }

    public int getUltimaBola(){
        return ultimaBola;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String nombreboton);
    }

}
