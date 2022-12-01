package com.example.houserenting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import ui.HouseRecyclerAdapter;
import ui.HouseRecyclerAdapter4;
import util.HouseApi;
import util.HouseApi2;

public class MyFavourites extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage firebaseStorage;
    private FirebaseUser user;
    private String currentUserid;
    private List<Images> imagelist3;
    private RecyclerView recyclerView;
    private HouseRecyclerAdapter4 houseRecyclerAdapter4;

    private CollectionReference collectionReference = db.collection("House_Images");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favourites);
        imagelist3 = new ArrayList<>();
//        delete = findViewById(R.id.imageView2);
//        delete.setVisibility(View.VISIBLE);
        recyclerView = findViewById(R.id.recyclerView_myfav);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        houseRecyclerAdapter4 = new HouseRecyclerAdapter4(
                MyFavourites.this, imagelist3
        );


    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d("ourfav", "onStart: My Fav is starting");
        currentUserid = user.getUid();
//        imagelist.remove()
        currentUserid = user.getUid();
        collectionReference
                .whereEqualTo("userId", currentUserid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        Log.d("mahi", "onSuccess: Success Executed");
                        if(!queryDocumentSnapshots.isEmpty()){
                            for(QueryDocumentSnapshot images : queryDocumentSnapshots){
                                Images image = images.toObject(Images.class);
                                imagelist3.add(image);
                            }
                            // Invoke Recycler View
                            houseRecyclerAdapter4 = new HouseRecyclerAdapter4(
                                    MyFavourites.this, imagelist3
                            );

                            recyclerView.setAdapter(houseRecyclerAdapter4);
                            houseRecyclerAdapter4.notifyDataSetChanged();
                        }else{
                            Log.d("mahi", "onSuccess: No if executed");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("mahi", "onFailure: " + e.toString());
                    }
                });
    }

}