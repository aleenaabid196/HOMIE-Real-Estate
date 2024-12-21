package com.example.graphproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import ng.max.slideview.SlideView;

public class ViewAdActivity1 extends FragmentActivity implements OnMapReadyCallback {

    Advertisement ad;
    ImageSlider imageSlider;


    ImageView fvrtImgView;
    boolean favClicked = false;

    TextView adTitleTxt, bedroomTxt, bathroomTxt, floorTxt, areaUnitTxt, usernameTxt, priceTxt, descriptionTxt;
    ImageView profilePic;
//    ViewPager imagesView;

    GoogleMap map;

    SlideView callBtn;

    String username;

    String uri;

    ArrayList<Uri> adImagesList = new ArrayList<>();

    ArrayList<SlideModel> imagesss = new ArrayList<>();

    Button tour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ad1);


        imageSlider = findViewById(R.id.imageSLider);
        //get username
        username = getIntent().getStringExtra("KEY");

        //HIDING THE NAVIGATION BAR
        hideNavigationBar();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.TYPE_STATUS_BAR);

        ad = (Advertisement) getIntent().getSerializableExtra("Ad");
        //Toast.makeText(this, ad.getAdTitle(), Toast.LENGTH_SHORT).show();

        //binding the views
        fvrtImgView = findViewById(R.id.fvrtImgView);
        callBtn = findViewById(R.id.slideBtn);

        tour = findViewById(R.id.tourBtn);

        adTitleTxt = findViewById(R.id.adTitleText);
        bedroomTxt = findViewById(R.id.bedroomText);
        bathroomTxt = findViewById(R.id.bathroomText);
        floorTxt = findViewById(R.id.floorText);
        areaUnitTxt = findViewById(R.id.areaUnitText);
        usernameTxt = findViewById(R.id.nameText);
        priceTxt = findViewById(R.id.priceText);
        descriptionTxt = findViewById(R.id.descriptionText);

        profilePic = findViewById(R.id.pfpView);
//        imagesView = findViewById(R.id.adImgViewPager);

        adTitleTxt.setText(ad.getAdTitle());
        bedroomTxt.setText(ad.getBedrooms());
        bathroomTxt.setText(ad.getBathrooms());
        floorTxt.setText(ad.getFloors());
        areaUnitTxt.setText(ad.getAdAreaUnit().toString());
        priceTxt.setText("PKR "+ad.getAdPrice().toString());
        descriptionTxt.setText(ad.getAdDescription());

        tour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ViewAdActivity1.this,PanoramaActivity.class);
                startActivity(i);
            }
        });


        //get list of ad images
        for(int i = 0 ; i < ad.getImagesListSize();i++)
        {
            imagesss.add(new SlideModel(ad.getImages(i), ScaleTypes.CENTER_CROP));
//            adImagesList.add(Uri.parse(ad.getImages(i)));
//            Toast.makeText(this, adImagesList.get(i).toString(), Toast.LENGTH_SHORT).show();
        }
        imageSlider.setImageList(imagesss,ScaleTypes.CENTER_CROP);

//        UploadViewPagerAdapter adapter1 = new UploadViewPagerAdapter(this,adImagesList);
//        imageSlider.setAdapter(adapter1);

//        get username and profile
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users");
        db.child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usernameTxt.setText(snapshot.child("Name").getValue(String.class));
               //
                //Toast.makeText(ViewAdActivity1.this, snapshot.child("Profile Pic").getValue(String.class), Toast.LENGTH_SHORT).show();
              //  Picasso.with(ViewAdActivity1.this).load(snapshot.child("Profile Pic").getValue(String.class)).fit().centerCrop().into(profilePic);
                //uri = snapshot.child("Profile Pic").getValue(String.class);
              //  profilePic.setImageURI(Uri.parse(snapshot.child("Profile Pic").getValue(String.class)));
               //profilePic.setImageURI(Uri.parse(snapshot.child("Profile Pic").getValue(String.class)));
               // profilePic.setScaleType(ImageView.ScaleType.CENTER_CROP);
               // Picasso.with(ViewAdActivity1.this).load(snapshot.child("Profile Pic").getValue(String.class)).centerCrop().into(profilePic);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //profilePic.setImageURI(Uri.parse(uri));




        fvrtImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(favClicked == true)
                {
                    fvrtImgView.setImageResource(R.drawable.favorite_outline);
                    favClicked = false;
                }
                else
                {
                    fvrtImgView.setImageResource(R.drawable.favorite_filled);
                    favClicked = true;
                }

            }
        });

        callBtn.setOnSlideCompleteListener(new SlideView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideView slideView) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+92"+ad.getAdPhoneNumber().toString()));
                startActivity(intent);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
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

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        LatLng AmirTown = new LatLng(Double.parseDouble(ad.getLatitude()), Double.parseDouble(ad.getLongitude()));
        map.addMarker(new MarkerOptions().position(AmirTown).title("AmirTown"));
        //map.addCircle(new CircleOptions().center(AmirTown).radius(10).);
        //map.moveCamera(CameraUpdateFactory.newLatLng(AmirTown));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(AmirTown,15));
    }
}