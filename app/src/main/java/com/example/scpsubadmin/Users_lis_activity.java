package com.example.scpsubadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Users_lis_activity extends AppCompatActivity {

    DatabaseReference databaseReference;
    RecyclerView parkingList_recyclerView;
    Adapter_usersListRecyclerView adapter_usersListRecyclerView;

    List<String> users_token_List;
    List<String> users_name_List;
    List<String> users_car_no;
    List<String> users_phno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_lis);
        setTitle("Users List");

        databaseReference = FirebaseDatabase.getInstance().getReference();

        users_token_List = new ArrayList<>();
        users_name_List = new ArrayList<>();
        users_car_no = new ArrayList<>();
        users_phno = new ArrayList<>();

        parkingList_recyclerView = findViewById(R.id.parkingList_recyclerView);
        parkingList_recyclerView.setLayoutManager(new GridLayoutManager(Users_lis_activity.this, 1));

        databaseReference.child("Parking_users").child(AdminDetails_class.getInstance().parking).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    users_token_List.add(snapshot1.getValue().toString());
                }
                databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot1:snapshot.getChildren()){
                            for (int i=0;i<users_token_List.size();i++){
                                if (snapshot1.getKey().equals(users_token_List.get(i))){
                                    users_name_List.add(snapshot1.child("name").getValue().toString());
                                    users_car_no.add(snapshot1.child("car_no").getValue().toString());
                                    users_phno.add(snapshot1.child("ph_no").getValue().toString());
                                }
                            }
                        }

                        adapter_usersListRecyclerView = new Adapter_usersListRecyclerView(Users_lis_activity.this, users_name_List, users_car_no, users_phno);
                        parkingList_recyclerView.setAdapter(adapter_usersListRecyclerView);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}