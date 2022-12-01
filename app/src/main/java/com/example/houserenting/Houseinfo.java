package com.example.houserenting;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

import util.HouseApi2;

public class Houseinfo extends AppCompatActivity {

    private static final int GALLERY_CODE = 1;
    private ImageView Camera;
    public ImageView delete;
    private TextView Name;
    HouseApi2 houseApi2;
    public String ImageUrl007;
//    private TextView Time;
//    private TextView About;
//    private TextView Construction;
//    private TextView BHK;
//    private TextView Address;
//    private TextView Distance;
//    private TextView Rent;
    private ImageView Bg;
    private String currentUserId;
    private String phone12;
    private EditText about;
    private EditText construction;
    private EditText bhk;
    private EditText address;
    private EditText distance;
    private EditText rent;
    private EditText mobile_no;

    private Button Save;
    private ProgressBar pb;

    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;


    private String Name11;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storageReference;

    private CollectionReference collectionReference = db.collection("House_Images");
    private CollectionReference collectionReference2 = db.collection("My_posts");
    private CollectionReference collectionReference3 = db.collection("Landlords");
    Uri imageUri;
//    private ActivityResultLauncher<String> Arl;

    public Houseinfo() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_houseinfo);

        Log.d("Design", "onCreate: He run Hotay mag problem kay aahe bc!! ");


        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance();


        mobile_no = findViewById(R.id.contact_recycler_students);
        delete = findViewById(R.id.imageView2);
        about = findViewById(R.id.AboutTv_Recycler_student);
        construction = findViewById(R.id.ConstructionTv_recycler_student);
        bhk = findViewById(R.id.BHKTv_recycler_student);
        address = findViewById(R.id.AddressTv_recycler_student);
        distance = findViewById(R.id.DistanceTv_recycler_students);
        rent = findViewById(R.id.rentTv_recycler_students);
        Save = findViewById(R.id.SaveButton);
        Bg = findViewById(R.id.housePhoto_all);
        pb = findViewById(R.id.progressBar2);

        pb.setVisibility(View.INVISIBLE);

            authStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    user = firebaseAuth.getCurrentUser();
                    if(user != null){

                    }else{

                    }
                }
            };

//            currentUsername = HouseApi2.getInstance().getName();

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Tag1", "onClick: Save Button Working");

                    Log.d("Tag2", "onClick: Enterd if of MainActivity of button");
                    imagetofb();


            }
        });

        Camera = findViewById(R.id.housePhoto_all);


      Camera.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Arl.launch("image/*");
          }
      });


    }

    @Override
    protected void onStart() {
        super.onStart();
        user = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }



    private void imagetofb(){

        Log.d("Tag3", "imagetofb: Inside the function");
        final String phno1 = mobile_no.getText().toString();
        final String about1 = about.getText().toString();
        final String construction1 = construction.getText().toString();
        final String bhk1 = bhk.getText().toString();
        final String address1 = address.getText().toString();
        final String distance1 = distance.getText().toString();
        final String rent1 = rent.getText().toString();
//        final Uri imgUri = imageUri;
        pb.setVisibility(View.VISIBLE);

        Log.d("Tag4", "imagetofb: Entering If of the function");

        if(!TextUtils.isEmpty(about1) && !TextUtils.isEmpty(construction1) && !TextUtils.isEmpty(bhk1) &&
                !TextUtils.isEmpty(address1) && !TextUtils.isEmpty(distance1) && !TextUtils.isEmpty(rent1)
                && !TextUtils.isEmpty(phno1) &&  imageUri != null) {
            Log.d("Tag5", "imagetofb: If Condition Checked,  moving to Storage");

            final StorageReference Filepath = storageReference.getReference()
                    .child("Flat_Images")
                    .child("House" + Timestamp.now().getSeconds());

            Log.d("Tag6", "imagetofb: Storage Reference on the way ( After Child)");

            Filepath.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.d("Tag7", "onSuccess: Filepath on Sucess");
                            pb.setVisibility(View.INVISIBLE);
                            Filepath.getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {



                                            currentUserId = user.getUid();
                                            houseApi2 = HouseApi2.getInstance();

                                            user = firebaseAuth.getCurrentUser();
                                            String CurrentUserid = user.getUid();
                                            String imageUrl = uri.toString();
                                            ImageUrl007 = storageReference.getReferenceFromUrl(imageUrl).toString();
                                            Log.d("America", "onSuccess: " + imageUrl);
                                            // Create a Image object
                                            Images images = new Images();
                                            images.setName(houseApi2.getName());
                                            images.setAbout(about1);
                                            images.setDescription(construction1);
                                            images.setBhk(bhk1);
                                            images.setAddress(address1);
                                            images.setDistance(distance1);
                                            images.setRent(rent1);
                                            images.setImageName(ImageUrl007);
                                            images.setImageUrl(imageUrl);
                                            images.setMobile_no(phno1);
                                            images.setUserId(CurrentUserid);
                                            images.setTimeAdded(new Timestamp(new Date()));
                                            Log.d("Tag8", "onSuccess: Ti model madhle images cha scene sort zhala");

                                            collectionReference.add(images)
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            Log.d("Tag9", "onSuccess: Intent cya acg");
//                                                            pb.setVisibility(View.INVISIBLE);

                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.d("fgiri1", "onFailure: " + e.toString());
                                                        }
                                                    });
                                            collectionReference2.add(images)
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            startActivity(new Intent(Houseinfo.this, postLoginLL.class));
                                                            Log.d("Tag10", "onSuccess: Added in My_posts");
                                                        }
                                                    });

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });





                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Fgiri3", "onFailure: " + e.toString());
                            pb.setVisibility(View.INVISIBLE);
                        }
                    });
        }else{
            pb.setVisibility(View.INVISIBLE);
        }

    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode == GALLERY_CODE && resultCode == RESULT_OK){
//            if(data != null){
//                imageUri = data.getData();
//                Bg.setImageURI(imageUri);
//            }
//        }
//    }
        ActivityResultLauncher<String> Arl = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
            Camera.setImageURI(result);
            imageUri = result;
        }
    });
}