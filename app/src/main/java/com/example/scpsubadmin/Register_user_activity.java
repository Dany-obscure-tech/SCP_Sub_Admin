package com.example.scpsubadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register_user_activity extends AppCompatActivity {
    TextView tokenNumber_textView;
    EditText name_editText;
    EditText carNo_editText;
    EditText phno_editText;
    EditText pin_editText;
    Button register_Button;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        setTitle("Enter User Details");

        tokenNumber_textView = findViewById(R.id.tokenNumber_textView);
        tokenNumber_textView.setText(getIntent().getStringExtra("TOKEN"));
        name_editText = findViewById(R.id.name_editText);
        carNo_editText = findViewById(R.id.carNo_editText);
        phno_editText = findViewById(R.id.phno_editText);
        pin_editText = findViewById(R.id.pin_editText);
        register_Button = findViewById(R.id.register_Button);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        register_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean valid = true;
                if (name_editText.getText().toString().isEmpty()){
                    name_editText.setError("Enter email");
                    valid = false;
                }
                if (carNo_editText.getText().toString().isEmpty()){
                    carNo_editText.setError("Enter email");
                    valid = false;
                }
                if (phno_editText.getText().toString().isEmpty()){
                    phno_editText.setError("Enter email");
                    valid = false;
                }

                if (pin_editText.getText().toString().isEmpty()){
                    pin_editText.setError("Enter pin");
                    valid = false;
                }


                if (valid){
                    databaseReference.child("Users").child(getIntent().getStringExtra("TOKEN")).child("name").setValue(name_editText.getText().toString());
                    databaseReference.child("Users").child(getIntent().getStringExtra("TOKEN")).child("car_no").setValue(carNo_editText.getText().toString());
                    databaseReference.child("Users").child(getIntent().getStringExtra("TOKEN")).child("ph_no").setValue(phno_editText.getText().toString());
                    databaseReference.child("Users").child(getIntent().getStringExtra("TOKEN")).child("pin").setValue(pin_editText.getText().toString());
                    databaseReference.child("Users").child(getIntent().getStringExtra("TOKEN")).child("parking").setValue(AdminDetails_class.getInstance().parking);

                    databaseReference.child("Wallet").child(getIntent().getStringExtra("TOKEN")).setValue(5000);

                    databaseReference.child("Subscription").child(getIntent().getStringExtra("TOKEN")).child("valid").setValue("0");
                    databaseReference.child("Subscription").child(getIntent().getStringExtra("TOKEN")).child("Duration").setValue("0");

                    databaseReference.child("Parking_users").child(AdminDetails_class.getInstance().parking).child(getIntent().getStringExtra("TOKEN")).setValue(getIntent().getStringExtra("TOKEN"));

                    finish();
                }

            }
        });
    }

}