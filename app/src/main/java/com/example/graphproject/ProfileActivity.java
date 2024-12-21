package com.example.graphproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity implements AdAdapterInterface1 {

    ArrayList<Advertisement> userAds = new ArrayList<>();
    String username;
    Integer userAdCount;

    RecyclerView recyclerView;

    ImageView followImg;
    boolean isFollowed = false;

    ImageView profilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //get the username
        username = getIntent().getStringExtra("KEY");

        //getAdCount
        getAdCount(username);

        //get User Ads in a list
        getUserAds();

        //binding the views
        recyclerView = findViewById(R.id.recyclerViewProfileAd);
        followImg = findViewById(R.id.followBtn);

        //follow button functionality
        followImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFollowed == true)
                {
                    followImg.setImageResource(R.drawable.follow_icon);
                    isFollowed = false;
                }
                else
                {
                    followImg.setImageResource(R.drawable.followw_icon2);
                    isFollowed = true;
                }

            }
        });


        //HIDING THE NAVIGATION BAR
        hideNavigationBar();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.TYPE_STATUS_BAR);

        ProfileAdApadter profileAdApadter = new ProfileAdApadter(ProfileActivity.this, userAds , this);

        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.setLayoutManager(new LinearLayoutManager(ProfileActivity.this, LinearLayoutManager.HORIZONTAL, false));
                recyclerView.setAdapter(profileAdApadter);
            }
        }, 2000);

    }

    @Override
    protected void onResume() {
        super.onResume();
        hideNavigationBar();
    }

    //HIDING THE NAV BAR
    private void hideNavigationBar() {
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
    }

    private void getAdCount(String username) {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users");
        db.child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot dataSnapshot = task.getResult();
                Integer adCount = dataSnapshot.child("Ad Count").getValue(Integer.class);
                userAdCount = adCount;
            }
        });
    }

    private void getUserAds() {

        final String[] adTitle = new String[1];
        final String[] adDescription = new String[1];
        final String[] adPhoneNumber = new String[1];
        final Integer[] adPrice = new Integer[1];
        final Integer[] adAreaUnit = new Integer[1];
        final Integer[] imageCount = new Integer[1];
        final String[] adLongitude = new String[1];
        final String[] adLatitude = new String[1];
        final String[] adBedrooms = new String[1];
        final String[] adBathrooms = new String[1];
        final String[] adFloors = new String[1];

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Ads");
        db.child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot dataSnapshot = task.getResult();
                String adNumber;
                String imageUri;
                Integer imageNumber;

                for (int i = 1; i <= userAdCount; i++) {

                    ArrayList<String> images = new ArrayList<>();
                    adTitle[0] = dataSnapshot.child("Ad" + i).child("Ad Title").getValue(String.class);
                    adDescription[0] = dataSnapshot.child("Ad" + i).child("Description").getValue(String.class);
                    adPhoneNumber[0] = dataSnapshot.child("Ad" + i).child("Phone No").getValue(String.class);
                    adPrice[0] = dataSnapshot.child("Ad" + i).child("Price").getValue(Integer.class);
                    adAreaUnit[0] = dataSnapshot.child("Ad" + i).child("Area Unit").getValue(Integer.class);
                    imageCount[0] = dataSnapshot.child("Ad" + i).child("Image Count").getValue(Integer.class);
                    adLongitude[0] = dataSnapshot.child("Ad" + i).child("Longitude").getValue(String.class);
                    adLatitude[0] = dataSnapshot.child("Ad" + i).child("Latitude").getValue(String.class);
                    adBedrooms[0] = dataSnapshot.child("Ad" + i).child("Bedrooms").getValue(String.class);
                    adBathrooms[0] = dataSnapshot.child("Ad" + i).child("Bathrooms").getValue(String.class);
                    adFloors[0] = dataSnapshot.child("Ad" + i).child("Floors").getValue(String.class);

                    adNumber = String.valueOf(i);
                    imageNumber = imageCount[0];

                    for (int j = 1; j <= imageNumber; j++) {
                        imageUri = dataSnapshot.child("Ad" + adNumber).child("Images").child("Image" + j).getValue(String.class);
                       // Toast.makeText(ProfileActivity.this, imageNumber.toString(), Toast.LENGTH_LONG).show();
                        images.add(imageUri);
                    }

                    Advertisement ad = new Advertisement(images, adTitle[0], adDescription[0], adPrice[0], adAreaUnit[0], adPhoneNumber[0],adLongitude[0],adLatitude[0] , adBedrooms[0] , adBathrooms[0] , adFloors[0]);
                    userAds.add(ad);
                }
            }
        });
    }

    @Override
    public void onItemCLick(int position) {
        Intent i = new Intent(ProfileActivity.this , ViewAdActivity1.class);
        Advertisement ad = userAds.get(position);
        i.putExtra("Ad",ad);
        i.putExtra("KEY",username);
        startActivity(i);
    }


}