package com.packsendme.cadastrofragmentsbdv3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CadastroDbHelper extends SQLiteOpenHelper {
    //se mudar o schema do bd, muda a versao
    public static final int DATABASE_VERSION=1;
    public static final String DATABASE_NAME="bdcadastro2.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE "+ CadastroContract.CadastroEntry.TABLE_NAME+
            " ("+ CadastroContract.CadastroEntry._ID+" INTEGER PRIMARY KEY,"+
                    CadastroContract.CadastroEntry.COLUMN_NAME_NOME+" TEXT,"+
                    CadastroContract.CadastroEntry.COLUMN_NAME_ENDERECO+" TEXT,"+
                    CadastroContract.CadastroEntry.COLUMN_NAME_TELEFONE+" TEXT)";
    private static final String SQL_DELETE_ENTRIES=
            "DROP TABLE IF EXISTS "+ CadastroContract.CadastroEntry.TABLE_NAME;

    public CadastroDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);//COMENTAR
        onCreate(db);
    }

    //OPCIONAL
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db,oldVersion,newVersion);
    }
}
