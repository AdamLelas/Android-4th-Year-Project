package com.adam.camerawithsaveapi24;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.adam.camerawithsaveapi24.classes.UserDetails;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SignUpActivity";
    private static final int RC_SIGN_IN = 9001;


    //views and buttons etc
//    private TextView tvSkipForNow;
    private EditText editTextEmail;
    private EditText editTextPassword;

    private GoogleSignInClient googleSignInClient;

//    private Button signUpButton;
//    private Button googleSignInButton;

    //firebase authentication
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

//      START - Google sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
//      END - Google sign in

        // Views
        editTextEmail = findViewById(R.id.email_sign_up_et);
        editTextPassword = findViewById(R.id.password_sign_up_et);
        findViewById(R.id.skip_for_now_tv).setOnClickListener(this);
        findViewById(R.id.already_registered_tv).setOnClickListener(this);

        // Buttons
        findViewById(R.id.sign_up_btn).setOnClickListener(this);
        findViewById(R.id.google_sign_up_button).setOnClickListener(this);

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();


    }


    private boolean validateForm() {
        boolean valid = true;

        //validate email
        String email = editTextEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Required.");
            valid = false;
        } else {
            editTextEmail.setError(null);
        }

        //validate password
        String password = editTextPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Required.");
            valid = false;
        } else {
            editTextPassword.setError(null);
        }
        return valid;
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }
        showProgressBar();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "CreateUserEmail:success");
//                            redirectToBase();
                            Intent goDetails = new Intent(SignUpActivity.this, UserDetailsActivity.class);
                            startActivity(goDetails);
                        } else {
                            Log.w(TAG, "createUserEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Auth failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                        hideProgressBar();
                    }
                });

    }

    private void redirectToBase(){
        Intent baseIntent = new Intent(this, MainActivity.class);
        startActivity(baseIntent);
    }

    private void showProgressBar() {
        findViewById(R.id.sign_up_progress_bar).setVisibility(VISIBLE);
        findViewById(R.id.google_sign_up_button).setVisibility(GONE);
        findViewById(R.id.sign_up_btn).setVisibility(GONE);
        findViewById(R.id.already_registered_tv).setVisibility(GONE);
        findViewById(R.id.skip_for_now_tv).setVisibility(GONE);
    }

    private void hideProgressBar() {
        findViewById(R.id.sign_up_progress_bar).setVisibility(GONE);
        findViewById(R.id.google_sign_up_button).setVisibility(VISIBLE);
        findViewById(R.id.sign_up_btn).setVisibility(VISIBLE);
        findViewById(R.id.already_registered_tv).setVisibility(VISIBLE);
        findViewById(R.id.skip_for_now_tv).setVisibility(VISIBLE);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            Toast.makeText(SignUpActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
//                        TODO : hideProgressBar();
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.sign_up_btn) {
            createAccount(editTextEmail.getText().toString().trim(), editTextPassword.getText().toString().trim());
        } else if (i == R.id.google_sign_up_button) {
//TODO: google sign in
            signIn();

        } else if (i == R.id.skip_for_now_tv) {
//TODO: Anonymous sign in?
            Intent skipForNowIntent = new Intent(this, MainActivity.class);
            startActivity(skipForNowIntent);
            finish();
        } else if (i == R.id.already_registered_tv){
            Intent registeredAlreadyIntent = new Intent (this, SignInActivity.class);
            startActivity(registeredAlreadyIntent);
            finish();
        }


    }
}
