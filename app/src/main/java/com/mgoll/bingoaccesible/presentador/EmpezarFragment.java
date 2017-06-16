package com.mgoll.bingoaccesible.presentador;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.mgoll.bingoaccesible.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EmpezarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EmpezarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmpezarFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public EmpezarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EmpezarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EmpezarFragment newInstance(String param1, String param2) {
        EmpezarFragment fragment = new EmpezarFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View vista;
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_empezar, container, false);
        ((ImageButton) vista.findViewById(R.id.boton_empezar)) //Establecemos el listener del botón pulsado
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        botonPulsado(view);
                    }
                });
        ((Button) vista.findViewById(R.id.boton_anterior_des)).setEnabled(false);
        ((Button) vista.findViewById(R.id.boton_siguiente_des)).setEnabled(false);
        ((Button) vista.findViewById(R.id.boton_parar_des)).setEnabled(false);
        ((Button) vista.findViewById(R.id.boton_continuar_des)).setEnabled(false);
        return vista;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void botonPulsado(View vista) {
        if (mListener != null) {
            mListener.onFragmentInteraction("EMPEZAR");
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
