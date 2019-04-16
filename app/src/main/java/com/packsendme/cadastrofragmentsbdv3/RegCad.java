package com.packsendme.cadastrofragmentsbdv3;

import android.os.Parcel;
import android.os.Parcelable;

public class RegCad implements Parcelable {
    protected String nome;
    protected String endereco;
    protected String telefone;
    protected RegCad ant;
    protected RegCad prox;

    protected RegCad(Parcel in) {
        nome = in.readString();
        endereco = in.readString();
        telefone = in.readString();
        ant = in.readParcelable(RegCad.class.getClassLoader());
        prox = in.readParcelable(RegCad.class.getClassLoader());
    }

    protected RegCad(){

    }

    public static final Creator<RegCad> CREATOR = new Creator<RegCad>() {
        @Override
        public RegCad createFromParcel(Parcel in) {
            return new RegCad(in);
        }

        @Override
        public RegCad[] newArray(int size) {
            return new RegCad[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nome);
        dest.writeString(endereco);
        dest.writeString(telefone);
        dest.writeParcelable(ant, flags);
        dest.writeParcelable(prox, flags);
    }
}
