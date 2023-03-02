package com.example.scpsubadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Home_activity extends AppCompatActivity {

    DatabaseReference checkReference;
    Button registerUser_Button;
    Button logout_Button;
    Button users_Button;
    Button usersList_Button;
    DatabaseReference registrationRef;
    DatabaseReference databaseReference;
    DatabaseReference tokenCheckReference;
    RecyclerView parkingSlots_recyclerView;
    Adapter_parkingSlotsRecyclerView adapter_parkingSlotsRecyclerView;

    List<String> parkingSlotsName_List;
    List<String> parkingSlots_List;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Home");

        parkingSlotsName_List = new ArrayList<>();
        parkingSlots_List = new ArrayList<>();

        tokenCheckReference = FirebaseDatabase.getInstance().getReference("Tokens");
        databaseReference = FirebaseDatabase.getInstance().getReference("Slots/"+AdminDetails_class.getInstance().parking);

        Toast.makeText(this, AdminDetails_class.getInstance().getParking(), Toast.LENGTH_SHORT).show();
        parkingSlots_recyclerView = findViewById(R.id.parkingSlots_recyclerView);
        parkingSlots_recyclerView.setLayoutManager(new GridLayoutManager(Home_activity.this, 2));
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    parkingSlotsName_List.add(snapshot1.getKey());
                    parkingSlots_List.add(snapshot1.getValue().toString());
                }
                adapter_parkingSlotsRecyclerView = new Adapter_parkingSlotsRecyclerView(Home_activity.this,parkingSlotsName_List,parkingSlots_List);
                parkingSlots_recyclerView.setAdapter(adapter_parkingSlotsRecyclerView);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        registrationRef = FirebaseDatabase.getInstance().getReference("Registration/"+AdminDetails_class.getInstance().parking);
        registrationRef = FirebaseDatabase.getInstance().getReference("Registration/"+AdminDetails_class.getInstance().parking);
        registrationRef.setValue(0);

        usersList_Button = findViewById(R.id.usersList_Button);
        usersList_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home_activity.this,Users_lis_activity.class);
                startActivity(intent);
            }
        });

        registerUser_Button = findViewById(R.id.registerUser_Button);
        logout_Button = findViewById(R.id.logout_Button);
        users_Button = (Button) findViewById(R.id.users_Button);
        users_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home_activity.this, ActivityUsersList.class);
                startActivity(intent);

            }
        });
        logout_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });
        registerUser_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrationRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue().toString().equals("0")){
                            registrationRef.setValue("waiting");
                            Toast.makeText(Home_activity.this, "Please tap new token", Toast.LENGTH_SHORT).show();
                        }else if (snapshot.getValue().toString().equals("waiting")){
                            registrationRef.setValue(0);
                            Toast.makeText(Home_activity.this, "Registration Canceled", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            tokenCheckReference.child(snapshot.getValue().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                    if (snapshot1.exists()){
                                        Toast.makeText(Home_activity.this, "Token already exists!", Toast.LENGTH_SHORT).show();
                                        registrationRef.setValue(0);
                                    }else {
                                        Intent intent = new Intent(Home_activity.this,Register_user_activity.class);
                                        intent.putExtra("TOKEN",snapshot.getValue().toString());
                                        registrationRef.setValue(0);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }
}