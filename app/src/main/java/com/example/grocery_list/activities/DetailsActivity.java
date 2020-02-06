package com.example.grocery_list.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.grocery_list.model.ListItem;
import com.example.grocery_list.R;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {

    public TextView name, quantity, date;
    public Button edit, delete;
    public Bundle extras;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        extras = getIntent().getExtras();
        ListItem item = extras.getParcelable("item");


            name = (TextView) findViewById(R.id.detailsItem);
            quantity = (TextView) findViewById(R.id.detailsQuantity);
            date = (TextView) findViewById(R.id.detailsDate);
            edit = (Button) findViewById(R.id.detailsEditButton);
            delete = (Button) findViewById(R.id.detailsDeleteButton);

            name.setText(item.getName());
            quantity.setText(item.getQuantity());
            date.setText(item.getDate());

            edit.setOnClickListener(this);
            delete.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        extras = getIntent().getExtras();
        ListItem item = extras.getParcelable("item");

        switch (v.getId()){

            case R.id.detailsEditButton:

                editPopup(item);

                break;

            case R.id.detailsDeleteButton:

                deletePopup(item);

                break;

        }

    }

    private void editPopup(final ListItem item){

        final EditText editName, editQuantity;
        Button editButton;
        LayoutInflater inflater;
        AlertDialog.Builder alertDialogBuilder;
        final AlertDialog alertDialog;
        View view;

        inflater = LayoutInflater.from(getBaseContext());
        view = inflater.inflate(R.layout.edit_item, null);

        editName = (EditText) view.findViewById(R.id.editNameInput);
        editQuantity = (EditText) view.findViewById(R.id.editQuantityInput);
        editButton = (Button) view.findViewById(R.id.editSaveButton);

        editName.setText(item.getName());
        editQuantity.setText(item.getQuantity());

        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(view);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edit( item, editName, editQuantity );

            }
        });

    }

    private void edit(ListItem item, EditText editName, EditText editQuantity){

        final int position = extras.getInt("position");

        if ( isEmpty(editName) && isEmpty(editQuantity) ) {

            Intent returnIntent = getIntent();
            returnIntent.putExtra("action", "edit");
            returnIntent.putExtra("position", -1);
            returnIntent.putExtra("item", item);
            setResult(RESULT_OK, returnIntent);
            finish();

        }else if( equals(editName.getText().toString(), item.getName()) && equals(editQuantity.getText().toString(), item.getQuantity())) {

            Intent returnIntent = getIntent();

            returnIntent.putExtra("action", "edit");
            returnIntent.putExtra("position", -1);
            returnIntent.putExtra("item", item);
            setResult(RESULT_OK, returnIntent);
            finish();

        } else{

            item.setName(editName.getText().toString());
            item.setQuantity(editQuantity.getText().toString());

            Intent returnIntent = getIntent();
            returnIntent.putExtra("action", "edit");
            returnIntent.putExtra("position", position);
            returnIntent.putExtra("item", item);
            setResult(RESULT_OK, returnIntent);
            finish();

        }
    }

    private void deletePopup( final ListItem item ) {

        extras = getIntent().getExtras();
        final int position = extras.getInt("position");

        LayoutInflater inflater;
        AlertDialog.Builder alertDialogBuilder;
        final AlertDialog alertDialog;
        View view;
        Button sim, nao;


        inflater = LayoutInflater.from(this);
        view = inflater.inflate(R.layout.confirmation_popup, null);

        sim = (Button) view.findViewById(R.id.YesButton);
        nao = (Button) view.findViewById(R.id.NoButton);

        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(view);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        sim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                delete( item, position );

            }
        });

        nao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });

    }

    private void delete(ListItem item, int position){

        Intent returnIntent = getIntent();
        returnIntent.putExtra("action", "delete");
        returnIntent.putExtra("position", position);
        returnIntent.putExtra("item", item);
        setResult(RESULT_OK, returnIntent);
        finish();

    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    private boolean equals( String str1, String str2){

        str1.replaceAll("\\P{Print}","");
        str2.replaceAll("\\P{Print}","");

        return str1.equals(str2);
    }
}
