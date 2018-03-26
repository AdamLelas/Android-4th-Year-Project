package com.adam.camerawithsaveapi24;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.Map;

import static com.adam.camerawithsaveapi24.Tools.Utility.*;

public class UserDetailsActivity extends AppCompatActivity implements View.OnClickListener {


    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private FirebaseUser user;

    private TextView username;
    private EditText height, weight, age, goal;
    private Spinner gender, activeLevel;
    private Button save, cancel;
    private Map<String, Object> values;
    private UserDetails userDetails;
//    private List<String> genderList, activityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();
        user = mAuth.getCurrentUser();

        height = findViewById(R.id.edl_height_et);
        weight = findViewById(R.id.edl_weight_et);
        age = findViewById(R.id.edl_age_et);
        goal = findViewById(R.id.edl_goal_weight_et);
        gender = findViewById(R.id.edl_gender_dropdown);
        activeLevel = findViewById(R.id.edl_activity_level_dropdown);
        username = findViewById(R.id.edl_username_tv);

        save = findViewById(R.id.edl_save_bt);
        save.setOnClickListener(this);

        cancel = findViewById(R.id.edl_cancel_bt);
        cancel.setOnClickListener(this);

        ArrayAdapter genderAdapter = ArrayAdapter.createFromResource(this, R.array.gender_array, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(genderAdapter);

        ArrayAdapter activeAdapter = ArrayAdapter.createFromResource(this, R.array.active_array, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeLevel.setAdapter(activeAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        gatherUserDetails();
    }

    private void redirectToMain() {
        Intent intent = new Intent(this, BottomNavActivity.class);
        startActivity(intent);
    }


    private void gatherUserDetails() {
        DatabaseReference userDetailsRef = dbRef.child("users").child(user.getUid()).child("user-details");
        userDetailsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                extractUserDetails(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void extractUserDetails(DataSnapshot ds) {
        userDetails = ds.getValue(UserDetails.class);

        try {
            height.setText(String.valueOf((userDetails.getHeight())));
            weight.setText(String.valueOf((userDetails.getWeight())));
            age.setText(String.valueOf((userDetails.getAge())));
            goal.setText(String.valueOf((userDetails.getGoal_weight())));
            if (userDetails.getUsername() != null) {
                username.setText(userDetails.getUsername());
            }
            gender.setSelection(getIndex(gender, userDetails.getGender()));
            activeLevel.setSelection(getIndex(activeLevel, userDetails.getActivity_level()));

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }



    private void saveUpdate() {
        UserDetails tu = new UserDetails();
        tu.setHeight(parseDoubleSafely(height.getText().toString()));
        tu.setWeight(parseDoubleSafely(weight.getText().toString()));
        tu.setAge(parseIntSafely(age.getText().toString()));
        tu.setGoal_weight(parseDoubleSafely(goal.getText().toString()));
        tu.setActivity_level(activeLevel.getSelectedItem().toString());
        tu.setGender(gender.getSelectedItem().toString());
        if (username != null) {
            tu.setUsername(username.getText().toString());
        }

        dbRef.child("users").child(user.getUid()).child("user-details").setValue(tu);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == cancel.getId()) {
            Intent botnav = new Intent(this, BottomNavActivity.class);
            startActivity(botnav);
        } else if (i == save.getId()) {
            saveUpdate();
        }
        redirectToMain();
    }
}





