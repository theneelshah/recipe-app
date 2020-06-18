package com.example.administrator.recipeapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    List<FoodData> myFoodList;
    FoodData mFoodData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this,1);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Items....");

        myFoodList = new ArrayList<>();

        final MyAdapter myAdapter = new MyAdapter(MainActivity.this, this.myFoodList);
        mRecyclerView.setAdapter(myAdapter);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Recipe");

//        progressDialog.show();
        ValueEventListener eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myFoodList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String itemName = (String) snapshot.child("itemName").getValue();
                    String itemPrice = (String) snapshot.child("itemPrice").getValue();
                    String itemDescription = (String) snapshot.child("itemDescription").getValue();
                    String itemImage = (String) snapshot.child("itemImage").getValue();

                    FoodData foodData = new FoodData(itemName, itemDescription, itemPrice, itemImage);
                    myFoodList.add(foodData);
//                    String name = foodData.getItemName();
                }

                Toast.makeText(MainActivity.this, "Fetched from the database", Toast.LENGTH_LONG).show();
                myAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }

    public void btn_uploadActivity(View view) {
        startActivity(new Intent(this, UploadRecipe.class));
    }
}
