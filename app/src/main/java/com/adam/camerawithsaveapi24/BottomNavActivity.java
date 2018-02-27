package com.adam.camerawithsaveapi24;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BottomNavActivity extends AppCompatActivity {
    //TODO: Remove Text and Image if not used
    private TextView mTextMessage;
    private ImageView mImageView;
    private FirebaseAuth mAuth;

    private String mCurrentPhotoPath;
    static final int REQUEST_IMAGE_CAPTURE = 1001;
    static final int REQUEST_TAKE_PHOTO = 1;
    private String todaysLogName;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    return true;

                case R.id.navigation_account_settings:
                    findViewById(R.id.navigation_account_settings).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mAuth.getCurrentUser() == null) {
                                dispatchSignInIntent();
                            } else {
                                dispatchSignOutIntent();
                            }
                        }
                    });
                    return true;

                case R.id.navigation_camera:
                    findViewById(R.id.navigation_camera).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (ContextCompat.checkSelfPermission(BottomNavActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                dispatchTakePictureIntent();
                            } else {
                                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                                    Toast.makeText(getApplicationContext(), "Permission Needed.", Toast.LENGTH_LONG).show();
                                }
                                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_IMAGE_CAPTURE);
                            }
                        }
                    });
                    return true;
            }
            return false;
        }
    };

    protected void dispatchSignInIntent() {
        Intent signInIntent = new Intent(this, SignInActivity.class);
        startActivity(signInIntent);
    }

    protected void dispatchSignOutIntent() {
        Intent signOutIntent = new Intent(this, AccountManagementActivity.class);
        startActivity(signOutIntent);
    }

    private void dispatchSignUpIntent() {
        Intent signUpIntent = new Intent(this, SignUpActivity.class);
        startActivity(signUpIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO: Maybe remove casts
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_bottom_nav);
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mAuth = FirebaseAuth.getInstance();

        try {
            createFoodLogFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void appendFoodLogFile(String[] strArr) throws IOException {
        String timeNow = new SimpleDateFormat("HH:mm").format(new Date());
        if (todaysLogName != null) {
            try {
                FileOutputStream outputStream = openFileOutput(todaysLogName, MODE_APPEND);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                for (String aStrArr : strArr) {
                    outputStreamWriter.write(timeNow + ": " + aStrArr);
                    outputStreamWriter.flush();
                }
                outputStreamWriter.close();
            } catch (FileNotFoundException e) {
                Log.e("FileNotFound: ", "File not found exception thrown");
                e.printStackTrace();
            }
        }
    }

    private void createFoodLogFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyy_MM_dd").format(new Date());
        String logFileName = "foodLog_" + timeStamp + ".txt";
        FileOutputStream outputStream;
        todaysLogName = logFileName;

        if (isFilePresent(logFileName)) {
            Log.i("File_Exists_Already", "File: " + logFileName + " exists already");
        } else {
            Log.i("Creating_File:", logFileName);
            try {
                outputStream = openFileOutput(logFileName, Context.MODE_APPEND);
                outputStream.write(logFileName.getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isFilePresent(String fileName) {
        String path = getBaseContext().getFilesDir().getAbsolutePath() + "/" + fileName;
//        Log.i("AbsolutePath: ",  path);
        File file = new File(path);
        return file.exists();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser curUser = mAuth.getCurrentUser();
        updateUI(curUser);
    }

    private void updateUI(FirebaseUser firebaseUser) {
        if (firebaseUser != null) {
            mTextMessage.setText(firebaseUser.getEmail().toString());
        }
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

                ex.printStackTrace();
            }
            // Continues only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.adam.camerawithsaveapi24.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_4YP_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Cam_Save");
        if (!storageDir.exists()) {
            if (!storageDir.mkdirs()) {
                Log.d("Y4P", "failed to create directory");
                return null;
            }
        }
        File image = File.createTempFile(imageFileName,
                ".jpg",
                storageDir
        );
        // Save file path for use to pass to other activities / methods
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
//            TODO: REMOVE COMMENTED OUT CODE
            // Show the thumbnail on ImageView
//            Uri imageUri = Uri.parse(mCurrentPhotoPath);
//            File file = new File(imageUri.getPath());
//            try {
//                InputStream ims = new FileInputStream(file);
//                Bitmap bm = BitmapFactory.decodeFile(mCurrentPhotoPath);
//                mImageView.setImageBitmap(bm);
//                mImageView.setImageBitmap(BitmapFactory.decodeStream(ims));
//            } catch (FileNotFoundException e) {
//                return;
//            }


//          TODO: REMOVE this?
//            final byte[] imageBytes = createByteArray(mCurrentPhotoPath);
//            recognizeConceptsActivity.onImagePicked(imageBytes);

            Intent displayPhotoIntent = new Intent(this, PhotoDisplayActivity.class);
            displayPhotoIntent.putExtra("fpath", mCurrentPhotoPath);
            startActivity(displayPhotoIntent);

            // ScanFile so it will be appeared on Gallery
//            MediaScannerConnection.scanFile(MainActivity.this,
//                    new String[]{imageUri.getPath()}, null,
//                    new MediaScannerConnection.OnScanCompletedListener() {
//                        public void onScanCompleted(String path, Uri uri) {
//                        }
//                    });
        }
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            mImageView.setImageBitmap(imageBitmap);
//        }
//    }

}
