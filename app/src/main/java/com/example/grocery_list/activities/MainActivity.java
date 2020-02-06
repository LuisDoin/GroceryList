package com.example.grocery_list.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.grocery_list.adapter.MyAdapter;
import com.example.grocery_list.data.DataBaseHandler;
import com.example.grocery_list.model.ListItem;
import com.example.grocery_list.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private EditText name, quantity;
    private Button saveAdd;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private List<ListItem> listItem;
    private DataBaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DataBaseHandler(this);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addPopup();

            }
        });

        listItem = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listItem = db.getAllItems();
        adapter = new MyAdapter(this, listItem);
        recyclerView.setAdapter(adapter);

        if (db.getItensCount() == 0){

            final int REQUEST_CODE = 2;
            Intent intent = new Intent(this, AddFirstItem.class);
            startActivityForResult(intent, REQUEST_CODE);
        }

    }

    private void addPopup(){

        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.addpopup, null);
        name = (EditText) view.findViewById(R.id.nameInput);
        quantity = (EditText) view.findViewById(R.id.quantityInput);
        saveAdd = (Button) view.findViewById(R.id.addButton);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        saveAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                adicionarItem();

                dialog.dismiss();

            }
        });
    }

    public void adicionarItem(){

        if( !name.getText().toString().isEmpty() || !quantity.getText().toString().isEmpty()) {

            ListItem item = new ListItem(name.getText().toString(), quantity.getText().toString());

            db.addItem(item);

            listItem.clear();
            listItem.addAll(db.getAllItems());

            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Chamado do MyAdapter.onClick.
        if (requestCode == 1)
            if( resultCode == RESULT_OK){

                String action = data.getStringExtra("action");
                int position = data.getIntExtra("position", -1);
                ListItem item = data.getParcelableExtra("item");

                switch (action){

                    case "delete":

                        db.deleteItem(item.getId());

                        if (db.getItensCount() == 0) {

                            final int REQUEST_CODE = 2;
                            Intent intent = new Intent(this, AddFirstItem.class);
                            startActivityForResult(intent, REQUEST_CODE);
                        }else {

                            listItem.clear();
                            listItem = db.getAllItems();
                            adapter.notifyItemRemoved(position);
                            adapter = new MyAdapter(this, listItem);
                            recyclerView.setAdapter(adapter);

                        }

                    case "edit":

                        if ( position != -1 ){

                        db.updateItem(item);
                        listItem.clear();
                        listItem.addAll(db.getAllItems());
                        adapter.notifyDataSetChanged();
                        adapter = new MyAdapter(this, listItem);
                        recyclerView.setAdapter(adapter);

                        } else
                            Toast.makeText(this, "Item sem alterações", Toast.LENGTH_SHORT).show();

                }

            }

        // Chamado do MainActivity.onCreate na condição de não haver items na db ao inicializar o app.
        // Também chamado do MyAdapter.deletePopup na condição de ter sido excluído o último item da db.

        if ( requestCode == 2 )
            if ( resultCode == RESULT_OK){

                String name = data.getStringExtra("name");
                String quantity = data.getStringExtra("quantity");

                ListItem item = new ListItem(name, quantity);

                db.addItem(item);

                listItem.clear();
                listItem.addAll(db.getAllItems());
                adapter = new MyAdapter(this, listItem); //Por que não adapter.notifyItemInserted(0); ?
                recyclerView.setAdapter(adapter);

            }
    }
}

