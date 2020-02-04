package com.example.grocery_list.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.grocery_list.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddFirstItem extends AppCompatActivity {

    private FloatingActionButton fab;
    private EditText nome, quantidade;
    private Button salvarAdd;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_first_item);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addPopup();

            }
        });

    }

    private void addPopup(){

        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.addpopup, null);
        nome = (EditText) view.findViewById(R.id.nomeInput);
        quantidade = (EditText) view.findViewById(R.id.quantidadeInput);
        salvarAdd = (Button) view.findViewById(R.id.addButton);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        salvarAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( !nome.getText().toString().isEmpty() || !quantidade.getText().toString().isEmpty()) {

                    Intent returnIntent = getIntent();
                    if ( !nome.getText().toString().isEmpty() && !quantidade.getText().toString().isEmpty() ){

                        returnIntent.putExtra("nome", nome.getText().toString());
                        returnIntent.putExtra("quantidade", quantidade.getText().toString());

                        setResult(RESULT_OK, returnIntent);
                        finish();

                    }else if ( nome.getText().toString().isEmpty() && !quantidade.getText().toString().isEmpty() ){

                        returnIntent.putExtra("nome", "");
                        returnIntent.putExtra("quantidade", quantidade.getText().toString());

                        setResult(RESULT_OK, returnIntent);
                        finish();

                    }else if ( !nome.getText().toString().isEmpty() && quantidade.getText().toString().isEmpty()){

                        returnIntent.putExtra("nome", nome.getText().toString());
                        returnIntent.putExtra("quantidade", "");

                        setResult(RESULT_OK, returnIntent);
                        finish();

                    }
                }

                dialog.dismiss();

            }
        });
    }




}
