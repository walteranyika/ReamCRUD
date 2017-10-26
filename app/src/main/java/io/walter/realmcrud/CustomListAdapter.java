package io.walter.realmcrud;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

public class CustomListAdapter extends BaseAdapter {


    private Context mContext;
    private ArrayList<PersonModel> temporaryArray;


    public CustomListAdapter(Context context, ArrayList<PersonModel> data) {
        this.mContext = context;
        this.temporaryArray = data;
    }

    @Override
    public int getCount() {
        return temporaryArray.size();// # of items in your arraylist
    }

    @Override
    public Object getItem(int position) {
        return temporaryArray.get(position);// get the actual movie
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_item_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvNames = (TextView) convertView.findViewById(R.id.tvPersonName);
            viewHolder.imgViewDelete= (ImageView) convertView.findViewById(R.id.ivDeletePerson);
            viewHolder.imgViewEdit= (ImageView) convertView.findViewById(R.id.ivEditPesonDetail);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final PersonModel personModel = temporaryArray.get(position);
        viewHolder.tvNames.setText(personModel.getName());

        viewHolder.imgViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersonModel model=temporaryArray.get(position);
                int id= model.getId();
                show_confirmDialog(id, position);
            }
        });

        viewHolder.imgViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersonModel model=temporaryArray.get(position);
                int id= model.getId();
                show_update_dialog(model,id, position);
            }
        });


        return convertView;
    }

    private void show_update_dialog(final  PersonModel model, final int id, final int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        final View add_view = inflater.inflate(R.layout.prompt_dialog, null);
        final android.support.v7.app.AlertDialog addDialog = new android.support.v7.app.AlertDialog.Builder(mContext).setTitle("Update Person").create();
        addDialog.setView(add_view);
        final EditText edtNames = (EditText) add_view.findViewById(R.id.etAddPersonName);
        edtNames.setText(model.getName());
        final EditText edtEmail = (EditText) add_view.findViewById(R.id.etAddPersonEmail);
        edtEmail.setText(model.getEmail());
        final EditText edtAddress = (EditText) add_view.findViewById(R.id.etAddPersonAddress);
        edtAddress.setText(model.getAddress());
        final EditText edtAge = (EditText) add_view.findViewById(R.id.etAddPersonAge);
        edtAge.setText(String.valueOf(model.getAge()));
        Button btnSave= (Button) add_view.findViewById(R.id.btnSave);
        Button btnClose= (Button) add_view.findViewById(R.id.btnClose);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String names = edtNames.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String address = edtAddress.getText().toString().trim();
                String age_string = edtAge.getText().toString().trim();
                if (names.isEmpty() || email.isEmpty() || address.isEmpty() || age_string.isEmpty()) {
                    Snackbar.make(add_view, "Fill in all fields", Snackbar.LENGTH_LONG).show();
                    return;
                }
                int age = Integer.parseInt(age_string);
                model.setName(names);
                model.setAge(age);
                model.setAddress(address);
                model.setEmail(email);
                MainActivity.getInstance().updateToReal(model,id,position);
                Snackbar.make(add_view, "Updated Successfully", Snackbar.LENGTH_LONG).show();
                addDialog.dismiss();
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDialog.dismiss();
            }
        });
        addDialog.show();
    }


    private void show_confirmDialog(final int id, final int position) {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(mContext);
        alertDialog
                .setTitle("Sure?")
                .setMessage("Do you really want to delete this record?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.getInstance().deleteFromRealm(id,position);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                 })
                .setCancelable(true)
                .create()
                .show();

    }

    static class ViewHolder
    {
        TextView tvNames;
        ImageView imgViewEdit;
        ImageView imgViewDelete;
    }
}
