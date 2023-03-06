package com.example.scpsubadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Activity_Edit_User extends AppCompatActivity {
    String username;
    String pin;
    String name;
    String email;
    String parking;
    String phno;
    String token;
    String carno;
    TextView userName_textView;
    EditText name_editText;
    TextView email_textView;
    TextView parking_textView;
    EditText phno_editText;
    TextView tokenNumber_textView;
    EditText carNo_editText;

    Button edit_Button;
    Button remove_Button;

    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        setTitle("Edit User Details");
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        username=getIntent().getStringExtra("USERNAME");
        pin=getIntent().getStringExtra("PIN");
        name=getIntent().getStringExtra("NAME");
        email=getIntent().getStringExtra("EMAIL");
        parking=getIntent().getStringExtra("PARKING");
        phno=getIntent().getStringExtra("PHNO");
        token=getIntent().getStringExtra("TOKEN");
        carno=getIntent().getStringExtra("CARNO");

        userName_textView =findViewById(R.id.userName_editText);
        name_editText=findViewById(R.id.name_editText);
        email_textView =findViewById(R.id.email_editText);
        parking_textView =findViewById(R.id.parking_TextView);
        phno_editText=findViewById(R.id.phno_editText);
        tokenNumber_textView=findViewById(R.id.tokenNumber_textView);
        carNo_editText=findViewById(R.id.carNo_editText);

        userName_textView.setText(username);
        name_editText.setText(name);
        email_textView.setText(email);
        parking_textView.setText(parking);
        phno_editText.setText(phno);
        tokenNumber_textView.setText(token);
        carNo_editText.setText(carno);

        edit_Button=findViewById(R.id.edit_Button);
        edit_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean valid = true;
                if (name_editText.getText().toString().isEmpty()){
                    name_editText.setError("Enter name");
                    valid = false;
                }
                if (carNo_editText.getText().toString().isEmpty()){
                    carNo_editText.setError("Enter car no");
                    valid = false;
                }
                if (phno_editText.getText().toString().isEmpty()){
                    phno_editText.setError("Enter phno");
                    valid = false;
                }

                if (valid){
                    databaseReference.child("Users").child(username).child("name").setValue(name_editText.getText().toString());
                    databaseReference.child("Users").child(username).child("car_no").setValue(carNo_editText.getText().toString());
                    databaseReference.child("Users").child(username).child("ph_no").setValue(phno_editText.getText().toString());
                    finish();
                }
            }
        });

        remove_Button=findViewById(R.id.remove_Button);
        remove_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signInWithEmailAndPassword(email, pin).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            user = mAuth.getCurrentUser();
                            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        databaseReference.child("Users").child(username).removeValue();
                                        databaseReference.child("Wallet").child(username).removeValue();
                                        databaseReference.child("Tokens").child(token).removeValue();
                                        databaseReference.child("Subscription").child(username).removeValue();
                                        databaseReference.child("Parking_users").child(parking).child(username).removeValue();

                                        String pushID = databaseReference.push().getKey().toString();
                                        databaseReference.child("Logs").child(pushID).child("subadmin").setValue(AdminDetails_class.getInstance().getName());
                                        databaseReference.child("Logs").child(pushID).child("description").setValue("deleted user "+ username+" with token number "+token);
                                        mAuth.signOut();
                                        finish();
                                    }else {
                                        Toast.makeText(Activity_Edit_User.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(Activity_Edit_User.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });


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
}