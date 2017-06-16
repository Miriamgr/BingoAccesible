package com.mgoll.bingoaccesible.presentador;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;


import com.mgoll.bingoaccesible.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BolasSalidasFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BolasSalidasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BolasSalidasFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    private Button boton;

    private String modo;

    private OnFragmentInteractionListener mListener;

    public BolasSalidasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mod Parameter 1.
     * @return A new instance of fragment BolasSalidasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BolasSalidasFragment newInstance(String mod) {
        BolasSalidasFragment fragment = new BolasSalidasFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, mod);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            modo = getArguments().getString(ARG_PARAM1);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_bolas_salidas, container, false);
        FrameLayout fl = (FrameLayout) vista;

        RecyclerView rv = (RecyclerView) fl.findViewById(R.id.rv_bolassalidas);

        boton = (Button) fl.findViewById(R.id.boton_mostrartodos);

        boton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View vista) {
                        botonPulsado(vista);
                    }
                });

        if(modo!= null) {
            if (modo.matches("Entero")) {
                FrameLayout.LayoutParams flP = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                fl.setLayoutParams(flP);
            }
        }
        return fl;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void botonPulsado(View vista) {

        String s = ((Button) vista).getText().toString();

        if (mListener != null) {
            if(s.equals("Mostrar bolas salidas")){
                mListener.onFragmentInteraction("MOSTRARTODOS");
            }
            else if(s.equals("Ocultar bolas salidas")){
                mListener.onFragmentInteraction("OCULTARTODOS");
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

    public void cambiarNombreBoton(String nombre){
        if(nombre.matches("ocultar")){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    boton.setText("Ocultar bolas salidas");
                }
            });
        }
        else
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    boton.setText("Mostrar bolas salidas");
                }
            });
    }
}
