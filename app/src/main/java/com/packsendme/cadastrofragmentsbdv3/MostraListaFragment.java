package com.packsendme.cadastrofragmentsbdv3;


import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MostraListaFragment extends Fragment {

    TextView mostraNome ,mostraTelefone,mostraEndereco;
    Button btnProximo, btnAnterior;

    public MostraListaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_mostra_lista, container, false);
        // Inflate the layout for this fragment

        mostraNome=view.findViewById(R.id.mostraNome);          //O problema
        mostraEndereco=view.findViewById(R.id.mostraEndereco);  //NAO É
        mostraTelefone=view.findViewById(R.id.mostraTelefone);  //aqui
        btnAnterior=view.findViewById(R.id.btnAnterior);
        btnProximo=view.findViewById(R.id.btnProximo);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mostraNome=view.findViewById(R.id.mostraNome);          //O problema
        mostraEndereco=view.findViewById(R.id.mostraEndereco);  //NAO É
        mostraTelefone=view.findViewById(R.id.mostraTelefone);  //aqui
        btnAnterior=view.findViewById(R.id.btnAnterior);
        btnProximo=view.findViewById(R.id.btnProximo);

        //ActionBar actionBar=getActivity()
        //mostraNome.setText("FUNFOU");
        //mostraNome.setTextSize(77);
    }
}
