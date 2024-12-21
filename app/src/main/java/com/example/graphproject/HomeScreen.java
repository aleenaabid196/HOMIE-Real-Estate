package com.example.graphproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;

import java.io.IOException;
import java.text.StringCharacterIterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.StringTokenizer;

public class HomeScreen extends AppCompatActivity {

    CircleMenu circleMenu;
    String username;
    TextView hiText;
    VideoView videoView;

    //Location
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView locationText;
    String longitude;
    String latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        //binding the views
        locationText = findViewById(R.id.locationText);
        hiText = findViewById(R.id.hiText);
//        videoView = findViewById(R.id.homeVideoView);
//
//        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.home_video);
//        videoView.setVideoURI(uri);
//        videoView.start();
//        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mediaPlayer) {
//                mediaPlayer.setLooping(true);
//            }
//        });
        username = getIntent().getStringExtra("KEY");

        //HIDING THE NAVIGATION BAR
        hideNavigationBar();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.TYPE_STATUS_BAR);

        //Initialize fusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        //get location data
        getLocationData();

        hiText.setText("Hi "+username+"!");

        //circle menu functionality
        circleMenu = findViewById(R.id.circleMenu);

        circleMenu.setMainMenu(Color.parseColor("#819782"), R.drawable.menu_icon, R.drawable.cancel_icon)
                .addSubMenu(Color.parseColor("#485549"), R.drawable.profile_icon)
                .addSubMenu(Color.parseColor("#485549"), R.drawable.plus_icon)
                .addSubMenu(Color.parseColor("#485549"), R.drawable.explore_icon)
                .addSubMenu(Color.parseColor("#485549"), R.drawable.fav_icon)
                .addSubMenu(Color.parseColor("#485549"), R.drawable.help_icon)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {
                    @Override
                    public void onMenuSelected(int index) {

                        Handler h = new Handler();
                        switch (index) {
                            case 0:
                                Toast.makeText(HomeScreen.this, "Profile", Toast.LENGTH_SHORT).show();

                                h.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent i = new Intent(getApplicationContext() , ProfileActivity.class);
                                        i.putExtra("KEY" , username);
                                        startActivity(i);
                                    }
                                }, 1200);
                                break;
                            case 1:
                                Toast.makeText(HomeScreen.this, "Post Ad", Toast.LENGTH_SHORT).show();
                                h.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent i = new Intent(getApplicationContext() , PostAdActivity.class);
                                        i.putExtra("KEY" , username);
                                        i.putExtra("Long",longitude);
                                        i.putExtra("Lat",latitude);
                                        startActivity(i);
                                    }
                                }, 1200);

                                break;
                            case 2:
                               // Toast.makeText(HomeScreen.this, "Explore", Toast.LENGTH_SHORT).show();
                                h.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent i = new Intent(getApplicationContext() , ExploreActivity.class);
                                        i.putExtra("KEY" , username);
                                        startActivity(i);
                                    }
                                }, 1200);
                                break;
                            case 3:
                                Toast.makeText(HomeScreen.this, "Favorites", Toast.LENGTH_SHORT).show();
                                break;
                            case 4:
                                Toast.makeText(HomeScreen.this, "Help", Toast.LENGTH_SHORT).show();
                                //constraintLayout.setBackgroundColor(Color.parseColor("#fac4a2"));
                                break;

                        }
                    }
                });
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

    private void getLocationData() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HomeScreen.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    10);
        }
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HomeScreen.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    11);
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                //Initialize Location

                Location location = task.getResult();
                if (location != null) {
                    try {
                        //Initialize geoCOder
                        Geocoder geocoder = new Geocoder(HomeScreen.this,
                                Locale.getDefault());

                        longitude = String.valueOf(location.getLongitude());
                        latitude = String.valueOf(location.getLatitude());

                        //Initialize address list
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1);

                        locationText.setText(addresses.get(0).getLocality() +", "+addresses.get(0).getCountryName());

//                        longitude = addresses.get(0).getLongitude();
//                        latitude = addresses.get(0).getLatitude();


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void getName()
    {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users");
        db.child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot dataSnapshot = task.getResult();
                String name = dataSnapshot.child("Name").getValue(String.class);
                //name = name.replaceAll(" ","");
                hiText.setText("Hi "+name +"!");
            }
        });
    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        videoView.start();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        videoView.stopPlayback();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        videoView.suspend();
//    }
}