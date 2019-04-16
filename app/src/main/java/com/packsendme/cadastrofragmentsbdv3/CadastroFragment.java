package com.packsendme.cadastrofragmentsbdv3;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class CadastroFragment extends Fragment {
    //View mView;
    EditText campoNome,campoEndereco,campoTelefone;

    //Animacoes e transicao
    //Scene

    public CadastroFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_cadastro, container, false);
        // Inflate the layout for this fragment

        campoEndereco=view.findViewById(R.id.cadEndereco);
        campoNome=view.findViewById(R.id.cadNome);
        campoTelefone=view.findViewById(R.id.cadTelefone);

        return view;
    }

    @Override
    public void onViewCreated(View mView, Bundle savedInstanceState) {
        super.onViewCreated(mView, savedInstanceState);

        //(savedInstanceState!=null){
            campoEndereco=mView.findViewById(R.id.cadEndereco);
            campoNome=mView.findViewById(R.id.cadNome);
            campoTelefone=mView.findViewById(R.id.cadTelefone);

            campoNome.requestFocus();
            //campoNome.setText();
       // }
    }

    /*  void inicializaObjetos(){
        //View view=getView();
        // Inflate the layout for this fragment

        //ERRO ESTA AQUI

        campoEndereco=mView.findViewById(R.id.cadEndereco);
        campoNome=mView.findViewById(R.id.cadNome);
        campoTelefone=mView.findViewById(R.id.cadTelefone);
    }
*/
}
