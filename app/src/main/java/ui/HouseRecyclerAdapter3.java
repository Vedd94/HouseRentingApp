package ui;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.houserenting.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.List;

import com.example.houserenting.Images;

public class HouseRecyclerAdapter3 extends RecyclerView.Adapter<HouseRecyclerAdapter3.ViewHolder> {

    private Context context;
    private List<Images> ImageList;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage firebaseStorage;
    private FirebaseUser user;
    private String currentuserid;


    private CollectionReference  collectionReference = db.collection("House_Images");
    private CollectionReference  collectionReference2 = db.collection("Landlords");
//    private RecyclerViewClickListner Listner;

    public HouseRecyclerAdapter3(Context context, List<Images> imageList) {
        this.context = context;
        this.ImageList = imageList;
    }


    @NonNull
    @Override
    public HouseRecyclerAdapter3.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.forviewonly,parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Images images = ImageList.get(position);
        String imageUrl;
//        String Contact_Number;
        holder.about.setText(images.getAbout());
        holder.construction.setText(images.getDescription());
        holder.bhk.setText(images.getBhk());
        holder.address.setText(images.getAddress());
        holder.distance.setText(images.getDistance());
        holder.rent.setText(images.getRent());
        holder.name.setText(images.getMobile_no());

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
        return ImageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name,
                about,
                construction,
                bhk,
                address,
                distance,
                rent,
                dateAdded;
        public ImageView image, delete;




        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);

            firebaseAuth = FirebaseAuth.getInstance();
            user = firebaseAuth.getCurrentUser();
            context = ctx;
            user = firebaseAuth.getCurrentUser();
            about = itemView.findViewById(R.id.AboutTv_Recycler_PostLoginll);
            construction = itemView.findViewById(R.id.ConstructionTv_recycler_PostLoginll);
            bhk = itemView.findViewById(R.id.BHKTv_recycler_PostLoginll);
            address = itemView.findViewById(R.id.AddressTv_recycler_PostLoginll);
            distance = itemView.findViewById(R.id.DistanceTv_recycler_PostLoginll);
            rent = itemView.findViewById(R.id.rentTv_recycler_PostLoginll);
            dateAdded = itemView.findViewById(R.id.TimeAgo_recycler_PostLoginll);
            image = itemView.findViewById(R.id.housePhoto_PostLoginll);
//            delete = itemView.findViewById(R.id.imageView2);
//            delete.setVisibility(View.INVISIBLE);
            name = itemView.findViewById(R.id.Landlord_no_PostLoginll);
            currentuserid = user.getUid();

        }

    }

}


