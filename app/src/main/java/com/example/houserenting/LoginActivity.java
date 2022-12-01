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

import util.HouseApi;
import util.HouseApi2;

public class LoginActivity extends AppCompatActivity {

    private AutoCompleteTextView Email;
    private EditText Password;
    private Button Signin;
    private Button Login;
    private Button SinLand;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        Email = findViewById(R.id.student_email);
        Password = findViewById(R.id.Student_password);
        Signin = findViewById(R.id.Student_Signin);
        SinLand = findViewById(R.id.Landlord);
        Login = findViewById(R.id.Login_Student);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFunc(Email.getText().toString(), Password.getText().toString());
            }
        });


        SinLand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, Landlord_login.class);
                startActivity(intent);
            }
        });
        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SigninActivity.class);
                startActivity(intent);
            }
        });
    }

    private void LoginFunc(String email, String password){
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
                                                    HouseApi houseApi = HouseApi.getInstance();
                                                    houseApi.setName(snapshot.getString("Name"));
                                                    houseApi.setName(snapshot.getString("Roll_Number"));
                                                    houseApi.setContact_no(snapshot.getString("Contact Details"));
                                                    houseApi.setStudentid(snapshot.getString("Student_id"));
                                                    Log.d("tag7", "onEvent: Before Intent");
                                                    Intent intent = new Intent(LoginActivity.this, ListofHouses.class);
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