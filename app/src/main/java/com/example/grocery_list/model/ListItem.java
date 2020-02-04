package com.example.grocery_list.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ListItem implements Parcelable {

    private String nome;
    private String quantidade;
    private String data;
    private int id;

    public ListItem(){

    }

    public ListItem(Parcel in) {
        nome = in.readString();
        quantidade = in.readString();
        data = in.readString();
        id = in.readInt();
    }

    public ListItem(String nome, String quantidade, int id) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.id = id;
    }

    public ListItem(String nome, String quantidade) {
        this.nome = nome;
        this.quantidade = quantidade;


    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(nome);
        dest.writeString(quantidade);
        dest.writeString(data);
        dest.writeInt(id);

    }

    public static final Parcelable.Creator<ListItem> CREATOR = new Parcelable.Creator<ListItem>(){

        public ListItem createFromParcel(Parcel in) {
            return new ListItem(in);
        }

        public ListItem[] newArray(int size) {
            return new ListItem[size];
        }
    };
}



