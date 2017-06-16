package com.mgoll.bingoaccesible.presentador;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mgoll.bingoaccesible.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BomboFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BomboFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BomboFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters

    private String mod;

    private Button boton_mostrar;
    private View vista;
    private TextView tv;
    private RelativeLayout rl;


    private OnFragmentInteractionListener mListener;

    public BomboFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param modo Parameter 1.
     * @return A new instance of fragment BomboFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BomboFragment newInstance(String modo) {
        BomboFragment fragment = new BomboFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, modo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
           mod = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        vista = inflater.inflate(R.layout.fragment_bombo, container, false); //"Inflamos" la vista del fragment

        if(mod !=null && mod.equals("completo")){
            vista.findViewById(R.id.rl_subfragmento1).setVisibility(View.VISIBLE);
        }
        else{
            vista.findViewById(R.id.rl_subfragmento1).setVisibility(View.GONE);
        }
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

        return vista;
    }

    // TODO: Rename method, update argument and hook method into UI event
    private void botonPulsado(View v) {

        String s = ((Button) v).getText().toString();
        if (mListener != null) {
            if(s.equals("Atras")){
                mListener.onFragmentInteraction("ATRAS");
            }
            else if(s.equals("Siguiente")){
                mListener.onFragmentInteraction("SIG");
            }
            else if(s.equals("Parar")){
                mListener.onFragmentInteraction("PARAR");
            }
            else if(s.equals("Continuar")){
                mListener.onFragmentInteraction("CONT");
            }
            else if(s.equals("Mostrar bolas salidas")){
                mListener.onFragmentInteraction("MOSTRARTODOS");
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

    public void actualizaNumBola(int b){
        final int bola = b;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv.setText(Integer.toString(bola));
                tv.setContentDescription(Integer.toString(bola));
            }
        });
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
