package io.walter.realmcrud;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {
    TextView tvNames, tvEmail, tvAge, tvAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        tvNames = (TextView) findViewById(R.id.tvNames);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvAge = (TextView) findViewById(R.id.tvAge);
        int id = getIntent().getIntExtra("id", 0);
        PersonModel personModel = MainActivity.getInstance().searchPerson(id);
        if (personModel != null) {
            tvNames.setText(personModel.getName());
            tvAge.setText(String.valueOf(personModel.getAge()));
            tvAddress.setText(personModel.getAddress());
            tvEmail.setText(personModel.getEmail());
        }

    }
}
