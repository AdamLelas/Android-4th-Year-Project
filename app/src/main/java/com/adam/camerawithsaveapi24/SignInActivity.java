package com.adam.camerawithsaveapi24;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth mAuth;

    private GoogleSignInClient googleSignInClient;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView tvSkip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


//      views
        editTextEmail = findViewById(R.id.email_sign_up_et);
        editTextPassword = findViewById(R.id.password_sign_up_et);

        //TODO: set click listener
        findViewById(R.id.tvSkipForNowSI).setOnClickListener(this);
        findViewById(R.id.not_registered_button).setOnClickListener(this);

//      buttons
        findViewById(R.id.google_sign_in_button).setOnClickListener(this);
        findViewById(R.id.email_sign_in_button).setOnClickListener(this);


//      START - Google sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
//      END - Google sign in

        googleSignInClient = GoogleSignIn.getClient(this, gso);

//      Init FireBaseAuth
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount acc = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(acc);
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        showProgressBar();


        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
                            displayBase();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            Toast.makeText(SignInActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                        hideProgressBar();
                    }
                });
    }

    private void displayBase(){
        Intent displayBaseIntent = new Intent(this, MainActivity.class);
        startActivity(displayBaseIntent);
    }

    private void showProgressBar(){
        findViewById(R.id.sign_in_progress_bar).setVisibility(VISIBLE);
        findViewById(R.id.google_sign_in_button).setVisibility(GONE);
        findViewById(R.id.email_sign_in_button).setVisibility(GONE);
    }

    private void hideProgressBar(){
        findViewById(R.id.sign_in_progress_bar).setVisibility(GONE);
        findViewById(R.id.google_sign_in_button).setVisibility(VISIBLE);
        findViewById(R.id.email_sign_in_button).setVisibility(VISIBLE);
    }


    private void signInEmailPassword(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressBar();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        hideProgressBar();

                    }
                });
        // [END sign_in_with_email]
    }


    private void sendVerification() {

    }

    protected void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {

        } else {

        }
    }

    private boolean validateForm() {
        boolean valid = true;

        //validate email
        String email = editTextEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Required");
            valid = false;
        } else {
            editTextEmail.setError(null);
        }

        //validate password
        String password = editTextPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Required");
            valid = false;
        } else {
            editTextPassword.setError(null);
        }
        return valid;
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tvSkipForNowSI) {
//            TODO: anonymous sign in?
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (i == R.id.email_sign_in_button) {
            signInEmailPassword(editTextEmail.getText().toString().trim(),
                    editTextPassword.getText().toString().trim());
        } else if (i == R.id.google_sign_in_button) {
            signIn();
        } else if (i == R.id.not_registered_button) {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        }
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

}
