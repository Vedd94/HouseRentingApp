package com.example.houserenting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import util.HouseApi2;

public class Landlord_login extends AppCompatActivity {

    private Button LLSignin;
    private Button LLLogin;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private AutoCompleteTextView Email;
    private EditText Password;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Landlords");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landlord_login);

        firebaseAuth = FirebaseAuth.getInstance();
        LLSignin = findViewById(R.id.Student_Signin);
        LLLogin = findViewById(R.id.Login_Student);
        Email = findViewById(R.id.student_email);
        Password = findViewById(R.id.Student_password);

        LLSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Landlord_login.this, Landlord_Signin.class);
                startActivity(intent);
            }
        });

        LLLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginnFun(Email.getText().toString(), Password.getText().toString());

            }
        });
    }

    private void LoginnFun(String email, String password){
        Log.d("tag1", "LoginnFun: Enteren function");
            if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
                Log.d("tag2", "LoginnFun: Entered if Statement");
                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                String currentUserId = user.getUid();
                                Log.d("tag3", "onComplete: Before Collection reference");
                                collectionReference
                                        .whereEqualTo("userId",currentUserId)
                                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot value,
                                                                @Nullable FirebaseFirestoreException error) {
                                                Log.d("tag4", "onEvent: Snapshot madhe");
                                                if(error != null){
                                                    Log.d("TAG12", "onEvent: " + error.toString());
                                                }
                                                assert value != null;
                                                if(!value.isEmpty()) {
                                                    Log.d("tag5", "onEvent: Inside if of Event");
                                                    for (QueryDocumentSnapshot snapshot : value) {
                                                        Log.d("tag6", "onEvent: Inside for loop");
                                                        HouseApi2 houseApi2 = HouseApi2.getInstance();
                                                        houseApi2.setName(snapshot.getString("Name"));
                                                        houseApi2.setUserid(snapshot.getString("userId"));
                                                        Log.d("tag7", "onEvent: Before Intent");
                                                        Intent intent = new Intent(Landlord_login.this, postLoginLL.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            }
                                        });
             }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

            }else{
                Toast.makeText(this, "Please fill details", Toast.LENGTH_SHORT).show();
            }
    }
}