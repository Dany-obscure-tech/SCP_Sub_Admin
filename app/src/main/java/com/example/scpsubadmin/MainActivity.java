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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText email_editText;
    EditText pin_editText;
    Button login_Button;
    DatabaseReference subAdminRef;

    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Sub Admin Panel");

        subAdminRef = FirebaseDatabase.getInstance().getReference("Admin");

        email_editText = findViewById(R.id.email_editText);
        pin_editText = findViewById(R.id.pin_editText);

        login_Button = findViewById(R.id.login_Button);

        login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean valid = true;
                if (email_editText.getText().toString().isEmpty()){
                    email_editText.setError("Enter email");
                    valid = false;
                }else if (!isValidEmail(email_editText.getText().toString())){
                    email_editText.setError("Invaid Email");
                    valid = false;
                }

                if (pin_editText.getText().toString().isEmpty()){
                    pin_editText.setError("Enter pin");
                    valid = false;
                }


                if (valid){
                    subAdminRef.child(email_editText.getText().toString().replace(".",",")).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {


                            if (snapshot.exists()){
                                System.out.println("Valid email");
                                if (pin_editText.getText().toString().equals(snapshot.child("pin").getValue().toString())){
                                    Intent intent = new Intent(MainActivity.this,Home_activity.class);
                                    AdminDetails_class.getInstance().setName(snapshot.child("name").getValue().toString());
                                    AdminDetails_class.getInstance().setParking(snapshot.child("parking").getValue().toString());
                                    startActivity(intent);
                                }else {
                                    Toast.makeText(MainActivity.this, "Wrong pin", Toast.LENGTH_SHORT).show();
                                }

                            }else {
                                Toast.makeText(MainActivity.this, "Invalid email!", Toast.LENGTH_SHORT).show();
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

    public boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        email_editText.setText("");
        pin_editText.setText("");
    }
}