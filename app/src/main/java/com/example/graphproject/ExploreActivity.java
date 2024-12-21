package com.example.graphproject;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ExploreActivity extends AppCompatActivity {

    String username;

    static ArrayList<String> usersList= new ArrayList<>();
    static ArrayList<Integer> adCountList = new ArrayList<>();

    TextView name;
    DatabaseReference db;

    Integer count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        username = getIntent().getStringExtra("KEY");

        name = findViewById(R.id.abc);

        db = FirebaseDatabase.getInstance().getReference().child("Ads");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                usersList = new ArrayList<>();

                for(DataSnapshot data : snapshot.getChildren())
                {
                   String key = data.getKey();
                   // Toast.makeText(ExploreActivity.this, key, Toast.LENGTH_SHORT).show();
                    usersList.add(key);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        db = FirebaseDatabase.getInstance().getReference().child("Users");

        adCountList = new ArrayList<>();

        for(int i = 0 ; i < usersList.size() ; i++)
        {

            int finalI = i;

            db.child(usersList.get(finalI)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                   count =  snapshot.child("Ad Count").getValue(Integer.class);
                    Toast.makeText(ExploreActivity.this, count.toString(), Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            adCountList.add(count);

        }

       //name.setText(adCountList.get(0));



    }
}