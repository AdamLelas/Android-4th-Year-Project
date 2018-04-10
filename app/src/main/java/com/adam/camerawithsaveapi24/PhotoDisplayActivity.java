package com.adam.camerawithsaveapi24;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;


import com.adam.camerawithsaveapi24.classes.FoodItem;
import com.adam.camerawithsaveapi24.tools.AdapterConfirmItemsList;
import com.adam.camerawithsaveapi24.tools.App;
import com.adam.camerawithsaveapi24.tools.NutritionSingleton;
import com.adam.camerawithsaveapi24.tools.Utility;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import clarifai2.api.ClarifaiResponse;
import clarifai2.dto.input.ClarifaiImage;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.model.ConceptModel;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;

import static android.view.View.*;
import static com.adam.camerawithsaveapi24.tools.Utility.*;

public class PhotoDisplayActivity extends AppCompatActivity implements OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private FirebaseUser user;
    private StorageReference storageRef;
    private FirebaseStorage storage;

    private final int list_size1 = 350, list_size2 = 700, list_size3 = 1050;

    private static final String TAG = "PhotoDisplayActivity";
    private final int ARRAYSIZE = 5;

    private String fpath;
    private byte[] imageBytes;
    protected List<Concept> concepts = new ArrayList<>();
    private Button b1, b2, b3;
    private String milkSelection, sweetenerSelection;
    private ArrayList<FoodItem> extrasList = new ArrayList<>();

    //radio button dialogs
    private EditText dialogMilkET;
    private TextView dialogMilkTV, sugartv, sweettv, honeytv;
    private int milkAmtML;
    private double sweetenerAmountTsp;
    private Button milkbtn, sweetbtn, confMilkBtn,
            sugarpp, sweetenerpp, honeypp,
            sugarmm, sweetenermm, honeymm,
            sweetcancel, sweetconf;
    private double sweetvalue, honeyvalue, sugarvalue;
    private boolean otherbool = false;
    private FoodItem customizedFood = new FoodItem();

    private boolean buttonFirstClick = true;
    private Button noneOfThese, uploadButton;
    private ImageView imageView;
    private ProgressBar dialog;

    private int selectValue;
    private String searchedWord = null;

    private AdapterConfirmItemsList adapterConfirmItemsList;
    private SwipeMenuListView selectedItemsListView;

    private LinearLayout confTopBar, confBottomBar;
    private LinearLayout ll_selected_list;
    private LinearLayout extras_ll;
    private ScrollView confItemScrollViewParent;
    private View includedConfLayout;
    private ImageButton backArrow;
    private ImageView backToMainArrow;
    private ImageButton confConfirmSelection;
    private EditText servingAmount;
    private int servingAmountValue;


    private TextView confItemName, botCal, botCarb, botFat, botProtein,
            calVal, totFatVal, satFatVal, cholVal, carbVal,
            fibVal, sugVal, protVal, potasVal;

    private List<FoodItem> foodItemsList = new ArrayList<>();

    //    maybe make an array instead
    private String itemNameLocal;
    private double calValLocal, totFatValLocal, satFatValLocal,
            cholValLocal, carbValLocal, fibValLocal, sugValLocal,
            protValLocal, potasValLocal, servingWeightGramsLocal;


    //LinearLayout - buttons
    private LinearLayout L11, L12, L13, L21, L22, L23, L31, L32, L33;
    private TextView L11TV1, L11TV2, L12TV1, L12TV2, L13TV1, L13TV2,
            L21TV1, L21TV2, L22TV1, L22TV2, L23TV1, L23TV2,
            L31TV1, L31TV2, L32TV1, L32TV2, L33TV1, L33TV2;

    //Nutritionix Search Instant Storage
    private List<String> searchInstantNamesArray1 = new ArrayList<>();
    private List<String> searchInstantServingUnitArray1 = new ArrayList<>();

    private List<String> searchInstantNamesArray2 = new ArrayList<>();
    private List<String> searchInstantServingUnitArray2 = new ArrayList<>();

    private List<String> searchInstantNamesArray3 = new ArrayList<>();
    private List<String> searchInstantServingUnitArray3 = new ArrayList<>();

    private ArrayList<FoodItem> selectedList = new ArrayList<>();

    //    private String queryString = null;
    private final String NUTRITION_INSTANT_URL_PREFIX = "https://trackapi.nutritionix.com/v2/search/instant?query=";
    private final String NUTRITION_INSTANT_URL_POSTFIX = "&branded=false";

    private final String NUTRITION_NUTRIENTS_URL = "https://trackapi.nutritionix.com/v2/natural/nutrients";

    private static final String DEBUG_GESTURES_TAG = "Gestures";
    private GestureDetector detector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_display);

        //Swipe Gestures
//        detector = new GestureDetector(this, new MyGestureListener());


        //Firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();
        user = mAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        backToMainArrow = findViewById(R.id.back_arrow_pda_iv);
        backToMainArrow.setOnClickListener(this);

        //Set up views findViewById
        imageView = findViewById(R.id.photoDisplayView);
        b1 = findViewById(R.id.food_select_button_one);
        b1.setOnClickListener(this);
        b2 = findViewById(R.id.food_select_button_two);
        b2.setOnClickListener(this);
        b3 = findViewById(R.id.food_select_button_three);
        b3.setOnClickListener(this);

        noneOfThese = findViewById(R.id.food_select_button_none);
        noneOfThese.setOnClickListener(this);

        uploadButton = findViewById(R.id.upload_to_firebase_button);
        uploadButton.setOnClickListener(this);

        ll_selected_list = findViewById(R.id.list_ll);

        dialog = findViewById(R.id.photo_display_progress_bar);

        Intent intent = getIntent();
        try {
            fpath = intent.getStringExtra("fpath");
        } catch (Exception e) {
            System.out.println("catch block fpath");
        }
        searchedWord = intent.getStringExtra("searchTerm");

        if (!(fpath.equalsIgnoreCase("placeholder"))) {
            Bitmap bm = BitmapFactory.decodeFile(fpath);
            imageBytes = getBytesFromFile(bm);
            imageView.setImageBitmap(bm);
        } else {
            System.out.println("Else block");
            StorageReference placeholderRef = storageRef.child("public/Placeholder-food.jpg");
            Glide.with(this)
                    .using(new FirebaseImageLoader())
                    .load(placeholderRef)
                    .into(imageView);
        }


//        LinearLayouts - buttons
        L11 = findViewById(R.id.c1_linearLayout1);
        L11.setOnClickListener(this);

        L12 = findViewById(R.id.c1_linearLayout2);
        L12.setOnClickListener(this);

        L13 = findViewById(R.id.c1_linearLayout3);
        L13.setOnClickListener(this);

        L21 = findViewById(R.id.c2_linearLayout1);
        L21.setOnClickListener(this);

        L22 = findViewById(R.id.c2_linearLayout2);
        L22.setOnClickListener(this);

        L23 = findViewById(R.id.c2_linearLayout3);
        L23.setOnClickListener(this);

        L31 = findViewById(R.id.c3_linearLayout1);
        L31.setOnClickListener(this);

        L32 = findViewById(R.id.c3_linearLayout2);
        L32.setOnClickListener(this);

        L33 = findViewById(R.id.c3_linearLayout3);
        L33.setOnClickListener(this);


        //LinearLayouts - textviews
//        child one
        L11TV1 = findViewById(R.id.c1_1_tv_1);
        L11TV2 = findViewById(R.id.c1_1_tv_2);
        L12TV1 = findViewById(R.id.c1_2_tv_1);
        L12TV2 = findViewById(R.id.c1_2_tv_2);
        L13TV1 = findViewById(R.id.c1_3_tv_1);
        L13TV2 = findViewById(R.id.c1_3_tv_2);
//        child two
        L21TV1 = findViewById(R.id.c2_1_tv_1);
        L21TV2 = findViewById(R.id.c2_1_tv_2);
        L22TV1 = findViewById(R.id.c2_2_tv_1);
        L22TV2 = findViewById(R.id.c2_2_tv_2);
        L23TV1 = findViewById(R.id.c2_3_tv_1);
        L23TV2 = findViewById(R.id.c2_3_tv_2);
//        child three
        L31TV1 = findViewById(R.id.c3_1_tv_1);
        L31TV2 = findViewById(R.id.c3_1_tv_2);
        L32TV1 = findViewById(R.id.c3_2_tv_1);
        L32TV2 = findViewById(R.id.c3_2_tv_2);
        L33TV1 = findViewById(R.id.c3_3_tv_1);
        L33TV2 = findViewById(R.id.c3_3_tv_2);

//        Confirmation Screen Views
        includedConfLayout = findViewById(R.id.include_conf_items);
        confBottomBar = includedConfLayout.findViewById(R.id.conf_bottom_bar_linear);
        confItemScrollViewParent = includedConfLayout.findViewById(R.id.conf_item_vert_scroll_view);
        confTopBar = includedConfLayout.findViewById(R.id.conf_topbar);

        confItemName = includedConfLayout.findViewById(R.id.conf_item_name);


        confConfirmSelection = includedConfLayout.findViewById(R.id.conf_tick);
        confConfirmSelection.setOnClickListener(this);

        backArrow = includedConfLayout.findViewById(R.id.conf_back_arrow);
        backArrow.setOnClickListener(this);

        servingAmount = includedConfLayout.findViewById(R.id.conf_serving_amt_et);
        servingAmount.setText("1");
        servingAmountValue = parseServingAmount();

        servingAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                setServingAmountValue();
//                modifyLocalValues();
                setConfScreenTextViews();
            }
        });


//        Confirmation Screen Vals
        botCal = includedConfLayout.findViewById(R.id.bot_tv_cals_val);
        botCarb = includedConfLayout.findViewById(R.id.bot_tv_carbs_val);
        botFat = includedConfLayout.findViewById(R.id.bot_tv_fat_val);
        botProtein = includedConfLayout.findViewById(R.id.bot_tv_protein_val);
        calVal = includedConfLayout.findViewById(R.id.conf_calories_disp_value);
        totFatVal = includedConfLayout.findViewById(R.id.conf_totalfat_disp_value);
        satFatVal = includedConfLayout.findViewById(R.id.conf_satfat_disp_value);
        cholVal = includedConfLayout.findViewById(R.id.conf_chol_disp_value);
        carbVal = includedConfLayout.findViewById(R.id.conf_carbs_disp_value);
        fibVal = includedConfLayout.findViewById(R.id.conf_fiber_disp_value);
        sugVal = includedConfLayout.findViewById(R.id.conf_sugar_disp_value);
        protVal = includedConfLayout.findViewById(R.id.conf_protein_disp_value);
        potasVal = includedConfLayout.findViewById(R.id.conf_potassium_disp_value);

        selectedItemsListView = findViewById(R.id.selected_items_lv);


//        gestureDetector = new GestureDetector(getActivity())

//        TODO: remove
        addedExtrasToFirebase();


        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(170);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        selectedItemsListView.setMenuCreator(creator);

        selectedItemsListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        System.out.println("Delete item");
                        selectedList.remove(position);
                        updateAdapter();
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        extras_ll = findViewById(R.id.add_extras_ll);

        milkbtn = findViewById(R.id.add_milk_btn);
        milkbtn.setOnClickListener(this);
        sweetbtn = findViewById(R.id.add_sweet_btn);
        sweetbtn.setOnClickListener(this);

        honeyvalue = 0;
        sugarvalue = 0;
        sweetvalue = 0;

    }//end oncreate


    @Override
    protected void onStart() {
        super.onStart();
        hideConf();
        hideButtons();
        if (searchedWord != null) {
            getNutritionInstantSearch(searchedWord, 0);
            getNutritionInstantSearch(searchedWord, 1);
            getNutritionInstantSearch(searchedWord, 2);
            changeButtonText(searchedWord);
        } else {
            onImagePicked(imageBytes);
        }
    }

    /*
    * MISCELLANEOUS [START]
    */


    protected byte[] getBytesFromFile(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    private int parseServingAmount() {
        return Utility.parseIntSafely(servingAmount.getText().toString());
    }

    private void setServingAmountValue() {
        servingAmountValue = parseServingAmount();
    }



    /*
    * MISCELLANEOUS [END]
    */

    /*
    * FIREBASE [START]
    */
    public void sendDataToDataBase(final FoodItem item) {
        final String timeNow = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//        String hoursNow = new SimpleDateFormat("HH").format(new Date());
//        final int hoursNowInt = Integer.parseInt(hoursNow);
//        final String[] mealTypeArray = {"Breakfast", "Lunch", "Dinner", "Snack"};
//
//        System.out.println("Hours now int: " + hoursNowInt);

//        int check;
//        if (hoursNowInt > 6 && hoursNowInt < 12) {
//            check = 0;
//        } else if (hoursNowInt < 16) {
//            check = 1;
//        } else if (hoursNowInt < 18) {
//            check = 2;
//        } else {
//            check = 3;
//        }


        DatabaseReference child = dbRef.child("users").child(user.getUid()).child("log").child(timeNow).child(item.getFood_name());

        child.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()) {
                    System.out.println("onDataChange: IF");
                    System.out.println(dataSnapshot.getValue());
                    FoodItem tFood = (dataSnapshot.getValue(FoodItem.class));
                    dbRef.child("users").child(user.getUid()).child("log").child(timeNow).child(item.getFood_name()).child("servings").setValue(tFood.getServings() + servingAmountValue);
                } else {
                    System.out.println("onDataChange: ELSE");

                    dbRef.child("users").child(user.getUid()).child("log").child(timeNow).child(item.getFood_name()).setValue(item);

//                    dbRef.child("users").child(user.getUid()).child("log").child(timeNow).child(item.getFood_name()).child("servings").setValue(item.getServings());
//                    dbRef.child("users").child(user.getUid()).child("log").child(timeNow).child(item.getFood_name()).child("calories").setValue(item.getCalories());
//                    dbRef.child("users").child(user.getUid()).child("log").child(timeNow).child(item.getFood_name()).child("protein").setValue(item.getProtein());
//                    dbRef.child("users").child(user.getUid()).child("log").child(timeNow).child(item.getFood_name()).child("total_fat").setValue(item.getTotal_fat());
//                    dbRef.child("users").child(user.getUid()).child("log").child(timeNow).child(item.getFood_name()).child("saturated_fat").setValue(item.getSaturated_fat());
//                    dbRef.child("users").child(user.getUid()).child("log").child(timeNow).child(item.getFood_name()).child("cholesterol").setValue(item.getCholesterol());
//                    dbRef.child("users").child(user.getUid()).child("log").child(timeNow).child(item.getFood_name()).child("carbs").setValue(item.getCarbs());
//                    dbRef.child("users").child(user.getUid()).child("log").child(timeNow).child(item.getFood_name()).child("fiber").setValue(item.getFiber());
//                    dbRef.child("users").child(user.getUid()).child("log").child(timeNow).child(item.getFood_name()).child("serving_weight_grams").setValue(item.getServing_weight_grams());
//                    dbRef.child("users").child(user.getUid()).child("log").child(timeNow).child(item.getFood_name()).child("iron").setValue(item.getIron());
//                    dbRef.child("users").child(user.getUid()).child("log").child(timeNow).child(item.getFood_name()).child("sugar").setValue(item.getSugar());
//                    dbRef.child("users").child(user.getUid()).child("log").child(timeNow).child(item.getFood_name()).child("sodium").setValue(item.getSodium());
//                    dbRef.child("users").child(user.getUid()).child("log").child(timeNow).child(item.getFood_name()).child("calcium").setValue(item.getCalcium());
//                    dbRef.child("users").child(user.getUid()).child("log").child(timeNow).child(item.getFood_name()).child("vitamin_a").setValue(item.getVitamin_a());
//                    dbRef.child("users").child(user.getUid()).child("log").child(timeNow).child(item.getFood_name()).child("vitamin_b1").setValue(item.getVitamin_b1());
//                    dbRef.child("users").child(user.getUid()).child("log").child(timeNow).child(item.getFood_name()).child("vitamin_b2").setValue(item.getVitamin_b2());
//                    dbRef.child("users").child(user.getUid()).child("log").child(timeNow).child(item.getFood_name()).child("vitamin_b3").setValue(item.getVitamin_b3());
//                    dbRef.child("users").child(user.getUid()).child("log").child(timeNow).child(item.getFood_name()).child("vitamin_b5").setValue(item.getVitamin_b5());
//                    dbRef.child("users").child(user.getUid()).child("log").child(timeNow).child(item.getFood_name()).child("vitamin_b6").setValue(item.getVitamin_b6());
//                    dbRef.child("users").child(user.getUid()).child("log").child(timeNow).child(item.getFood_name()).child("vitamin_b9").setValue(item.getVitamin_b9());
//                    dbRef.child("users").child(user.getUid()).child("log").child(timeNow).child(item.getFood_name()).child("vitamin_b12").setValue(item.getVitamin_b12());
//                    dbRef.child("users").child(user.getUid()).child("log").child(timeNow).child(item.getFood_name()).child("vitamin_c").setValue(item.getVitamin_c());
//                    dbRef.child("users").child(user.getUid()).child("log").child(timeNow).child(item.getFood_name()).child("vitamin_d").setValue(item.getVitamin_d());
//                    dbRef.child("users").child(user.getUid()).child("log").child(timeNow).child(item.getFood_name()).child("vitamin_e").setValue(item.getVitamin_e());
//                    dbRef.child("users").child(user.getUid()).child("log").child(timeNow).child(item.getFood_name()).child("vitamin_k").setValue(item.getVitamin_k());

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        }
        Intent finished = new Intent(PhotoDisplayActivity.this, MainActivity.class);
        startActivity(finished);
        finish();
    }

    /*
    * FIREBASE [END]
    */


    /*
     * JSON RELATED FUNCTIONALITY [START]
     */
    private JSONObject buildJsonPostNutrientsBody() {
        JSONObject jsonObject = new JSONObject();
        String queryBody = "";
        try {
            for (int i = 0; i < searchInstantNamesArray1.size(); i++) {
                queryBody = queryBody + searchInstantNamesArray1.get(i) + " and ";
            }
            for (int i = 0; i < searchInstantNamesArray2.size(); i++) {
                queryBody = queryBody + searchInstantNamesArray2.get(i) + " and ";
            }
            for (int i = 0; i < searchInstantNamesArray3.size(); i++) {
                queryBody = queryBody + searchInstantNamesArray3.get(i) + " and ";
            }
            jsonObject.put("query", queryBody);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("buildJsonPostNutrientsBody", queryBody);
        return jsonObject;
    }

    //    probably dont need
    private void addedExtrasToFirebase() {
        JSONObject jsonObject = new JSONObject();
        String queryBody = "sugar and sweetener and honey and milk and 1% milk and 0% milk and soya milk and oat milk and almond milk and lactose free milk and water";
        try {
            jsonObject.put("query", queryBody);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("added extras firebase");
        extrasNutrients(jsonObject);
    }

    //    probably dont need
    private void extrasNutrients(JSONObject jsonObject) {
        System.out.println("Extrasnutrients start");
        String url = NUTRITION_NUTRIENTS_URL;
        String REQUEST_TAG = "Nutrients_POST";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("PostNutrients", response.toString());
                        extractExtrasData(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("x-app-key", "c030c944f416765d8674debb3322fc2d");
                headers.put("x-app-id", "7b43b860");
//                headers.put("x-remote-user-id", "0");
                return headers;
            }
        };
        NutritionSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest, REQUEST_TAG);
    }

    //    probably dont need
    private void extractExtrasData(JSONObject object) {
        ArrayList<FoodItem> extrasList = new ArrayList<>();
        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray = object.getJSONArray("foods");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < jsonArray.length(); i++) {

            try {

                JSONObject tempJSONObject = jsonArray.getJSONObject(i);
                FoodItem tempFoodItem = new FoodItem(
                        tempJSONObject.getString("food_name"),
                        tempJSONObject.getString("serving_weight_grams"),
                        tempJSONObject.getString("nf_calories"),
                        tempJSONObject.getString("nf_total_fat"),
                        tempJSONObject.getString("nf_saturated_fat"),
                        tempJSONObject.getString("nf_cholesterol"),
                        tempJSONObject.getString("nf_sodium"),
                        tempJSONObject.getString("nf_total_carbohydrate"),
                        tempJSONObject.getString("nf_dietary_fiber"),
                        tempJSONObject.getString("nf_sugars"),
                        tempJSONObject.getString("nf_protein"),
                        tempJSONObject.getString("nf_potassium")
                );


                JSONArray tempJArray = tempJSONObject.getJSONArray("full_nutrients");


                for (int j = 0; j < tempJArray.length(); j++) {
                    if (tempJArray.getJSONObject(j).getDouble("attr_id") == 303) {
                        tempFoodItem.setIron(tempJArray.getJSONObject(j).getDouble("value"));

                    } else if (tempJArray.getJSONObject(j).getDouble("attr_id") == 301) {
                        tempFoodItem.setCalcium(tempJArray.getJSONObject(j).getDouble("value"));

                    } else if (tempJArray.getJSONObject(j).getDouble("attr_id") == 320) {
                        tempFoodItem.setVitamin_a(tempJArray.getJSONObject(j).getDouble("value"));

                    } else if (tempJArray.getJSONObject(j).getDouble("attr_id") == 328) {
                        tempFoodItem.setVitamin_d(tempJArray.getJSONObject(j).getDouble("value"));

                    } else if (tempJArray.getJSONObject(j).getDouble("attr_id") == 401) {
                        tempFoodItem.setVitamin_c(tempJArray.getJSONObject(j).getDouble("value"));

                    } else if (tempJArray.getJSONObject(j).getDouble("attr_id") == 415) {
                        tempFoodItem.setVitamin_b6(tempJArray.getJSONObject(j).getDouble("value"));

                    } else if (tempJArray.getJSONObject(j).getDouble("attr_id") == 418) {
                        tempFoodItem.setVitamin_b12(tempJArray.getJSONObject(j).getDouble("value"));

                    } else if (tempJArray.getJSONObject(j).getDouble("attr_id") == 404) {
                        tempFoodItem.setVitamin_b1(tempJArray.getJSONObject(j).getDouble("value"));

                    } else if (tempJArray.getJSONObject(j).getDouble("attr_id") == 405) {
                        tempFoodItem.setVitamin_b2(tempJArray.getJSONObject(j).getDouble("value"));

                    } else if (tempJArray.getJSONObject(j).getDouble("attr_id") == 406) {
                        tempFoodItem.setVitamin_b3(tempJArray.getJSONObject(j).getDouble("value"));

                    } else if (tempJArray.getJSONObject(j).getDouble("attr_id") == 410) {
                        tempFoodItem.setVitamin_b5(tempJArray.getJSONObject(j).getDouble("value"));

                    } else if (tempJArray.getJSONObject(j).getDouble("attr_id") == 417) {
                        tempFoodItem.setVitamin_b9(tempJArray.getJSONObject(j).getDouble("value"));

                    } else if (tempJArray.getJSONObject(j).getDouble("attr_id") == 323) {
                        tempFoodItem.setVitamin_e(tempJArray.getJSONObject(j).getDouble("value"));

                    } else if (tempJArray.getJSONObject(j).getDouble("attr_id") == 430) {
                        tempFoodItem.setVitamin_k(tempJArray.getJSONObject(j).getDouble("value"));
                    }
                }
                extrasList.add(convertTo1Gram(tempFoodItem));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        sendExtrasToFB(extrasList);
    }

    //    probably dont need
    private void sendExtrasToFB(final ArrayList<FoodItem> list) {
        System.out.println("send extras to fb start");
        DatabaseReference ref = dbRef.child("common-items");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    System.out.println("send extras to fb if");


                } else {
                    System.out.println("send extras to fb else");

                    for (FoodItem item : list) {
                        System.out.println(item.getFood_name());
                        dbRef.child("common-items").child(item.getFood_name()).child("servings").setValue(item.getServings());
                        dbRef.child("common-items").child(item.getFood_name()).child("calories").setValue(item.getCalories());
                        dbRef.child("common-items").child(item.getFood_name()).child("protein").setValue(item.getProtein());
                        dbRef.child("common-items").child(item.getFood_name()).child("total_fat").setValue(item.getTotal_fat());
                        dbRef.child("common-items").child(item.getFood_name()).child("saturated_fat").setValue(item.getSaturated_fat());
                        dbRef.child("common-items").child(item.getFood_name()).child("cholesterol").setValue(item.getCholesterol());
                        dbRef.child("common-items").child(item.getFood_name()).child("carbs").setValue(item.getCarbs());
                        dbRef.child("common-items").child(item.getFood_name()).child("fiber").setValue(item.getFiber());
                        dbRef.child("common-items").child(item.getFood_name()).child("serving_weight_grams").setValue(item.getServing_weight_grams());
                        dbRef.child("common-items").child(item.getFood_name()).child("iron").setValue(item.getIron());
                        dbRef.child("common-items").child(item.getFood_name()).child("sugar").setValue(item.getSugar());
                        dbRef.child("common-items").child(item.getFood_name()).child("sodium").setValue(item.getSodium());
                        dbRef.child("common-items").child(item.getFood_name()).child("calcium").setValue(item.getCalcium());
                        dbRef.child("common-items").child(item.getFood_name()).child("vitamin_a").setValue(item.getVitamin_a());
                        dbRef.child("common-items").child(item.getFood_name()).child("vitamin_b1").setValue(item.getVitamin_b1());
                        dbRef.child("common-items").child(item.getFood_name()).child("vitamin_b2").setValue(item.getVitamin_b2());
                        dbRef.child("common-items").child(item.getFood_name()).child("vitamin_b3").setValue(item.getVitamin_b3());
                        dbRef.child("common-items").child(item.getFood_name()).child("vitamin_b5").setValue(item.getVitamin_b5());
                        dbRef.child("common-items").child(item.getFood_name()).child("vitamin_b6").setValue(item.getVitamin_b6());
                        dbRef.child("common-items").child(item.getFood_name()).child("vitamin_b9").setValue(item.getVitamin_b9());
                        dbRef.child("common-items").child(item.getFood_name()).child("vitamin_b12").setValue(item.getVitamin_b12());
                        dbRef.child("common-items").child(item.getFood_name()).child("vitamin_c").setValue(item.getVitamin_c());
                        dbRef.child("common-items").child(item.getFood_name()).child("vitamin_d").setValue(item.getVitamin_d());
                        dbRef.child("common-items").child(item.getFood_name()).child("vitamin_e").setValue(item.getVitamin_e());
                        dbRef.child("common-items").child(item.getFood_name()).child("vitamin_k").setValue(item.getVitamin_k());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    //    probably dont need
    public void extractPostNutrientsData(JSONObject objIn) {
        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray = objIn.getJSONArray("foods");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            try {

                JSONObject tempJSONObject = jsonArray.getJSONObject(i);
                FoodItem tempFoodItem = new FoodItem(
                        tempJSONObject.getString("food_name"),
                        tempJSONObject.getString("serving_weight_grams"),
                        tempJSONObject.getString("nf_calories"),
                        tempJSONObject.getString("nf_total_fat"),
                        tempJSONObject.getString("nf_saturated_fat"),
                        tempJSONObject.getString("nf_cholesterol"),
                        tempJSONObject.getString("nf_sodium"),
                        tempJSONObject.getString("nf_total_carbohydrate"),
                        tempJSONObject.getString("nf_dietary_fiber"),
                        tempJSONObject.getString("nf_sugars"),
                        tempJSONObject.getString("nf_protein"),
                        tempJSONObject.getString("nf_potassium")
                );


                JSONArray tempJArray = tempJSONObject.getJSONArray("full_nutrients");


                for (int j = 0; j < tempJArray.length(); j++) {
                    if (tempJArray.getJSONObject(j).getDouble("attr_id") == 303) {
                        tempFoodItem.setIron(tempJArray.getJSONObject(j).getDouble("value"));

                    } else if (tempJArray.getJSONObject(j).getDouble("attr_id") == 301) {
                        tempFoodItem.setCalcium(tempJArray.getJSONObject(j).getDouble("value"));

                    } else if (tempJArray.getJSONObject(j).getDouble("attr_id") == 320) {
                        tempFoodItem.setVitamin_a(tempJArray.getJSONObject(j).getDouble("value"));

                    } else if (tempJArray.getJSONObject(j).getDouble("attr_id") == 328) {
                        tempFoodItem.setVitamin_d(tempJArray.getJSONObject(j).getDouble("value"));

                    } else if (tempJArray.getJSONObject(j).getDouble("attr_id") == 401) {
                        tempFoodItem.setVitamin_c(tempJArray.getJSONObject(j).getDouble("value"));

                    } else if (tempJArray.getJSONObject(j).getDouble("attr_id") == 415) {
                        tempFoodItem.setVitamin_b6(tempJArray.getJSONObject(j).getDouble("value"));

                    } else if (tempJArray.getJSONObject(j).getDouble("attr_id") == 418) {
                        tempFoodItem.setVitamin_b12(tempJArray.getJSONObject(j).getDouble("value"));

                    } else if (tempJArray.getJSONObject(j).getDouble("attr_id") == 404) {
                        tempFoodItem.setVitamin_b1(tempJArray.getJSONObject(j).getDouble("value"));

                    } else if (tempJArray.getJSONObject(j).getDouble("attr_id") == 405) {
                        tempFoodItem.setVitamin_b2(tempJArray.getJSONObject(j).getDouble("value"));

                    } else if (tempJArray.getJSONObject(j).getDouble("attr_id") == 406) {
                        tempFoodItem.setVitamin_b3(tempJArray.getJSONObject(j).getDouble("value"));

                    } else if (tempJArray.getJSONObject(j).getDouble("attr_id") == 410) {
                        tempFoodItem.setVitamin_b5(tempJArray.getJSONObject(j).getDouble("value"));

                    } else if (tempJArray.getJSONObject(j).getDouble("attr_id") == 417) {
                        tempFoodItem.setVitamin_b9(tempJArray.getJSONObject(j).getDouble("value"));

                    } else if (tempJArray.getJSONObject(j).getDouble("attr_id") == 323) {
                        tempFoodItem.setVitamin_e(tempJArray.getJSONObject(j).getDouble("value"));

                    } else if (tempJArray.getJSONObject(j).getDouble("attr_id") == 430) {
                        tempFoodItem.setVitamin_k(tempJArray.getJSONObject(j).getDouble("value"));
                    }
                }

                foodItemsList.add(tempFoodItem);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    public void extractInstantData(JSONObject jsonObjectIn, int check) {
        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray = jsonObjectIn.getJSONArray("common");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 3/*jsonArray.length()*/; i++) {
            try {
                JSONObject tempJsonObject = jsonArray.getJSONObject(i);
                if (check == 0) {
                    searchInstantNamesArray1.add(tempJsonObject.getString("food_name"));
                    searchInstantServingUnitArray1.add(tempJsonObject.getString("serving_unit"));
                } else if (check == 1) {
                    searchInstantNamesArray2.add(tempJsonObject.getString("food_name"));
                    searchInstantServingUnitArray2.add(tempJsonObject.getString("serving_unit"));
                } else if (check == 2) {
                    searchInstantNamesArray3.add(tempJsonObject.getString("food_name"));
                    searchInstantServingUnitArray3.add(tempJsonObject.getString("serving_unit"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    /*
     * JSON RELATED FUNCTIONALITY [END]
     */


    /*
     * NETWORK STUFF [START]
     */
    @SuppressLint("StaticFieldLeak")
    private void onImagePicked(@NonNull final byte[] imageBytes) {

        new AsyncTask<Void, Void, ClarifaiResponse<List<ClarifaiOutput<Concept>>>>() {
            @Override
            protected ClarifaiResponse<List<ClarifaiOutput<Concept>>> doInBackground(Void... params) {
                // The default Clarifai model that identifies concepts in images
                final ConceptModel model = App.get().clarifaiClient().getDefaultModels().foodModel();

                // Use this model to predict, with the image that the user just selected as the input
                return model.predict()
                        .withInputs(ClarifaiInput.forImage(ClarifaiImage.of(imageBytes)))
                        .executeSync();
            }

            @Override
            protected void onPostExecute(ClarifaiResponse<List<ClarifaiOutput<Concept>>> response) {
//                setBusy(false);
                if (!response.isSuccessful()) {
                    showErrorSnackbar(R.string.error_while_contacting_api);
                    return;
                }
                final List<ClarifaiOutput<Concept>> predictions = response.get();
                if (predictions.isEmpty()) {
                    showErrorSnackbar(R.string.no_results_from_api);
                    return;
                }
                concepts = predictions.get(0).data();
                changeButtonText();
                getNutritionInstantSearch(concepts.get(0).name(), 0);
                getNutritionInstantSearch(concepts.get(1).name(), 1);
                getNutritionInstantSearch(concepts.get(2).name(), 2);
//                setTextResultsButtons();
            }

            private void showErrorSnackbar(@StringRes int errorString) {
                Snackbar.make(
                        findViewById(R.id.container_bot_nav),
                        errorString,
                        Snackbar.LENGTH_INDEFINITE
                ).show();
            }
        }.execute();
    }


    public void getNutritionInstantSearch(String queryValue, final int flag) {
        Log.i("InstantSearch", "Value:" + queryValue);
        String REQUEST_TAG = "Nutrition_GET";
        String completedGetString = NUTRITION_INSTANT_URL_PREFIX + queryValue + NUTRITION_INSTANT_URL_POSTFIX;

        if (queryValue != null) {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(completedGetString, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    extractInstantData(response, flag);
                    setTextResultsButtons(flag);

                    Log.i("Nutrition_INFO", response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                }
            }) {
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("x-app-key", "c030c944f416765d8674debb3322fc2d");
                    headers.put("x-app-id", "7b43b860");
                    return headers;
                }
            };
            NutritionSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest, REQUEST_TAG);
        }
    }


    public void postNutritionNutrients(JSONObject jsonObject) {
        String url = NUTRITION_NUTRIENTS_URL;
        String REQUEST_TAG = "Nutrients_POST";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("PostNutrients", response.toString());
                        extractPostNutrientsData(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("x-app-key", "c030c944f416765d8674debb3322fc2d");
                headers.put("x-app-id", "7b43b860");
//                headers.put("x-remote-user-id", "0");
                return headers;
            }
        };
        NutritionSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest, REQUEST_TAG);
    }

    /*
     * NETWORK STUFF [END]
     */


    /*
    *[START] Layout Modifiers - Show/Hide elements [START]
    */

    protected void hideButtons() {
        b1.setVisibility(GONE);
        b2.setVisibility(GONE);
        b3.setVisibility(GONE);
        noneOfThese.setVisibility(GONE);
    }

    protected void showButtons() {
        dialog.setVisibility(GONE);
        b1.setVisibility(VISIBLE);
        b2.setVisibility(VISIBLE);
        b3.setVisibility(VISIBLE);
        noneOfThese.setVisibility(VISIBLE);
    }

    public void changeButtonText() {
        showButtons();
        b1.setText(concepts.get(0).name());
        b2.setText(concepts.get(1).name());
        b3.setText(concepts.get(2).name());
    }

    private void changeButtonText(String searchTerm) {
        showButtons();
        b1.setText(searchTerm);
        b2.setText(searchTerm);
        b3.setText(searchTerm);
    }


    public void showConf() {

        includedConfLayout.setVisibility(VISIBLE);
        confBottomBar.setVisibility(VISIBLE);
        confItemScrollViewParent.setVisibility(VISIBLE);
        confTopBar.setVisibility(VISIBLE);
    }

    public void hideConf() {
        includedConfLayout.setVisibility(GONE);
        confBottomBar.setVisibility(GONE);
        confItemScrollViewParent.setVisibility(GONE);
        confTopBar.setVisibility(GONE);
    }

    private void hideAllForConf() {
        hideLLButtons();
        hideButtons();
        uploadButton.setVisibility(GONE);
        selectedItemsListView.setVisibility(GONE);
        imageView.setVisibility(GONE);
    }

    private void showAllAfterConf() {
        if (selectedList.size() >= 1) {
            uploadButton.setVisibility(VISIBLE);
        }
        hideConf();
        showButtons();
        imageView.setVisibility(VISIBLE);
        selectedItemsListView.setVisibility(VISIBLE);
    }

    public void hideLLButtons() {
        L11.setVisibility(GONE);
        L12.setVisibility(GONE);
        L13.setVisibility(GONE);
        L21.setVisibility(GONE);
        L22.setVisibility(GONE);
        L23.setVisibility(GONE);
        L31.setVisibility(GONE);
        L32.setVisibility(GONE);
        L33.setVisibility(GONE);
    }

    public void displayResultsButtons(int res) {
        if (res == 1) {
            hideLLButtons();
            L11.setVisibility(VISIBLE);
            L12.setVisibility(VISIBLE);
            L13.setVisibility(VISIBLE);
        } else if (res == 2) {
            hideLLButtons();
            L21.setVisibility(VISIBLE);
            L22.setVisibility(VISIBLE);
            L23.setVisibility(VISIBLE);
        } else if (res == 3) {
            hideLLButtons();
            L31.setVisibility(VISIBLE);
            L32.setVisibility(VISIBLE);
            L33.setVisibility(VISIBLE);
        }
    }

    public void goToConfirmationScreen(int val) {
        selectValue = val;
        hideAllForConf();
        showConf();
        setConfScreenLocalValues(val);
        setConfScreenTextViews();
    }

    /*
    * [END] Layout Modifiers - Show/Hide elements [END]
    */

    //    [START] Layout Modifiers - Edit Text / Images [START]

    public void setConfScreenLocalValues(int val) {
        itemNameLocal = (foodItemsList.get(val).getFood_name());
        calValLocal = (foodItemsList.get(val).getCalories());
        totFatValLocal = (foodItemsList.get(val).getTotal_fat());
        satFatValLocal = (foodItemsList.get(val).getSaturated_fat());
        cholValLocal = (foodItemsList.get(val).getCholesterol());
        carbValLocal = (foodItemsList.get(val).getCarbs());
        fibValLocal = (foodItemsList.get(val).getFiber());
        sugValLocal = (foodItemsList.get(val).getSugar());
        protValLocal = (foodItemsList.get(val).getProtein());
        potasValLocal = (foodItemsList.get(val).getPotassium());
        servingWeightGramsLocal = (foodItemsList.get(val).getServing_weight_grams());
    }

    private void addExtrasButtons() {

    }

    private void setConfScreenTextViews() {

        if (checkForHotDrink(itemNameLocal)) {
            extras_ll.setVisibility(VISIBLE);
        }

        confItemName.setText(itemNameLocal);
        botCal.setText(String.valueOf(roundSafely((calValLocal * servingAmountValue), 1)));
        botCarb.setText(String.valueOf(roundSafely((carbValLocal * servingAmountValue), 1)));
        botFat.setText(String.valueOf(roundSafely((totFatValLocal * servingAmountValue), 1)));
        botProtein.setText(String.valueOf(roundSafely((protValLocal * servingAmountValue), 1)));
        calVal.setText(String.valueOf(roundSafely((calValLocal * servingAmountValue), 1)));
        totFatVal.setText(String.valueOf(roundSafely((totFatValLocal * servingAmountValue), 1)));
        satFatVal.setText(String.valueOf(roundSafely((satFatValLocal * servingAmountValue), 1)));
        cholVal.setText(String.valueOf(roundSafely((cholValLocal * servingAmountValue), 1)));
        carbVal.setText(String.valueOf(roundSafely((carbValLocal * servingAmountValue), 1)));
        fibVal.setText(String.valueOf(roundSafely((fibValLocal * servingAmountValue), 1)));
        sugVal.setText(String.valueOf(roundSafely((sugValLocal * servingAmountValue), 1)));
        protVal.setText(String.valueOf(roundSafely((protValLocal * servingAmountValue), 1)));
        potasVal.setText(String.valueOf(roundSafely((potasValLocal * servingAmountValue), 1)));


    }

    private boolean checkForHotDrink(String itemName) {
        if (itemName.equalsIgnoreCase("coffee")) {
            return true;
        } else if (itemName.equalsIgnoreCase("tea")) {
            return true;
        } else if (itemName.equalsIgnoreCase("latte")) {
            return true;
        } else {
            return false;
        }

    }

    @SuppressLint("SetTextI18n")
    public void setTextResultsButtons(int val) {
        try {
            if (val == 0) {
                L11TV1.setText(searchInstantNamesArray1.get(0));
                L12TV1.setText(searchInstantNamesArray1.get(1));
                L13TV1.setText(searchInstantNamesArray1.get(2));

                L11TV2.setText("Serving Size: " + searchInstantServingUnitArray1.get(0));
                L12TV2.setText("Serving Size: " + searchInstantServingUnitArray1.get(1));
                L13TV2.setText("Serving Size: " + searchInstantServingUnitArray1.get(2));
            } else if (val == 1) {
                L21TV1.setText(searchInstantNamesArray2.get(0));
                L22TV1.setText(searchInstantNamesArray2.get(1));
                L23TV1.setText(searchInstantNamesArray2.get(2));

                L21TV2.setText("Serving Size: " + searchInstantServingUnitArray2.get(0));
                L22TV2.setText("Serving Size: " + searchInstantServingUnitArray2.get(1));
                L23TV2.setText("Serving Size: " + searchInstantServingUnitArray2.get(2));
            } else if (val == 2) {
                L31TV1.setText(searchInstantNamesArray3.get(0));
                L32TV1.setText(searchInstantNamesArray3.get(1));
                L33TV1.setText(searchInstantNamesArray3.get(2));

                L31TV2.setText("Serving Size: " + searchInstantServingUnitArray3.get(0));
                L32TV2.setText("Serving Size: " + searchInstantServingUnitArray3.get(1));
                L33TV2.setText("Serving Size: " + searchInstantServingUnitArray3.get(2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    *    [END] Layout Modifier - Edit Text / Images [END]
    */


    private void setParamsSelectList(int h) {
        ViewGroup.LayoutParams params = ll_selected_list.getLayoutParams();


        params.height = (int) convertPixelsToDp(h);
        ll_selected_list.setLayoutParams(params);
    }

    private void addToSelectedList() {
        FoodItem tempFood = foodItemsList.get(selectValue);
        tempFood.setServings(parseServingAmount());
        selectedList.add(tempFood);

        switch (selectedList.size()) {
            case 1:
                setParamsSelectList(list_size1);
                break;
            case 2:
                setParamsSelectList(list_size2);
                break;
            case 3:
                setParamsSelectList(list_size3);
                break;
            default:
        }
        updateAdapter();
        showAllAfterConf();
    }


    private void addToSelectedList(FoodItem item) {
//        FoodItem tempFood = foodItemsList.get(selectValue);
//        item.setServings(parseServingAmount());
        selectedList.add(item);

        switch (selectedList.size()) {
            case 1:
                setParamsSelectList(list_size1);
                break;
            case 2:
                setParamsSelectList(list_size2);
                break;
            case 3:
                setParamsSelectList(list_size3);
                break;
            default:
        }
        updateAdapter();
        showAllAfterConf();
    }

    private void launchAddSweetDialog() {
        final Dialog sweetdialog = new Dialog(this);
        sweetdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sweetdialog.setContentView(R.layout.extras_sweet);
        sugarpp = sweetdialog.findViewById(R.id.sugar_plus);
        sugarmm = sweetdialog.findViewById(R.id.sugar_minus);
        sweetenerpp = sweetdialog.findViewById(R.id.sweetener_plus);
        sweetenermm = sweetdialog.findViewById(R.id.sweetener_minus);
        honeypp = sweetdialog.findViewById(R.id.honey_plus);
        honeymm = sweetdialog.findViewById(R.id.honey_minus);
        sugartv = sweetdialog.findViewById(R.id.sugar_tv);
        honeytv = sweetdialog.findViewById(R.id.honey_tv);
        sweettv = sweetdialog.findViewById(R.id.sweetener_tv);

        sugartv.setText(String.valueOf(sugarvalue));
        honeytv.setText(String.valueOf(honeyvalue));
        sweettv.setText(String.valueOf(sweetvalue));

        sugarpp.setOnClickListener(this);
        sugarmm.setOnClickListener(this);
        sweetenerpp.setOnClickListener(this);
        sweetenermm.setOnClickListener(this);
        honeypp.setOnClickListener(this);
        honeymm.setOnClickListener(this);
        sweetcancel = sweetdialog.findViewById(R.id.sweet_cancel);
        sweetconf = sweetdialog.findViewById(R.id.sweet_done);
        sweetcancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sweetdialog.dismiss();
            }
        });

        sweetconf.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sweetdialog.dismiss();
                updateItemName("Sweet");
            }
        });
        sweetdialog.show();
    }

    private void updateItemName(String incoming) {
        String n = confItemName.getText().toString();
        confItemName.setText(n + ", with " + incoming);
    }

    private void launchAddMilkDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.extras_milk);
        confMilkBtn = dialog.findViewById(R.id.confirm_milk);
        confMilkBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otherbool) {
                    milkAmtML = parseIntSafely(dialogMilkET.getText().toString());
                }
                updateItemName(milkSelection);
                dialog.dismiss();
            }
        });
        dialogMilkET = dialog.findViewById(R.id.et_milk_size_other);
        dialogMilkTV = dialog.findViewById(R.id.tv_milk_size_other);
        dialog.show();
    }

    public void onMilkRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.rb_milk_1:
                if (checked)
                    milkSelection = "milk";
                break;
            case R.id.rb_milk_2:
                if (checked)
                    milkSelection = "1% milk";
                break;
            case R.id.rb_milk_3:
                if (checked)
                    milkSelection = "0% milk";
                break;
            case R.id.rb_milk_4:
                if (checked)
                    milkSelection = "soya milk";
                break;
            case R.id.rb_milk_5:
                if (checked)
                    milkSelection = "oat milk";
                break;
            case R.id.rb_milk_6:
                if (checked)
                    milkSelection = "almond milk";
                break;
            case R.id.rb_milk_7:
                if (checked)
                    milkSelection = "lactose free milk";
                break;
        }
    }

    private void showMilkDialogET() {
        dialogMilkET.setVisibility(VISIBLE);
        dialogMilkTV.setVisibility(VISIBLE);
    }

    private void hideMilkDialogET() {
        dialogMilkET.setVisibility(GONE);
        dialogMilkTV.setVisibility(GONE);
    }

    public void onMilkAmtRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.rb_milk_size_1:
                if (checked) {
                    milkAmtML = 12;
                    System.out.println(milkAmtML);
                    otherbool = false;
                    hideMilkDialogET();
                }
                break;
            case R.id.rb_milk_size_2:
                if (checked) {
                    milkAmtML = 30;
                    System.out.println(milkAmtML);
                    otherbool = false;
                    hideMilkDialogET();
                }
                break;
            case R.id.rb_milk_size_3:
                if (checked) {
                    milkAmtML = 12;
                    otherbool = false;
                    System.out.println(milkAmtML);

                    hideMilkDialogET();
                }
                break;
            case R.id.rb_milk_size_other:
                if (checked) {
                    showMilkDialogET();
                    otherbool = true;
                }
                break;
        }
    }

    private void updateAdapter() {
        adapterConfirmItemsList = new AdapterConfirmItemsList(this, 0, selectedList);
        selectedItemsListView.setAdapter(adapterConfirmItemsList);
    }

    private void afterTypedSearch(String searchTerm) {
        Intent intent = new Intent(this, PhotoDisplayActivity.class);
        intent.putExtra("fpath", fpath);
        intent.putExtra("searchTerm", searchTerm);
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
                Intent runaway = new Intent(PhotoDisplayActivity.this, MainActivity.class);
                startActivity(runaway);
            }
        });

        searchDialog.show();
    }

    private void setLocalValuesForExtras(ArrayList<FoodItem> list) {
//        TODO: FINISH THIS
    }

    /**
     * sugar, sweetener, honey, milkml, milkname
     */
    private void customizedDrink(final double sug, final double swtner, final double hny, final double milkml, final String milkname) {

        DatabaseReference extrasRef = dbRef.child("common-items");

        extrasRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ArrayList<FoodItem> tempList = new ArrayList<>();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        FoodItem tFood = ds.getValue(FoodItem.class);
                        tFood.setFood_name(ds.getKey());
                        tempList.add(tFood);
                    }
//                    setLocalValuesForExtras(tempList);
                    FoodItem temp = foodItemsList.get(selectValue);

                    for (FoodItem i : tempList) {
                        System.out.println("customizedDrink: ");
                        System.out.println(i);
                        if (i.getFood_name().equalsIgnoreCase("sugar") && sug != 0) {
//                            i.setServings(sug);
                            System.out.println("INSIDE SUGAR IF");
                            System.out.println(convertToXGrams(i, (sug * 5)));
                            temp = combineFoodItems(temp, convertToXGrams(i, (sug * 5)));
                            System.out.println(temp.getSugar());
                        }
                        if (i.getFood_name().equalsIgnoreCase("sweetener") && swtner != 0) {
//                            i.setServings(swtner);
                            temp = combineFoodItems(temp, convertToXGrams(i, (swtner * 5)));
                        }
                        if (i.getFood_name().equalsIgnoreCase("honey") && hny != 0) {
//                            i.setServings(hny);
                            System.out.println(convertToXGrams(i, (hny * 5)));
                            temp = combineFoodItems(temp, convertToXGrams(i, (hny * 5)));
                        }
                        if (i.getFood_name().equalsIgnoreCase(milkname) && milkml != 0) {
//                            i.setServings(1);
                            temp = combineFoodItems(temp, convertToXGrams(i, milkml));
                        }
                    }
                    dbRef.child("users").child(user.getUid()).child("user-items").child("your-" + temp.getFood_name()).setValue(temp);
                    customizedFood = temp;
                    addToSelectedList(customizedFood);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == b1.getId()) {
            //First concept
            if (buttonFirstClick) {
                postNutritionNutrients(buildJsonPostNutrientsBody());
//                sendDataToDataBase(1);
                buttonFirstClick = false;
            }
            displayResultsButtons(1);

        } else if (i == b2.getId()) {
            //Second concept
            if (buttonFirstClick) {
                postNutritionNutrients(buildJsonPostNutrientsBody());
                buttonFirstClick = false;
            }
            displayResultsButtons(2);


        } else if (i == b3.getId()) {
            //Third concept
            if (buttonFirstClick) {
                postNutritionNutrients(buildJsonPostNutrientsBody());
                buttonFirstClick = false;
            }
            displayResultsButtons(3);

        } else if (i == noneOfThese.getId()) {
            launchTypedSearchDialog();

        } else if (i == L11.getId()) {
            goToConfirmationScreen(0);

        } else if (i == L12.getId()) {
            goToConfirmationScreen(1);

        } else if (i == L13.getId()) {
            goToConfirmationScreen(2);

        } else if (i == L21.getId()) {
            goToConfirmationScreen(3);

        } else if (i == L22.getId()) {
            goToConfirmationScreen(4);

        } else if (i == L23.getId()) {
            goToConfirmationScreen(5);

        } else if (i == L31.getId()) {
            goToConfirmationScreen(6);

        } else if (i == L32.getId()) {
            goToConfirmationScreen(7);

        } else if (i == L33.getId()) {
            goToConfirmationScreen(8);

        } else if (i == backArrow.getId()) {
            showAllAfterConf();

        } else if (i == confConfirmSelection.getId()) {

            if (milkAmtML != 0 || sweetvalue != 0 || sugarvalue != 0 || honeyvalue != 0) {
                customizedDrink(sugarvalue, sweetvalue, honeyvalue, milkAmtML, milkSelection);
            } else {
                addToSelectedList();
            }
//            showAllAfterConf();
            milkAmtML = 0;
            sweetvalue = 0;
            sugarvalue = 0;
            honeyvalue = 0;

        } else if (i == uploadButton.getId()) {
            for (FoodItem item : selectedList) {
                sendDataToDataBase(item);
            }
        } else if (i == milkbtn.getId()) {
            launchAddMilkDialog();
        } else if (i == sweetbtn.getId()) {
            launchAddSweetDialog();
        } else if (i == sweetenermm.getId()) {
            if (!(sweetvalue < 1)) {
                sweetvalue--;
                sweettv.setText(String.valueOf(sweetvalue));
            }
        } else if (i == sweetenerpp.getId()) {
            sweetvalue++;
            sweettv.setText(String.valueOf(sweetvalue));
        } else if (i == sugarpp.getId()) {
            sugarvalue++;
            sugartv.setText(String.valueOf(sugarvalue));
        } else if (i == sugarmm.getId()) {
            if (!(sugarvalue < 1)) {
                sugarvalue--;
                sugartv.setText(String.valueOf(sugarvalue));
            }
        } else if (i == honeypp.getId()) {
            honeyvalue++;
            honeytv.setText(String.valueOf(honeyvalue));
        } else if (i == honeymm.getId()) {
            if (!(honeyvalue < 1)) {
                honeyvalue--;
                honeytv.setText(String.valueOf(honeyvalue));
            }
        } else if (i == backToMainArrow.getId() ) {
            Intent finished = new Intent(PhotoDisplayActivity.this, MainActivity.class);
            startActivity(finished);
            finish();
        }

    }//onclick end


}


















