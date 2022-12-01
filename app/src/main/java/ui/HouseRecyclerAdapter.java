package ui;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.houserenting.Images;
import com.example.houserenting.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HouseRecyclerAdapter extends RecyclerView.Adapter<HouseRecyclerAdapter.ViewHolder> {




    String sref;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage firebaseStorage;
    private FirebaseUser user;
    private CollectionReference collectionReference = db.collection("My_posts");
    private CollectionReference collectionReference2 = db.collection("House_Images");
    private Context context;
    private List<Images> ImageList;
    private ImageView delete;
//    private OnItemClickListner listner;


    


    public HouseRecyclerAdapter(Context context, List<Images> imageList) {
        this.context = context;
        this.ImageList = imageList;
    }


    @NonNull
    @Override
    public HouseRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.house_row,parent,false);


        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull HouseRecyclerAdapter.ViewHolder holder, int position) {

        Images images = ImageList.get(position);
        String imageUrl3;

//        holder.
        holder.about.setText(images.getAbout());
        holder.construction.setText(images.getDescription());
        holder.bhk.setText(images.getBhk());
        holder.address.setText(images.getAddress());
        holder.distance.setText(images.getDistance());
        holder.rent.setText(images.getRent());
//        holder.name.setText(images.getName());
        imageUrl3 = images.getImageUrl();
        firebaseStorage = FirebaseStorage.getInstance();
        sref = firebaseStorage.getReferenceFromUrl(imageUrl3).toString();

        String timeAgo = (String) DateUtils.getRelativeTimeSpanString(images.getTimeAdded().getSeconds() * 1000);
        holder.dateAdded.setText(timeAgo);
        Picasso.get()
                .load(imageUrl3)
                .placeholder(R.drawable.img)
                .fit()
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return ImageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name,
                about,
                construction,
                bhk,
                address,
                distance,
                rent,
                dateAdded;
        public ImageView image, delete;

        String userId;


        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);


            context = ctx;
            firebaseAuth = FirebaseAuth.getInstance();
            user = firebaseAuth.getCurrentUser();



            about = itemView.findViewById(R.id.AboutTv_Recycler_MyPosts);
            construction = itemView.findViewById(R.id.ConstructionTv_recycler_MyPosts);
            bhk = itemView.findViewById(R.id.BHKTv_recycler_MyPosts);
            address = itemView.findViewById(R.id.AddressTv_recycler_MyPosts);
            distance = itemView.findViewById(R.id.DistanceTv_recycler_MyPosts);
            rent = itemView.findViewById(R.id.rentTv_recycler_MyPosts);
            dateAdded = itemView.findViewById(R.id.TimeAgo_recycler_MyPosts);
            image = itemView.findViewById(R.id.housePhoto_all);
            delete = itemView.findViewById(R.id.imageView2);
//            name = itemView.findViewById(R.id.nameTv_recycler);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Images images = Images.getInstance();
                    Log.d("Dhoni", "onClick: Button chlu aahe");

                    collectionReference.whereEqualTo("imageName", sref)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    Log.d("Dhoni", "onComplete: Inside OncompleteListner " + sref);
                                    if(task.isSuccessful() && !task.getResult().isEmpty()){
                                        Log.d("Dhoni", "onComplete: Inside If");
                                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                        String documentID = documentSnapshot.getId();
                                        collectionReference.document(documentID)
                                                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(ctx, "Deleted from My_posts", Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.d("Rohit", "onFailure: Problem in Myposts in delete " + e.toString());
                                                    }
                                                });
                                    }else{
                                        Log.d("Dhoni", "onComplete: Task cha issue aahe kahitari");
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("Virat", "onFailure: Problem in finding  ids" + e.toString());
                                }
                            });
                    collectionReference2.whereEqualTo("imageName", sref)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    Log.d("Dhoni2", "onComplete: CR2" + images.getImageUrl());
                                    if(task.isSuccessful() && !task.getResult().isEmpty()){

                                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                        String documentID = documentSnapshot.getId();
                                        collectionReference2.document(documentID)
                                                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(ctx, "Deleted from images", Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.d("Rohit", "onFailure: Problem in Images in delete " + e.toString());
                                                    }
                                                });
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("Virat", "onFailure: Problem in finding id " + e.toString());
                                }
                            });

                }
            });

        }
    }
}
