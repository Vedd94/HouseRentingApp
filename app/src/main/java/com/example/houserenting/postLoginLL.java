package com.example.houserenting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import ui.HouseRecyclerAdapter3;

public class postLoginLL extends AppCompatActivity {

    private Button But;
    private String Name;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage firebaseStorage;
    private FirebaseUser user;
    private List<Images> imagelist;
    private RecyclerView recyclerView;
    private HouseRecyclerAdapter3 houseRecyclerAdapter3;

    private CollectionReference collectionReference = db.collection("House_Images");
    private TextView noImageEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_login_ll);

        Log.d("pposst", "onCreate: He pn chalu hotay");
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        noImageEntry =findViewById(R.id.list_no_houses);
        imagelist = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.New_Flat:
                if(user != null && firebaseAuth!=null){

                    startActivity(new Intent(postLoginLL.this, Houseinfo.class));
                    finish();
                }

                break;
            case R.id.Sign_out:
                //sign user out!
                if(user != null && firebaseAuth!=null){
                    firebaseAuth.signOut();

                   startActivity(new Intent(postLoginLL.this, MainActivity.class));
                }
                break;

            case R.id.myPosts:
                if(user != null && firebaseAuth != null){

                    startActivity((new Intent(postLoginLL.this,Timepass.class)));
                }

        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onStart() {
        super.onStart();

        collectionReference
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
                                houseRecyclerAdapter3 = new HouseRecyclerAdapter3(
                                        postLoginLL.this, imagelist
                                );

                                recyclerView.setAdapter(houseRecyclerAdapter3);
                                houseRecyclerAdapter3.notifyDataSetChanged();
                            }else{
                                noImageEntry.setVisibility(View.VISIBLE);
                            }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}