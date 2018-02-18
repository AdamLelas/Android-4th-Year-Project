package com.adam.camerawithsaveapi24;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class AccountManagementActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_signed_in);

        findViewById(R.id.sign_out_button).setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();

        if(i == R.id.sign_out_button){
            signOut();
        }
    }

    private void redirect(){
        Intent redirect = new Intent(this, BottomNavActivity.class);
        startActivity(redirect);
    }

    protected void signOut(){
        mAuth.signOut();
        redirect();
    }

    private void backupToCloud(){
//        TODO: get file from firebase, read that file, write local file to end of downloaded file, upload file to firebase
    }


}
