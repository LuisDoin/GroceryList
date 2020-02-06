package com.example.grocery_list.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocery_list.activities.AddFirstItem;
import com.example.grocery_list.activities.DetailsActivity;
import com.example.grocery_list.data.DataBaseHandler;
import com.example.grocery_list.R;

import java.util.List;

import com.example.grocery_list.model.ListItem;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private Context context;
    private List<ListItem> listItem;

    public MyAdapter (Context context, List<ListItem> listItem){

        this.listItem = listItem;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.items, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {

        ListItem  item = listItem.get(position);

        holder.name.setText(item.getName());
        holder.quantity.setText(item.getQuantity());
        holder.data.setText(item.getDate());

    }

    @Override
    public int getItemCount() {

        return listItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView name, quantity, data;
        private Button edit, delete;

        private ViewHolder(@NonNull View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.item);
            quantity = (TextView) view.findViewById(R.id.quantity);
            data = (TextView) view.findViewById(R.id.date);
            edit = (Button) view.findViewById(R.id.edit);
            delete = (Button) view.findViewById(R.id.delete);

            edit.setOnClickListener(this);
            delete.setOnClickListener(this);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            switch (v.getId()){

                case R.id.edit:

                    editPopup();

                    break;

                case R.id.delete:

                    deletePopup();

                    break;

                case R.id.itensParent:

                    final int REQUEST_CODE = 1;
                    Intent intent = new Intent(context, DetailsActivity.class);

                    intent.putExtra("item", listItem.get(getAdapterPosition()));
                    intent.putExtra("position", getAdapterPosition());
                    ((Activity) context).startActivityForResult(intent, REQUEST_CODE);

                    break;
            }
        }

        private void editPopup(){

            final ListItem item = listItem.get(getAdapterPosition());
            final EditText editName, editQuantity;
            Button editButton;
            LayoutInflater inflater;
            AlertDialog.Builder alertDialogBiulder;
            final AlertDialog alertDialog;
            View view;

            inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.edit_item, null);

            editName = (EditText) view.findViewById(R.id.editNameInput);
            editQuantity = (EditText) view.findViewById(R.id.editQuantityInput);
            editButton = (Button) view.findViewById(R.id.editSaveButton);

            editName.setText(item.getName());
            editQuantity.setText(item.getQuantity());


            alertDialogBiulder = new AlertDialog.Builder(context);
            alertDialogBiulder.setView(view);
            alertDialog = alertDialogBiulder.create();
            alertDialog.show();

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    edit(item, editName, editQuantity);

                    alertDialog.dismiss();

                }
            });
        }

        private void edit(ListItem item, EditText editName, EditText editQuantity){

            DataBaseHandler db = new DataBaseHandler(context);

            if ( isEmpty(editName) && isEmpty(editQuantity) )

                Toast.makeText(context, "No changes made", Toast.LENGTH_SHORT).show();

            else{

                if( equals(editName.getText().toString(), item.getName()) &&  equals(editQuantity.getText().toString(), item.getQuantity()) )

                    Toast.makeText(context, "No changes made", Toast.LENGTH_SHORT).show();

                else{

                    item.setName(editName.getText().toString());
                    item.setQuantity(editQuantity.getText().toString());
                    db.updateItem(item);

                    listItem.clear();
                    listItem.addAll(db.getAllItems());

                    notifyDataSetChanged();

                }
            }
        }

        private void deletePopup() {

            LayoutInflater inflater;
            AlertDialog.Builder alertDialogBiulder;
            final AlertDialog alertDialog;
            View view;
            Button sim, nao;

            inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.confirmation_popup, null);

            sim = (Button) view.findViewById(R.id.YesButton);
            nao = (Button) view.findViewById(R.id.NoButton);

            alertDialogBiulder = new AlertDialog.Builder(context);
            alertDialogBiulder.setView(view);
            alertDialog = alertDialogBiulder.create();
            alertDialog.show();

            sim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    delete();

                    alertDialog.dismiss();

                }
            });

            nao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    alertDialog.dismiss();
                }
            });

        }

        private void delete(){

            DataBaseHandler db = new DataBaseHandler(context);
            ListItem item = listItem.get(getAdapterPosition());
            db.deleteItem(item.getId());

            listItem.clear();
            listItem.addAll(db.getAllItems());

            notifyDataSetChanged();

            if (db.getItensCount() == 0){

                final int REQUEST_CODE = 2;
                Intent intent = new Intent(context, AddFirstItem.class);
                ((Activity) context).startActivityForResult(intent, REQUEST_CODE);
            }

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
}
