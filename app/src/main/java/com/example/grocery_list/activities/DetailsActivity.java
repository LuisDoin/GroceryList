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

    public TextView nome, quantidade, data;
    public Button editar, deletar;
    public Bundle extras;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        extras = getIntent().getExtras();
        ListItem item = extras.getParcelable("item");


            nome = (TextView) findViewById(R.id.detailsItem);
            quantidade = (TextView) findViewById(R.id.detailsQuantidade);
            data = (TextView) findViewById(R.id.detailsData);
            editar = (Button) findViewById(R.id.detailsEditarButton);
            deletar = (Button) findViewById(R.id.detailsDeletarButton);

            nome.setText(item.getNome());
            quantidade.setText(item.getQuantidade());
            data.setText(item.getData());

            editar.setOnClickListener(this);
            deletar.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        extras = getIntent().getExtras();
        ListItem item = extras.getParcelable("item");

        switch (v.getId()){

            case R.id.detailsEditarButton:

                editPopup(item);

                break;

            case R.id.detailsDeletarButton:

                deletePopup(item);

                break;

        }

    }

    private void editPopup(final ListItem item){

        final EditText editNome, editQuantidade;
        Button editButton;
        LayoutInflater inflater;
        AlertDialog.Builder alertDialogBuilder;
        final AlertDialog alertDialog;
        View view;

        inflater = LayoutInflater.from(getBaseContext());
        view = inflater.inflate(R.layout.editar_item, null);

        editNome = (EditText) view.findViewById(R.id.editNomeInput);
        editQuantidade = (EditText) view.findViewById(R.id.editQuantidadeInput);
        editButton = (Button) view.findViewById(R.id.editSaveButton);

        editNome.setText(item.getNome());
        editQuantidade.setText(item.getQuantidade());

        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(view);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editar( item, editNome, editQuantidade );

            }
        });

    }

    private void editar(ListItem item, EditText editNome, EditText editQuantidade){

        final int position = extras.getInt("position");

        if ( isEmpty(editNome) && isEmpty(editQuantidade) ) {

            Intent returnIntent = getIntent();
            returnIntent.putExtra("acao", "editar");
            returnIntent.putExtra("position", -1);
            returnIntent.putExtra("item", item);
            setResult(RESULT_OK, returnIntent);
            finish();

        }else if( equals(editNome.getText().toString(), item.getNome()) && equals(editQuantidade.getText().toString(), item.getQuantidade())) {

            Intent returnIntent = getIntent();

            returnIntent.putExtra("acao", "editar");
            returnIntent.putExtra("position", -1);
            returnIntent.putExtra("item", item);
            setResult(RESULT_OK, returnIntent);
            finish();

        } else{

            item.setNome(editNome.getText().toString());
            item.setQuantidade(editQuantidade.getText().toString());

            Intent returnIntent = getIntent();
            returnIntent.putExtra("acao", "editar");
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
        returnIntent.putExtra("acao", "deletar");
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
