package com.adam.camerawithsaveapi24;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adam.camerawithsaveapi24.classes.FoodItem;
import com.adam.camerawithsaveapi24.classes.UserDetails;
import com.adam.camerawithsaveapi24.tools.AdapterLogList;
import com.adam.camerawithsaveapi24.tools.AdapterQuickList;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.view.View.*;
import static com.adam.camerawithsaveapi24.tools.Utility.*;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private String mCurrentPhotoPath;
    static final int REQUEST_IMAGE_CAPTURE = 1001;
    static final int REQUEST_TAKE_PHOTO = 1;
    private String todaysLogName;
    private String timevalue;
    private Date datetime;
    private UserDetails userDetails;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private FirebaseUser user;
    private ArrayList<FoodItem> foodItemsList;
    private Button printButton;

    private ListView loglist;
    private AdapterLogList adapterLogList;

    private TextView tvDate, tvCalRem, tvBudgetVal, tvConsVal;
    private ProgressBar calPB;

    private final int places = 0;
    private ArrayList<FoodItem> quickListArray = new ArrayList();


    private double dailyCarb, dailyCal, dailyChol, dailyFib, dailyPro, dailySatFat, dailyTotFat, calRec;

    private LinearLayout progAdd;
    private LayoutInflater inflater;
    private ImageView dateminus, dateplus;
    private View includeLayout;

    private SwipeMenuListView quickListView;
    private SwipeMenuCreator creator;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_more_info:

                    if (mAuth.getCurrentUser() != null) {
                        Intent moreInfoIntent = new Intent(MainActivity.this, MoreInformationActivity.class);
                        startActivity(moreInfoIntent);
                    } else {
                        dispatchSignInIntent();
                    }

                case R.id.navigation_home:

                    return true;

                case R.id.navigation_account_settings:

                    if (mAuth.getCurrentUser() == null) {
                        dispatchSignInIntent();
                    } else {
                        dispatchSignOutIntent();
                    }

                    return true;

                case R.id.navigation_camera:

                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        dispatchTakePictureIntent();
                    } else {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                            Toast.makeText(getApplicationContext(), "Permission Needed.", Toast.LENGTH_LONG).show();
                        }
                        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_IMAGE_CAPTURE);
                    }
                    return true;
                case R.id.navigation_add_more:
                    launchAddMoreDialog();
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_bottom_nav);

        //views
        includeLayout = findViewById(R.id.include_botnav);
        tvBudgetVal = includeLayout.findViewById(R.id.tv_budget_val);
        tvCalRem = includeLayout.findViewById(R.id.tv_cal_rem);
        tvConsVal = includeLayout.findViewById(R.id.tv_cons_val);
        tvDate = includeLayout.findViewById(R.id.tv_date);
        calPB = includeLayout.findViewById(R.id.calories_percent_pb);
        progAdd = includeLayout.findViewById(R.id.progAdd);
        dateminus = includeLayout.findViewById(R.id.date_minus_bt);
        dateminus.setOnClickListener(this);
        dateplus = includeLayout.findViewById(R.id.date_plus_bt);
        dateplus.setOnClickListener(this);
        loglist = includeLayout.findViewById(R.id.log_list_lv);

//        TODO: don't need this?
        inflater = LayoutInflater.from(this);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        foodItemsList = new ArrayList<>();

        //      Firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();
        user = mAuth.getCurrentUser();

        //calculates and sets the recommended calories
        calRec = getRecCal();

//        Misc
        datetime = new Date();
        timevalue = new SimpleDateFormat("dd-MM-yyyy").format(datetime);
        tvDate.setText(timevalue);
        if (user != null) {
            gatherTodaysFood();
            gatherUserDetails();
        }
        try {
            createFoodLogFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0x66, 0xff, 0x33)));
                // set item width
                openItem.setWidth(170);
                openItem.setIcon(R.drawable.ic_tick);
                // add to menu
                menu.addMenuItem(openItem);
            }
        };


    }

    private void launchQuickAddDialog() {
        final Dialog quickDialog = new Dialog(this);
        quickDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        quickDialog.setContentView(R.layout.quickadd_dialog);

        quickListView = quickDialog.findViewById(R.id.quick_list_lv);
        final TextView tv = quickDialog.findViewById(R.id.empty_list_tv);


        quickListView.setMenuCreator(creator);
        DatabaseReference child = dbRef.child("users").child(user.getUid()).child("user-items");
        child.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

//                    populate quicklist

                    quickListArray.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        FoodItem foodItem = ds.getValue(FoodItem.class);
                        foodItem.setFood_name(ds.getKey());
                        System.out.println(foodItem);
                        quickListArray.add(foodItem);
                        AdapterQuickList adapterQuickList = new AdapterQuickList(MainActivity.this, 0, quickListArray);
                        quickListView.setAdapter(adapterQuickList);
                    }


//                    AdapterQuickList adapterQuickList = new AdapterQuickList(MainActivity.this, 0, quickListArray);
//                    quickListView.setAdapter(adapterQuickList);

                    quickListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                            switch (index) {
                                case 0:
                                    System.out.println("Add to fb");
                                    sendDataToFB(quickListArray.get(position));
//                                    quickDialog.dismiss();
                                    itemAddedDialog();
                                    break;
                            }
                            return false;
                        }
                    });


                } else {
                    tv.setVisibility(VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        quickDialog.show();
    }

    private void itemAddedDialog() {
        AlertDialog.Builder itemAddedDialog = new AlertDialog.Builder(this);
//        final TextView et = new TextView(this);
        itemAddedDialog.setMessage("Item added");
        itemAddedDialog.show();
    }

    private void sendDataToFB(final FoodItem item) {
        System.out.println(item);
        final String timeNow = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        DatabaseReference child = dbRef.child("users").child(user.getUid()).child("log").child(timeNow).child("food").child(item.getFood_name());

        child.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    FoodItem tFood = (dataSnapshot.getValue(FoodItem.class));
                    dbRef.child("users").child(user.getUid()).child("log").child(timeNow).child("food").child(item.getFood_name()).child("servings").setValue(tFood.getServings() + 1);
                } else {
                    dbRef.child("users").child(user.getUid()).child("log").child(timeNow).child("food").child(item.getFood_name()).setValue(item);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void launchAddMoreDialog() {
        final Dialog addMoreDialog = new Dialog(this);
        addMoreDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addMoreDialog.setContentView(R.layout.add_more_dialog);
        Button typed = addMoreDialog.findViewById(R.id.add_more_typed);
        Button quick = addMoreDialog.findViewById(R.id.add_more_quick);
        quick.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                launchQuickAddDialog();
                addMoreDialog.dismiss();
            }
        });
        typed.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                launchTypedSearchDialog();
                addMoreDialog.dismiss();
            }
        });

        addMoreDialog.show();
    }

    private void afterTypedSearch(String searchTerm) {
        Intent intent = new Intent(this, PhotoDisplayActivity.class);
        intent.putExtra("searchTerm", searchTerm);
        intent.putExtra("fpath", "placeholder");
        startActivity(intent);
    }

    private void launchTypedSearchDialog() {
        AlertDialog.Builder searchDialog = new AlertDialog.Builder(this);
        final EditText et = new EditText(this);
        searchDialog.setMessage("Type the name of each item");
//        searchDialog.setTitle("Title");

        searchDialog.setView(et);

        searchDialog.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String et_value = et.getText().toString();
                afterTypedSearch(et_value);
            }
        });

        searchDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent runaway = new Intent(MainActivity.this, MainActivity.class);
                startActivity(runaway);
            }
        });

        searchDialog.show();
    }


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
    protected void onStart() {
        super.onStart();
//        updateUI(user);

        if (user != null) {

        } else {
            System.out.println("USER WAS NULL");
        }
    }

    protected double getRecCal() {
//        TODO: Calculate recommended calories height/weight or w/e
        return 2000;
    }

    private void gatherTodaysFood() {
        DatabaseReference logRef = dbRef.child("users").child(user.getUid()).child("log").child(timevalue).child("food");
        logRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                extractFoodLog(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
    }


    public void createTVs() {
//TODO: ??
    }

    public void extractFoodLog(DataSnapshot dataSnapshot) {
        foodItemsList.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            FoodItem tFood = (ds.getValue(FoodItem.class));
            if (tFood != null) {
                tFood.setFood_name(ds.getKey());
            }
            foodItemsList.add(tFood);
        }
        getDailyTotals();
        adapterLogList = new AdapterLogList(this, 0, foodItemsList);
        loglist.setAdapter(adapterLogList);
    }

    //    TODO: remove extra function call unless more code added to this function
    private void dateChanged() {
        gatherTodaysFood();
    }

    private void updateUI() {
        tvDate.setText(timevalue);
        tvConsVal.setText(String.valueOf(roundSafely(dailyCal, places)));
        tvCalRem.setText(String.valueOf(roundSafely((calRec - dailyCal), places)));
        tvBudgetVal.setText(String.valueOf(roundSafely(calRec, places)));
    }


    public void getDailyTotals() {
//        Reset Daily totals
        dailyCarb = 0;
        dailyCal = 0;
        dailyChol = 0;
        dailyFib = 0;
        dailyPro = 0;
        dailySatFat = 0;
        dailyTotFat = 0;

//        Populate daily totals
        for (FoodItem i : foodItemsList) {
            double servings = i.getServings();
            dailyCarb = dailyCarb + (i.getCarbs() * servings);
            dailyCal = dailyCal + (i.getCalories() * servings);
            dailyChol = dailyChol + (i.getCholesterol() * servings);
            dailyFib = dailyFib + (i.getFiber() * servings);
            dailyPro = dailyPro + (i.getProtein() * servings);
            dailySatFat = dailySatFat + (i.getSaturated_fat() * servings);
            dailyTotFat = dailyTotFat + (i.getTotal_fat() * servings);
        }
        updateUI();
    }

    //TODO: REMOVE?
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

//    private void updateUI(FirebaseUser firebaseUser) {
//        if (firebaseUser != null) {
////            mTextMessage.setText(firebaseUser.getEmail().toString());
//        }
//    }


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


    @Nullable
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


    @Override
    public void onClick(View v) {
        int i = v.getId();

        if (i == dateminus.getId()) {
            datetime = changeDate(datetime, -1);
            timevalue = new SimpleDateFormat("dd-MM-yyyy").format(datetime);
            dateChanged();
        } else if (i == dateplus.getId()) {
            //if date is today don't advance
            if (!timevalue.equalsIgnoreCase(new SimpleDateFormat("dd-MM-yyyy").format(new Date()))) {
                datetime = changeDate(datetime, 1);
                timevalue = new SimpleDateFormat("dd-MM-yyyy").format(datetime);
                dateChanged();
            }
        }
    }
}
