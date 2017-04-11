package com.mgoll.bingoaccesible.presentador;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
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
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BolasSalidasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BolasSalidasFragment newInstance(String mod) {
        BolasSalidasFragment fragment = new BolasSalidasFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, mod);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            modo = getArguments().getString(ARG_PARAM1);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_bolas_salidas, container, false);
        FrameLayout fl = (FrameLayout) vista;

        RecyclerView rv = (RecyclerView) fl.findViewById(R.id.rv_bolassalidas);

        if(modo!= null && modo.matches("Entero")) {
            // GridLayout.LayoutParams rlP = new GridLayout.LayoutParams(GridLayout.LayoutParams.MATCH_PARENT, GridLayout.LayoutParams.MATCH_PARENT);

            // rv.setLayoutParams(new LayoutParams());
        }

        boton = (Button) fl.findViewById(R.id.boton_mostrartodos);

        boton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View vista) {
                        botonPulsado(vista);
                    }
                });

        // Inflate the layout for this fragment
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
        if (mListener != null) {
            if(((Button) vista).getText().equals("Mostrar bolas salidas")){
                mListener.onFragmentInteraction("MOSTRARTODOS");
            }
            else if(((Button) vista).getText().equals("Ocultar bolas salidas")){
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
