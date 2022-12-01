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
import util.HouseApi2;

public class Timepass extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage firebaseStorage;
    private FirebaseUser user;
    private List<Images> imagelist;
    private RecyclerView recyclerView;
    private HouseRecyclerAdapter houseRecyclerAdapter;


//    public ImageView delete;

    private CollectionReference collectionReference = db.collection("My_posts");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timepass);

        Log.d("Shami", "onCreate: In Timepass");
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        imagelist = new ArrayList<>();
//        delete = findViewById(R.id.imageView2);
//        delete.setVisibility(View.VISIBLE);
        recyclerView = findViewById(R.id.recyclerView2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        houseRecyclerAdapter = new HouseRecyclerAdapter(
                Timepass.this, imagelist
        );


    }

    @Override
    protected void onStart() {
        super.onStart();

//        imagelist.remove()
        collectionReference
                .whereEqualTo("userId", HouseApi2.getInstance().getUserid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                        if(!queryDocumentSnapshots.isEmpty()){
                            for(QueryDocumentSnapshot images : queryDocumentSnapshots){
                                Images image = images.toObject(Images.class);
                                imagelist.add(image);
                            }
                            // Invoke Recycler View
                            houseRecyclerAdapter = new HouseRecyclerAdapter(
                                    Timepass.this, imagelist
                            );

                            recyclerView.setAdapter(houseRecyclerAdapter);
                            houseRecyclerAdapter.notifyDataSetChanged();
                        }else{
//                            noImageEntry.setVisibility(View.VISIBLE);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

}