package ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.houserenting.Houseinfo;
import com.example.houserenting.ListofHouses;
import com.example.houserenting.R;
import com.example.houserenting.postLoginLL;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.List;

import com.example.houserenting.Images;

public class HouseRecyclerAdapter2 extends RecyclerView.Adapter<HouseRecyclerAdapter2.ViewHolder> {

    private Context context;
    private List<Images> ImageList2;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage firebaseStorage;
    private FirebaseUser user;
    private String  imageUrl;
    private int pos1;
    private String currentuserid;
    int count = 0;


    private CollectionReference  collectionReference = db.collection("House_Images");
    private CollectionReference  collectionReference2 = db.collection("Landlords");
    private CollectionReference collectionReference3 = db.collection("MyFavourites");
//    private RecyclerViewClickListner Listner;

    public HouseRecyclerAdapter2(Context context, List<Images> imageList) {
        this.context = context;
        this.ImageList2 = imageList;
    }


    @NonNull
    @Override
    public HouseRecyclerAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.forstudents,parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        pos1 = position;
        Images images = ImageList2.get(position);

//        String Contact_Number;
        holder.phone_number.setText(images.getMobile_no());
        holder.about.setText(images.getAbout());
        holder.construction.setText(images.getDescription());
        holder.bhk.setText(images.getBhk());
        holder.address.setText(images.getAddress());
        holder.distance.setText(images.getDistance());
        holder.rent.setText(images.getRent());


//        holder.name.setText(images.getName());
        imageUrl = images.getImageUrl();

        String timeAgo = (String) DateUtils.getRelativeTimeSpanString(images.getTimeAdded().getSeconds() * 1000);
        holder.dateAdded.setText(timeAgo);
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.img)
                .fit()
                .into(holder.image);
    }




    @Override
    public int getItemCount() {
        return ImageList2.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView phone_number,
                about,
                construction,
                bhk,
                address,
                distance,
                rent,
                dateAdded;
        public ImageView image;
        public Button contact, fav;
        public static final int REQUEST_CALL = 1;




        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);

            firebaseAuth = FirebaseAuth.getInstance();
            user = firebaseAuth.getCurrentUser();
            context = ctx;
            user = firebaseAuth.getCurrentUser();
            contact = itemView.findViewById(R.id.button_listofhouses);
            about = itemView.findViewById(R.id.AboutTv_Recycler_listofhouses);
            construction = itemView.findViewById(R.id.ConstructionTv_recycler_listofhouses);
            bhk = itemView.findViewById(R.id.BHKTv_recycler_listofhouses);
            address = itemView.findViewById(R.id.AddressTv_recycler_listofhouses);
            distance = itemView.findViewById(R.id.DistanceTv_recycler_listofhouses);
            rent = itemView.findViewById(R.id.rentTv_recycler_listofhouses);
            dateAdded = itemView.findViewById(R.id.TimeAgo_recycler_listofhouses);
            image = itemView.findViewById(R.id.housePhoto_listofhouses);
            fav = itemView.findViewById(R.id.button2_listofhouses);
//            delete = itemView.findViewById(R.id.imageView2);
//            delete.setVisibility(View.INVISIBLE);
            phone_number = itemView.findViewById(R.id.Landlord_no_listofhouses);
            currentuserid = user.getUid();

            phone_number.setVisibility(View.INVISIBLE);

            contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String number = phone_number.getText().toString();
                    if(number != null){
                        if(ContextCompat.checkSelfPermission(context
                                , Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions((Activity) context
                                    ,new String[] {Manifest.permission.CALL_PHONE},REQUEST_CALL);
                        }else {
                            String dial = "tel:" + number;
                           Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(dial));
                           context.startActivity(intent);
                        }

                    }else{
                        Toast.makeText(ctx, "Enter Phone number", Toast.LENGTH_SHORT).show();
                    }


                }
            });

            fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    currentuserid = user.getUid();
                    Images images1 = ImageList2.get(pos1);

                    collectionReference3.add(images1)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d("mahi", "onSuccess: Add hora hai");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("mahi", "onFailure: "  +e.toString());
                                }
                            });


                }
            });
        }

    }

}


