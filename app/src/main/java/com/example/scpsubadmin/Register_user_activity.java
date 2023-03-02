package com.example.scpsubadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
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
    EditText userName_editText;
    EditText pin_editText;
    Button register_Button;

    DatabaseReference databaseReference;
    DatabaseReference usernameCheckReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        setTitle("Enter User Details");
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        tokenNumber_textView = findViewById(R.id.tokenNumber_textView);
        tokenNumber_textView.setText(getIntent().getStringExtra("TOKEN"));
        name_editText = findViewById(R.id.name_editText);
        carNo_editText = findViewById(R.id.carNo_editText);
        phno_editText = findViewById(R.id.phno_editText);
        userName_editText = findViewById(R.id.userName_editText);
        pin_editText = findViewById(R.id.pin_editText);
        register_Button = findViewById(R.id.register_Button);

        usernameCheckReference = FirebaseDatabase.getInstance().getReference("Parking_users");

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

                if (userName_editText.getText().toString().isEmpty()){
                    userName_editText.setError("Enter username");
                    valid = false;
                }

                if (pin_editText.getText().toString().isEmpty()){
                    pin_editText.setError("Enter pin");
                    valid = false;
                }


                if (valid){

                    usernameCheckReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                                    for(DataSnapshot dataSnapshot2:dataSnapshot.getChildren()){
                                        if (dataSnapshot2.getValue().toString().equals(userName_editText.getText().toString())){
                                            Toast.makeText(Register_user_activity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                                        }else {
                                            dataUpload();
                                        }
                                    }

                                }
                            }else {
                                dataUpload();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }

            }
        });
    }

    private void dataUpload() {
        databaseReference.child("Users").child(userName_editText.getText().toString()).child("name").setValue(name_editText.getText().toString());
        databaseReference.child("Users").child(userName_editText.getText().toString()).child("car_no").setValue(carNo_editText.getText().toString());
        databaseReference.child("Users").child(userName_editText.getText().toString()).child("ph_no").setValue(phno_editText.getText().toString());
        databaseReference.child("Users").child(userName_editText.getText().toString()).child("pin").setValue(pin_editText.getText().toString());
        databaseReference.child("Users").child(userName_editText.getText().toString()).child("parking").setValue(AdminDetails_class.getInstance().parking);
        databaseReference.child("Users").child(userName_editText.getText().toString()).child("token").setValue(getIntent().getStringExtra("TOKEN"));

        databaseReference.child("Wallet").child(userName_editText.getText().toString()).setValue(5000);

        databaseReference.child("Tokens").child(getIntent().getStringExtra("TOKEN")).setValue(userName_editText.getText().toString());

        databaseReference.child("Subscription").child(userName_editText.getText().toString()).child("valid").setValue("0");
        databaseReference.child("Subscription").child(userName_editText.getText().toString()).child("Duration").setValue("0");
        databaseReference.child("Subscription").child(userName_editText.getText().toString()).child("due_date").setValue("0");
        databaseReference.child("Subscription").child(userName_editText.getText().toString()).child("time").setValue("0");

        databaseReference.child("Parking_users").child(AdminDetails_class.getInstance().parking).child(userName_editText.getText().toString()).setValue(userName_editText.getText().toString());

        String pushID = databaseReference.push().getKey().toString();
        databaseReference.child("Logs").child(pushID).child("subadmin").setValue(AdminDetails_class.getInstance().getName());
        databaseReference.child("Logs").child(pushID).child("description").setValue("created user "+userName_editText.getText().toString()+" with token number "+getIntent().getStringExtra("TOKEN"));



        finish();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

}