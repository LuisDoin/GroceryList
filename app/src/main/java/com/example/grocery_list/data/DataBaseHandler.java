package com.example.grocery_list.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.grocery_list.model.ListItem;
import com.example.grocery_list.util.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataBaseHandler extends SQLiteOpenHelper {

    private Context context;

    public DataBaseHandler(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_LISTA_TABLE = "CREATE TABLE " + Constants.TABLE_NAME + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY," + Constants.KEY_ITEM + " TEXT,"
                + Constants.KEY_QUANTIDADE + " TEXT," + Constants.KEY_DATA_ITEM + " LONG);";

        db.execSQL(CREATE_LISTA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);

        onCreate(db);
    }

    public void addItem(ListItem item){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_ITEM, item.getNome());
        values.put(Constants.KEY_QUANTIDADE, item.getQuantidade());
        values.put(Constants.KEY_DATA_ITEM, java.lang.System.currentTimeMillis());

        db.insert(Constants.TABLE_NAME, null, values);

    }

    public ListItem getItem(int id){

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(Constants.TABLE_NAME, new String[] {Constants.KEY_ID, Constants.KEY_ITEM, Constants.KEY_QUANTIDADE, Constants.KEY_DATA_ITEM},
                Constants.KEY_ID + "=?", new String[] {String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        ListItem item = new ListItem();
        item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
        item.setNome(cursor.getString(cursor.getColumnIndex(Constants.KEY_ITEM)));
        item.setQuantidade(cursor.getString(cursor.getColumnIndex(Constants.KEY_QUANTIDADE)));

        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        String formatedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATA_ITEM))).
                getTime());

        item.setData(formatedDate);

        cursor.close();

        return item;
    }

    public List<ListItem> getAllItens(){

        SQLiteDatabase db = this.getReadableDatabase();

        List<ListItem> itemList = new ArrayList<>();

        Cursor cursor = db.query(Constants.TABLE_NAME, new String[] {
                Constants.KEY_ID, Constants.KEY_ITEM, Constants.KEY_QUANTIDADE,
                Constants.KEY_DATA_ITEM}, null, null, null, null,
                Constants.KEY_DATA_ITEM + " DESC");

        if (cursor.moveToFirst()){
            do{
                ListItem item = new ListItem();

                item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
                item.setNome(cursor.getString(cursor.getColumnIndex(Constants.KEY_ITEM)));
                item.setQuantidade(cursor.getString(cursor.getColumnIndex(Constants.KEY_QUANTIDADE)));

                @SuppressLint("SimpleDateFormat") SimpleDateFormat formater = new SimpleDateFormat("dd/MM  hh:mm a");
                String formatedDate = formater.format(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATA_ITEM))
                         - 1000*3600*3 );

                item.setData(formatedDate);

                itemList.add(item);

            }while (cursor.moveToNext());
        }

        cursor.close();

        return itemList;
    }

    public int updateItem(ListItem item){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_ITEM, item.getNome());
        values.put(Constants.KEY_QUANTIDADE, item.getQuantidade());
        values.put(Constants.KEY_DATA_ITEM, java.lang.System.currentTimeMillis());


        return db.update(Constants.TABLE_NAME, values, Constants.KEY_ID + "=?", new String[] {String.valueOf(item.getId())});
    }

    public void deleteItem(int id){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_NAME, Constants.KEY_ID + "=?", new String [] {String.valueOf(id)});

        db.close();

    }

    public int getItensCount(){

        int count;

        String countQuery = "SELECT * FROM " + Constants.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);
        count = cursor.getCount();

        cursor.close();

        return count;
    }
}
