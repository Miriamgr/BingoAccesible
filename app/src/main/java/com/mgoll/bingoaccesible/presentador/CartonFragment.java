package com.mgoll.bingoaccesible.presentador;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mgoll.bingoaccesible.R;
import com.mgoll.bingoaccesible.modelo.Carton;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CartonFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CartonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartonFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
   // private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int cartonJugado;
    private TextView numCarton;
    private Button botonMarcados;
   // private String mParam2;
    //private Carton carton;
    //private String contenido;

    private OnFragmentInteractionListener mListener;

    public CartonFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param cartonJugado Parameter 1.
     * @return A new instance of fragment CartonFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartonFragment newInstance(int cartonJugado) {
        CartonFragment fragment = new CartonFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, cartonJugado);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cartonJugado = getArguments().getInt(ARG_PARAM1);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_carton, container, false);

        numCarton = (TextView) view.findViewById(R.id.tv_numcarton);
        botonMarcados = (Button) view.findViewById(R.id.boton_marcados);
        botonMarcados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vista) {
                botonPulsado(vista);
            }
        });

        String texto = "Cartón nº: ";
        String t2 = texto.concat(Integer.toString(cartonJugado));

        numCarton.setText(t2);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void botonPulsado (View vista) {
        if (mListener != null) {
            mListener.onFragmentInteraction("MARCADOS");
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

    public String selecciona_carton(int numero){
        try
        {
            InputStream fraw = getResources().openRawResource(R.raw.cartones);

            BufferedReader brin = new BufferedReader(new InputStreamReader(fraw));
            Boolean encontrado = false;

            String linea = brin.readLine();
            String n;

            while(linea != null && !encontrado) {
                n = linea.substring(0, 1);
                if(Integer.parseInt(n)== numero)
                    encontrado = true;
                else
                    linea = brin.readLine();
            }
            fraw.close();
            return linea;
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al leer fichero desde recurso raw");
            return null;
        }
    }
}
