package com.packsendme.cadastrofragmentsbdv3;

import android.provider.BaseColumns;

public final class CadastroContract {
    private CadastroContract(){}//pra ninguem instanciar sem querer
    public static class CadastroEntry implements BaseColumns{
        public static final String TABLE_NAME="listaclientes";
        public static final String COLUMN_NAME_NOME="nome";
        public static final String COLUMN_NAME_ENDERECO="endereco";
        public static final String COLUMN_NAME_TELEFONE="telefone";
    }
}
