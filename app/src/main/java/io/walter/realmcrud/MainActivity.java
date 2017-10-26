package io.walter.realmcrud;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    //http://www.theappguruz.com/blog/realm-mobile-database-implementation-in-android
    ListView listPersons;
    Realm myRealm;
    CustomListAdapter adapter;
    ArrayList<PersonModel> personsArray;
    private static MainActivity instance;
    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listPersons = (ListView) findViewById(R.id.listPersons);
        myRealm = Realm.getInstance(MainActivity.this);
        personsArray = new ArrayList<>();
        adapter = new CustomListAdapter(this, personsArray);
        listPersons.setAdapter(adapter);
        instance=this;
        getAllUsers();
        listPersons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long identifier) {
                PersonModel model= personsArray.get(position);
                int id= model.getId();
                Intent intent =new Intent(getApplicationContext(), DetailsActivity.class );
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

    }

    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_add) {
            //ADD
            show_add_dialog();
        }
        return true;
    }

    private void show_add_dialog() {
        LayoutInflater inflator = LayoutInflater.from(this);
        final View add_view = inflator.inflate(R.layout.prompt_dialog, null);
        final AlertDialog addDialog = new AlertDialog.Builder(this).setTitle("New Person").create();
        addDialog.setView(add_view);
        final EditText edtNames = (EditText) add_view.findViewById(R.id.etAddPersonName);
        final EditText edtEmail = (EditText) add_view.findViewById(R.id.etAddPersonEmail);
        final EditText edtAddress = (EditText) add_view.findViewById(R.id.etAddPersonAddress);
        final EditText edtAge = (EditText) add_view.findViewById(R.id.etAddPersonAge);
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
                PersonModel model = new PersonModel(id, names, email, address, age);
                saveToRealm(model);
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        personsArray.clear();
        myRealm.close();
    }

    private void getAllUsers() {
        RealmResults<PersonModel> results = myRealm.where(PersonModel.class).findAll();
        myRealm.beginTransaction();
        for (int i = 0; i < results.size(); i++) {
            personsArray.add(results.get(i));
        }
        if (results.size() > 0)
            id = myRealm.where(PersonModel.class).max("id").intValue() + 1;
        myRealm.commitTransaction();
        adapter.notifyDataSetChanged();
    }

    private void saveToRealm(PersonModel model) {
        myRealm.beginTransaction();
        PersonModel personModel = myRealm.copyToRealm(model);
        personsArray.add(personModel);
        myRealm.commitTransaction();
        adapter.notifyDataSetChanged();
        id++;
    }

    public void deleteFromRealm(int id, int position) {
        RealmResults<PersonModel> results = myRealm.where(PersonModel.class).equalTo("id", id).findAll();
        myRealm.beginTransaction();
        results.remove(0);
        myRealm.commitTransaction();
        personsArray.remove(position);
        adapter.notifyDataSetChanged();
    }

    public void updateToReal(PersonModel new_model, int id, int position) {
        PersonModel model = myRealm.where(PersonModel.class).equalTo("id", id).findFirst();
        myRealm.beginTransaction();
        model.setName(new_model.getName());
        model.setEmail(new_model.getEmail());
        model.setAddress(new_model.getAddress());
        model.setAge(new_model.getAge());
        myRealm.commitTransaction();
        personsArray.set(position, new_model);
        adapter.notifyDataSetChanged();
    }

    public  PersonModel searchPerson(int id){
        RealmResults<PersonModel> results = myRealm.where(PersonModel.class).equalTo("id",id).findAll();
        myRealm.beginTransaction();
        myRealm.commitTransaction();
        return  results.get(0);
    }
}
