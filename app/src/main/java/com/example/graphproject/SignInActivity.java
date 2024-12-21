package com.example.graphproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity {

    TextView signupText;

    TextInputEditText usernameEditTxt, passwordEditTxt;
    AppCompatButton loginBtn;

    //database variable
    DatabaseReference dbReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://graph-project-cffcb-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //HIDING THE NAVIGATION BAR
        hideNavigationBar();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.TYPE_STATUS_BAR);


        //binding the views
        signupText = (TextView) findViewById(R.id.signupText);
        usernameEditTxt = findViewById(R.id.usernameText);
        passwordEditTxt = findViewById(R.id.passwordText);
        loginBtn = findViewById(R.id.loginBtn);

        signupText.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), SignupActivity.class);
            startActivity(i);
        });

        //login functionality
        loginBtn.setOnClickListener(view -> {
            String username = Objects.requireNonNull(usernameEditTxt.getText()).toString();
            String password = Objects.requireNonNull(passwordEditTxt.getText()).toString();
            //String uri = imageUri.toString();

            if (username.isEmpty()) {
                usernameEditTxt.setError("Please enter username");
            } else if (password.isEmpty()) {
                passwordEditTxt.setError("Please enter password");
            } else {

                dbReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //check if user exists in database
                        if (snapshot.hasChild(username)) {
                            String passwordFromDb = snapshot.child(username).child("Password").getValue(String.class);

                            if (password.equals(passwordFromDb)) {
                                Toast.makeText(SignInActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(), HomeScreen.class);
                                i.putExtra("KEY", usernameEditTxt.getText().toString());
                                startActivity(i);
                            } else {
                                passwordEditTxt.setError("Wrong password");
                            }

                        } else {
                            usernameEditTxt.setError("User not found");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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


}
