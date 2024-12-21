package com.example.graphproject;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class SignupActivity extends AppCompatActivity {

    TextView uploadImgText;
    Uri imageUri;
    ImageView profileImg;
    private final int GALLERY_REQ_CODE = 1_000;
    AppCompatButton signupBtn;
    TextInputEditText nameEditTxt, usernameEditTxt, passwordEditTxt;
    String profileUri;


    //database variable
    // DatabaseReference dbReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://graph-project-cffcb-default-rtdb.firebaseio.com/");
    StorageReference storageReference;
    DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //HIDING THE NAVIGATION BAR

        hideNavigationBar();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.TYPE_STATUS_BAR);


        //binding the views

        uploadImgText = findViewById(R.id.uploadimageText);
        profileImg = findViewById(R.id.profileImgView);
        signupBtn = findViewById(R.id.signupBtn);
        nameEditTxt = findViewById(R.id.nameText);
        usernameEditTxt = findViewById(R.id.usernameText);
        passwordEditTxt = findViewById(R.id.passwordText);

        //Intializing the firebase variables
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        dbReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://graph-project-cffcb-default-rtdb.firebaseio.com/");


        //underlining the upload img text
        uploadImgText.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        uploadImgText.setOnClickListener(view -> {
//            Intent iGallery = new Intent(Intent.ACTION_PICK);
//            iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            startActivityForResult(iGallery, GALLERY_REQ_CODE);
            openFileChooser();

        });

        //sign up functionality
        signupBtn.setOnClickListener(view -> {
            String name = nameEditTxt.getText().toString();
            String username = usernameEditTxt.getText().toString();
            String password = passwordEditTxt.getText().toString();
            //String uri = imageUri.toString();
            //check if user has uploaded a profile pic
            if(imageUri == null)
            {
                uploadImgText.setError("Please Upload an Image");
            }
            else if (name.isEmpty()) {
                nameEditTxt.setError("Please enter name");
            } else if (username.isEmpty()) {
                usernameEditTxt.setError("Please enter username");
            } else if (password.isEmpty()) {
                passwordEditTxt.setError("Please enter name");
            } else {

                ///send data to firebase
//                    dbReference.child("Users").child(username).child("Name").setValue(name);
//                    dbReference.child("Users").child(username).child("Username").setValue(username);
//                    dbReference.child("Users").child(username).child("Password").setValue(password);
                //dbReference.child("Users").child(username).child("Uri").setValue(uri);


                //uploadFile();
                if (imageUri != null) {

                    //String uploadId = dbReference.push().getKey();

                    StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

                    fileReference.putFile(imageUri).
                            addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

                                            profileUri = uri.toString();
                                            uploadProfilePic(uri.toString(),username);
                                        }
                                    });

                                }
                            }).
                            addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    dbReference.child("Users").child(username).child("Name").setValue(name);
                                    dbReference.child("Users").child(username).child("Username").setValue(username);
                                    dbReference.child("Users").child(username).child("Password").setValue(password);
                                    dbReference.child("Users").child(username).child("Ad Count").setValue(0);


                                }
                            });


                    //dbReference.child("Users").child(username).child("Profile Pic").setValue(profileUri);


                    //  String uploadId = databaseReference.push().getKey();


                    // Toast.makeText(SignupActivity.this, "Upload Successful!", Toast.LENGTH_SHORT).show();

                }

                Toast.makeText(SignupActivity.this, "User registered successfully", Toast.LENGTH_LONG).show();

                Intent i = new Intent(getApplicationContext(), HomeScreen.class);
                i.putExtra("KEY", usernameEditTxt.getText().toString());
                startActivity(i);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQ_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {

//                profileImg.setImageURI(data.getData());
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(profileImg);
         //   Picasso.with(this).load(imageUri).into(profileImg);
        }
    }

    private void openFileChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, GALLERY_REQ_CODE);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (imageUri != null) {

            String uploadId = dbReference.push().getKey();

            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

            fileReference.putFile(imageUri).
                    addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    profileUri = uri.toString();

                                }
                            });

                        }
                    }).
                    addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


            dbReference.child("Users").child(usernameEditTxt.getText().toString()).child("Profile Pic").setValue(profileUri);


            //  String uploadId = databaseReference.push().getKey();


            // Toast.makeText(SignupActivity.this, "Upload Successful!", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "No File Selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadProfilePic(String uri,String username)
    {
        dbReference.child("Users").child(username).child("Profile Pic").setValue(uri);
    }


}