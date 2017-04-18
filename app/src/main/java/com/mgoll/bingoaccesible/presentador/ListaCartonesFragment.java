package com.mgoll.bingoaccesible.presentador;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mgoll.bingoaccesible.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListaCartonesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListaCartonesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListaCartonesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private static final String ARG_PARAM2 = "param2";
    private ListView lv;
    private ArrayAdapter<String> ladapter;
    private static ArrayList<String> lista;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ListaCartonesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListaCartonesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListaCartonesFragment newInstance(ArrayList<String> l) {
        ListaCartonesFragment fragment = new ListaCartonesFragment();
        Bundle args = new Bundle();

        args.putStringArrayList("cartones", l);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            lista = getArguments().getStringArrayList("cartones");
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vista;

        vista = inflater.inflate(R.layout.fragment_lista_cartones, container, false);

        lv = (ListView) vista.findViewById(R.id.lv_cartones);
        ladapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, lista);

        lv.setAdapter(ladapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String elemento = ladapter.getItem(position);
                elementoPulsado (elemento);
            }
        });
        // Inflate the layout for this fragment
        return vista;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void elementoPulsado(String s) {
        if (mListener != null) {

            int elem;
            String sub = s.substring(11);
            elem = Integer.parseInt(sub);

            mListener.onFragmentInteraction(elem);
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
        void onFragmentInteraction(int elem);
    }
}
