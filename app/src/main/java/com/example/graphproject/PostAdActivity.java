package com.example.graphproject;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class PostAdActivity extends AppCompatActivity {

    static final int GALLERY_REQ_CODE = 1;
    TextView uploadImgText;
    ArrayList<Uri> adImages = new ArrayList<>();
    ViewPager adImgViewPager;

    AppCompatButton postAdBtn;
    ImageView backBtn;
    String profileUri;

    TextInputEditText priceText, areaText, adTitleText, descriptionText, phoneText, bedroomText, bathroomText, floorText;

    String username;
    Integer userAdCount;

    //database variable
    StorageReference storageReference;
    DatabaseReference dbReference;

    //location data
    String longitude;
    String latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_ad);

        adImages.clear();

        username = getIntent().getStringExtra("KEY");
        longitude =getIntent().getStringExtra("Long");
        latitude =getIntent().getStringExtra("Lat");


        //get user ad count
        getAdCount(username);

        //HIDING THE NAVIGATION BAR
        hideNavigationBar();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.TYPE_STATUS_BAR);


        //Initializing the firebase variables
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        dbReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://graph-project-cffcb-default-rtdb.firebaseio.com/");

        //binding the views
        uploadImgText = findViewById(R.id.uploadimageText);
        adImgViewPager = findViewById(R.id.adImgViewPager);
        postAdBtn = findViewById(R.id.postAdBtn);
        priceText = findViewById(R.id.priceText);
        areaText = findViewById(R.id.areaUnitText);
        adTitleText = findViewById(R.id.titleText);
        descriptionText = findViewById(R.id.descriptionText);
        phoneText = findViewById(R.id.phoneText);
        bedroomText = findViewById(R.id.bedroomText);
        bathroomText = findViewById(R.id.bathroomText);
        floorText = findViewById(R.id.floorText);
        backBtn = findViewById(R.id.backBtn);

        //underlining the upload img text
        uploadImgText.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        //back button functionality
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //upload ad images from gallery
        uploadImgText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImagesFromGallery();
            }
        });

        //post ad functionality
        postAdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //check all the edit texts and image sliders
                String price = priceText.getText().toString();
                String areaUnit = areaText.getText().toString();
                String adTitle = adTitleText.getText().toString();
                String description = descriptionText.getText().toString();
                String bedrooms = bedroomText.getText().toString();
                String bathrooms = bathroomText.getText().toString();
                String floors = floorText.getText().toString();
                String phoneNumText = phoneText.getText().toString();

                if (price.isEmpty()) {
                    priceText.setError("Please enter the price");
                } else if (areaUnit.isEmpty()) {
                    areaText.setError("Please enter the area unit");
                } else if (adTitle.isEmpty()) {
                    adTitleText.setError("Please enter the ad title");
                } else if (description.isEmpty()) {
                    descriptionText.setError("Please enter the description");
                } else if (phoneNumText.isEmpty()) {
                    phoneText.setError("Please enter the phone number");
                }else if (bedrooms.isEmpty()) {
                    bedroomText.setError("Please enter the bedrooms");
                }else if (bathrooms.isEmpty()) {
                    bathroomText.setError("Please enter the bathrooms");
                }else if (floors.isEmpty()) {
                    floorText.setError("Please enter the floors");
                } else
                //upload images to database
                {
                    if (adImages.size() > 0) {

                        ArrayList<String> uriList = new ArrayList<>();

                        for (int i = 0; i < adImages.size(); i++) {
                            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(adImages.get(i)));

                            int finalI = i;
                            fileReference.putFile(adImages.get(i)).
                                    addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    //uriList.add(uri.toString());
                                                    uploadUriToRealtime(uri.toString(), finalI);
                                                }
                                            });
                                        }
                                    }).
                                    addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(PostAdActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                            //dbReference.child("Ads").child(username).child("Adsss").setValue("sasdasdasd");
                                        }
                                    });

                        }

                        //send post ad data other than images
                        dbReference.child("Ads").child(username).child("Ad" + userAdCount.toString()).child("Ad Title").setValue(adTitle);
                        dbReference.child("Ads").child(username).child("Ad" + userAdCount.toString()).child("Price").setValue(Integer.valueOf(price));
                        dbReference.child("Ads").child(username).child("Ad" + userAdCount.toString()).child("Area Unit").setValue(Integer.valueOf(areaUnit));
                        dbReference.child("Ads").child(username).child("Ad" + userAdCount.toString()).child("Description").setValue(description);
                        dbReference.child("Ads").child(username).child("Ad" + userAdCount.toString()).child("Phone No").setValue(String.valueOf(phoneNumText));
                        dbReference.child("Ads").child(username).child("Ad" + userAdCount.toString()).child("Longitude").setValue(longitude);
                        dbReference.child("Ads").child(username).child("Ad" + userAdCount.toString()).child("Latitude").setValue(latitude);
                        dbReference.child("Ads").child(username).child("Ad" + userAdCount.toString()).child("Image Count").setValue(adImages.size());
                        dbReference.child("Ads").child(username).child("Ad" + userAdCount.toString()).child("Bedrooms").setValue(bedrooms);
                        dbReference.child("Ads").child(username).child("Ad" + userAdCount.toString()).child("Bathrooms").setValue(bathrooms);
                        dbReference.child("Ads").child(username).child("Ad" + userAdCount.toString()).child("Floors").setValue(floors);

                        //increment in ad count of the user
                        dbReference.child("Users").child(username).child("Ad Count").setValue(userAdCount);


                    } else {
                        Toast.makeText(PostAdActivity.this, "Please select images", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            //when images are done uploading then send all the other data

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

    private void chooseImagesFromGallery() {
        Intent i = new Intent();
        i.setType("image/*");
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Pictures"), GALLERY_REQ_CODE);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQ_CODE && resultCode == RESULT_OK) {
            if (data.getClipData() != null) {
                int x = data.getClipData().getItemCount();

                for (int i = 0; i < x; i++) {
                    adImages.add(data.getClipData().getItemAt(i).getUri());
                }

                UploadViewPagerAdapter adapter = new UploadViewPagerAdapter(this, adImages);
                adImgViewPager.setAdapter(adapter);

            }
        }
    }


    private void getAdCount(String username) {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users");
        db.child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot dataSnapshot = task.getResult();
                Integer adCount = dataSnapshot.child("Ad Count").getValue(Integer.class);
                userAdCount = adCount + 1;
            }
        });
    }

    private void uploadUriToRealtime(String uri, int index) {
        index++;
        dbReference.child("Ads").child(username).child("Ad" + userAdCount.toString()).child("Images").child("Image" + index).setValue(uri);
    }
}