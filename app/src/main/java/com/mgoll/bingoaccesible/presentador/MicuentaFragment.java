package com.mgoll.bingoaccesible.presentador;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mgoll.bingoaccesible.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static com.mgoll.bingoaccesible.presentador.PreferencesFragment.KEY_PREF_BJ;
import static com.mgoll.bingoaccesible.presentador.PreferencesFragment.KEY_PREF_LJ;
import static com.mgoll.bingoaccesible.presentador.PreferencesFragment.KEY_PREF_MODO;
import static com.mgoll.bingoaccesible.presentador.PreferencesFragment.KEY_PREF_NOMBRE;
import static com.mgoll.bingoaccesible.presentador.PreferencesFragment.KEY_PREF_PMBJ;
import static com.mgoll.bingoaccesible.presentador.PreferencesFragment.KEY_PREF_PMCJ;
import static com.mgoll.bingoaccesible.presentador.PreferencesFragment.KEY_PREF_PMCOJ;
import static com.mgoll.bingoaccesible.presentador.PreferencesFragment.KEY_PREF_PT;
import static com.mgoll.bingoaccesible.presentador.PreferencesFragment.KEY_PREF_VELOCIDAD;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MicuentaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MicuentaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MicuentaFragment extends Fragment {

    private TextView tv_bombo;
    private TextView tv_completo;
    private TextView tv_carton;
    private TextView tv_total;
    private TextView tv_terminadas;
    private TextView tv_bingo;
    private TextView tv_linea;
    private TextView tv_nombre;

    private OnFragmentInteractionListener mListener;

    public MicuentaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MicuentaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MicuentaFragment newInstance() {
        MicuentaFragment fragment = new MicuentaFragment();
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
        vista = inflater.inflate(R.layout.fragment_micuenta, container, false);

        tv_nombre = (TextView) vista.findViewById(R.id.tv_usuario_cuenta);
        tv_bombo = (TextView) vista.findViewById(R.id.tv_pmbj);
        tv_completo = (TextView) vista.findViewById(R.id.tv_pmcoj);
        tv_carton = (TextView) vista.findViewById(R.id.tv_pmcj);
        tv_total = (TextView) vista.findViewById(R.id.tv_tpj);
        tv_terminadas = (TextView) vista.findViewById(R.id.tv_pt);
        tv_bingo = (TextView) vista.findViewById(R.id.tv_bg);
        tv_linea = (TextView) vista.findViewById(R.id.tv_lg);

        ((Button) vista.findViewById(R.id.boton_resetear_estadisticas)) //Establecemos el listener del botón pulsado
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View vista) {
                        botonPulsado();
                    }
                });

        cargarTextView();

        return vista;
    }

    private void cargarTextView() {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        int pb, pc, pco, pj, pt, bj, lj;

        pb = Integer.parseInt(sp.getString(KEY_PREF_PMBJ, "0"));
        pc = Integer.parseInt(sp.getString(KEY_PREF_PMCJ, "0"));
        pco = Integer.parseInt(sp.getString(KEY_PREF_PMCOJ, "0"));
        pj = pb + pc + pco;
        pt = Integer.parseInt(sp.getString(KEY_PREF_PT, "0"));
        bj = Integer.parseInt(sp.getString(KEY_PREF_BJ, "0"));
        lj = Integer.parseInt(sp.getString(KEY_PREF_LJ, "0"));

        tv_nombre.setText(sp.getString(KEY_PREF_NOMBRE, "Usuario"));

        tv_bombo.setText("Partidas modo bombo jugadas: " + pb);
        tv_completo.setText("Partidas modo completo jugadas: " + pco);
        tv_carton.setText("Partidas modo cartón jugadas: " + pc);
        tv_total.setText("Total partidas jugadas: " + pj);
        tv_terminadas.setText("Partidas terminadas: " + pt);
        tv_bingo.setText("Bingos ganados: " + bj);
        tv_linea.setText("Líneas ganadas: " + lj);
    }

    public void botonPulsado() {

        if (mListener != null) {
            mListener.onFragmentInteraction("Resetear");
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
        void onFragmentInteraction(String nombreBoton);
    }
}
