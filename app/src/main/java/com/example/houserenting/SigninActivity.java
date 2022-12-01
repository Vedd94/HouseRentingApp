package com.example.houserenting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import util.HouseApi;

public class SigninActivity extends AppCompatActivity {

    private EditText Name;
    private EditText RollNumber;
    private EditText Password;
    private EditText Email;
    private Button AccountCreate;
    private EditText PhoneNumber;
    private ProgressBar pb;
    private FirebaseAuth firebaseAut;
    private FirebaseUser CurrentUser;
    private FirebaseAuth.AuthStateListener authStateListener;

    // Connection to FireStore
    private FirebaseFirestore db =  FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    public SigninActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        firebaseAut = FirebaseAuth.getInstance();
        PhoneNumber = findViewById(R.id.Landlord_ContactNo);
        Email = findViewById(R.id.student_email);
        Name = findViewById(R.id.Landlord_name);
        RollNumber = findViewById(R.id.Landlord_age);
        Password = findViewById(R.id.Student_password);
        AccountCreate = findViewById(R.id.Login_Student);
        pb = findViewById(R.id.progressBar);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                CurrentUser = firebaseAuth.getCurrentUser();
                if(CurrentUser != null){
                    // User Logged in
                }else{
                    //no user yet
                }
            }
        };
            AccountCreate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!TextUtils.isEmpty(Email.getText().toString()) && !TextUtils.isEmpty(Password.getText().toString()) &&
                            !TextUtils.isEmpty(RollNumber.getText().toString()) && !TextUtils.isEmpty(Name.getText().toString())
                    &&!TextUtils.isEmpty(PhoneNumber.getText().toString())) {
                        String email = Email.getText().toString();
                        String password = Password.getText().toString();
                        String rollno = RollNumber.getText().toString();
                        String name = Name.getText().toString();
                        String contact = PhoneNumber.getText().toString();

                        createUserEmailAccount(email,password,rollno,name, contact);

                    }else{
                        Toast.makeText(SigninActivity.this, "Em[ty Fields not allowed", Toast.LENGTH_SHORT).show();
                    }

                }

            });

    }

    private void createUserEmailAccount(String email, String password, String rollno, String name, String contact){
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) &&
                !TextUtils.isEmpty(rollno) && !TextUtils.isEmpty(name)){

            pb.setVisibility(View.VISIBLE);
            Log.d("Log1", "createUserEmailAccount: Entered Function");
            firebaseAut.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("Log2", "onComplete: Authentication madhe gele");
                            if(task.isSuccessful()){
                                //we take user to ListofHouses
                                CurrentUser = firebaseAut.getCurrentUser();
                                assert CurrentUser != null;
                                String CurrentUserId = CurrentUser.getUid();

                                //Create user map so we create user in user collection
                                Map<String, String> userObj = new HashMap<>();
                                userObj.put("Name", name);
                                userObj.put("Roll_Number", rollno);//else put CurrentUserId
                                userObj.put("Contact Details", contact);
                                userObj.put("userId",CurrentUserId);
                                // Save to our Firestore database
                                collectionReference.add(userObj)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Log.d("Log3", "onSuccess: Added to collections");
                                                    documentReference.get()
                                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                    if(task.getResult().exists()){
                                                                        pb.setVisibility(View.INVISIBLE);
                                                                        String name = task.getResult()
                                                                                .getString("Name");

                                                                        HouseApi houseApi = HouseApi.getInstance();
                                                                        houseApi.setName(name);
                                                                        houseApi.setRoll_no(rollno);
                                                                        houseApi.setContact_no(contact);
                                                                        houseApi.setStudentid(CurrentUserId);

                                                                        Intent intent = new Intent(SigninActivity.this,ListofHouses.class);
                                                                        intent.putExtra("Name", name);
                                                                        startActivity(intent);
                                                                    }else{

                                                                    }

                                                                }
                                                            });
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("Problem", "onFailure: " + e);
                                            }
                                        });
                            }else{
                                //something went wrong
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

        }else{

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        CurrentUser = firebaseAut.getCurrentUser();
        firebaseAut.addAuthStateListener(authStateListener);
    }
}