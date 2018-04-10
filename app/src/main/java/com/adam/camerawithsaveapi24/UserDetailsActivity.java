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

import com.adam.camerawithsaveapi24.classes.UserDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.Map;

import static com.adam.camerawithsaveapi24.tools.Utility.*;

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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
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
        try {
            userDetails = ds.getValue(UserDetails.class);


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
            if (height.getText().toString().trim().equalsIgnoreCase("")) {
                height.setError("Height is required.");
            } else if (weight.getText().toString().trim().equalsIgnoreCase("")) {
                weight.setError("Weight is required.");

            } else if (age.getText().toString().trim().equalsIgnoreCase("")) {
                age.setError("Age is required.");

            } else if (goal.getText().toString().trim().equalsIgnoreCase("")) {
                goal.setError("Goal is required.");
            } else {
                if (parseDoubleSafely(height.getText().toString()) < 60 || parseDoubleSafely(height.getText().toString()) > 300) {
                    height.setError("Range: 60cm - 300cm");
                } else if (parseDoubleSafely(weight.getText().toString()) < 2 || parseDoubleSafely(weight.getText().toString()) > 300) {
                    weight.setError("Range: 2kg - 300kg");
                } else if (parseDoubleSafely(age.getText().toString()) < 5 || parseDoubleSafely(age.getText().toString()) > 100) {
                    age.setError("Range: 5 - 100");
                } else if (parseDoubleSafely(goal.getText().toString()) < 2 || parseDoubleSafely(goal.getText().toString()) > 300) {
                    goal.setError("Range: 2kg - 300kg");
                } else {
                    redirectToMain();
                }

            }
        } else if (i == save.getId()) {
            if (height.getText().toString().trim().equalsIgnoreCase("")) {
                height.setError("Height is required.");
            } else if (weight.getText().toString().trim().equalsIgnoreCase("")) {
                weight.setError("Weight is required.");

            } else if (age.getText().toString().trim().equalsIgnoreCase("")) {
                age.setError("Age is required.");

            } else if (goal.getText().toString().trim().equalsIgnoreCase("")) {
                goal.setError("Goal is required.");
            } else {
                if (parseDoubleSafely(height.getText().toString()) < 60 || parseDoubleSafely(height.getText().toString()) > 300) {
                    height.setError("Range: 60cm - 300cm");
                } else if (parseDoubleSafely(weight.getText().toString()) < 2 || parseDoubleSafely(weight.getText().toString()) > 300) {
                    weight.setError("Range: 2kg - 300kg");
                } else if (parseDoubleSafely(age.getText().toString()) < 5 || parseDoubleSafely(age.getText().toString()) > 100) {
                    age.setError("Range: 5 - 100");
                } else if (parseDoubleSafely(goal.getText().toString()) < 2 || parseDoubleSafely(goal.getText().toString()) > 300) {
                    goal.setError("Range: 2kg - 300kg");
                } else {
                    saveUpdate();
                    redirectToMain();
                }
            }


        }
    }
}





