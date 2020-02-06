package com.example.grocery_list.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ListItem implements Parcelable {

    private String name;
    private String quantity;
    private String date;
    private int id;

    public ListItem(){

    }

    public ListItem(Parcel in) {
        name = in.readString();
        quantity = in.readString();
        date = in.readString();
        id = in.readInt();
    }

    public ListItem(String name, String quantity, int id) {
        this.name = name;
        this.quantity = quantity;
        this.id = id;
    }

    public ListItem(String name, String quantity) {
        this.name = name;
        this.quantity = quantity;


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

        dest.writeString(name);
        dest.writeString(quantity);
        dest.writeString(date);
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



