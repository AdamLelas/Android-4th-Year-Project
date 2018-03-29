package com.adam.camerawithsaveapi24;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class AccountManagementActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private Button outButton, userDetailsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_signed_in);

        outButton = findViewById(R.id.sign_out_button);
        outButton.setOnClickListener(this);
        userDetailsButton = findViewById(R.id.edit_details_bt);
        userDetailsButton.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
    }

    private void editDetails(){
        Intent dets = new Intent(this, UserDetailsActivity.class);
        startActivity(dets);
    }

    private void redirect(){
        Intent redirect = new Intent(this, MainActivity.class);
        startActivity(redirect);
    }

    protected void signOut(){
        mAuth.signOut();
        redirect();
    }

    private void backupToCloud(){
//        TODO: get file from firebase, read that file, write local file to end of downloaded file, upload file to firebase
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();

        if(i == outButton.getId()){
            signOut();
        }
        else if(i == userDetailsButton.getId()){

            editDetails();
        }
    }


}
