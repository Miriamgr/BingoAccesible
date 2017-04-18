package com.mgoll.bingoaccesible.presentador;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mgoll.bingoaccesible.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BotonesmainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class BotonesmainFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public BotonesmainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View vista; // Creamos la vista
        vista = inflater.inflate(R.layout.fragment_botonesmain, container, false); //"Inflamos" la vista del fragment
        ((Button) vista.findViewById(R.id.boton_bombo)) //Establecemos el listener del botón pulsado
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        botonPulsado(view);
                    }
                });
        ((Button) vista.findViewById(R.id.boton_carton)) //Establecemos el listener del botón pulsado
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        botonPulsado(view);
                    }
                });
        ((Button) vista.findViewById(R.id.boton_completo)) //Establecemos el listener del botón pulsado
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        botonPulsado(view);
                    }
                });
        return vista;// devolvemos la vista
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void botonPulsado(View v) {
        if (mListener != null) {

            String s = ((Button) v).getText().toString();
            if(s.equals("Modo Completo")){
                mListener.onFragmentInteraction("COMP");
            }
            else if(s.equals("Modo bombo")){
                mListener.onFragmentInteraction("BOMB");
            }
            else if(s.equals("Modo Cartón")){
                mListener.onFragmentInteraction("CART");
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
