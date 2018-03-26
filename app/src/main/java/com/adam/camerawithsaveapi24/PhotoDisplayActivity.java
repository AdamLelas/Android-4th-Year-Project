package com.adam.camerawithsaveapi24;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;


import com.adam.camerawithsaveapi24.Tools.Utility;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

public class PhotoDisplayActivity extends AppCompatActivity implements OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private FirebaseUser user;

    private static final String TAG = "PhotoDisplayActivity";
    private final int ARRAYSIZE = 5;

    private String fpath;
    private byte[] imageBytes;
    protected List<Concept> concepts = new ArrayList<>();
    private Button b1, b2, b3;
    private boolean buttonFirstClick = true;
    private Button noneOfThese;
    private ImageView imageView;
    private ProgressBar dialog;

//    Confirmation Screen Views [START]

    private LinearLayout confTopBar;
    private LinearLayout confBottomBar;
    private ScrollView confItemScrollViewParent;
    private View includedConfLayout;
    private ImageButton backArrow;
    private ImageButton confConfirmSelection;
    private EditText servingAmount;
    private int servingAmountValue;

//    Confirmation Screen TextViews
    private TextView confItemName, botCal, botCarb, botFat, botProtein, calVal, totFatVal, satFatVal, cholVal, carbVal, fibVal, sugVal, protVal, potasVal;


//    Confirmation Screen Views [END]


    private List<FoodItem> foodItemsList = new ArrayList<>();

    //    maybe make an array instead
    private String itemNameLocal;
    private double calValLocal;
    private double totFatValLocal;
    private double satFatValLocal;
    private double cholValLocal;
    private double carbValLocal;
    private double fibValLocal;
    private double sugValLocal;
    private double protValLocal;
    private double potasValLocal;


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


    //    private String queryString = null;
    private final String NUTRITION_INSTANT_URL_PREFIX = "https://trackapi.nutritionix.com/v2/search/instant?query=";
    private final String NUTRITION_INSTANT_URL_POSTFIX = "&branded=false";

    private final String NUTRITION_NUTRIENTS_URL = "https://trackapi.nutritionix.com/v2/natural/nutrients";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_display);


        //Firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();
        user = mAuth.getCurrentUser();

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


        dialog = findViewById(R.id.photo_display_progress_bar);

        Intent intent = getIntent();
        fpath = intent.getStringExtra("fpath");
        Bitmap bm = BitmapFactory.decodeFile(fpath);
        imageBytes = getBytesFromFile(bm);
        imageView.setImageBitmap(bm);

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
    }


    @Override
    protected void onStart() {
        super.onStart();
        hideConf();
        hideButtons();
        onImagePicked(imageBytes);
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
    public void sendDataToDataBase(/*int value*/) {
        final String timeNow = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        String hoursNow = new SimpleDateFormat("HH").format(new Date());
        final int hoursNowInt = Integer.parseInt(hoursNow);
        final String[] mealTypeArray = {"Breakfast", "Lunch", "Dinner", "Snack"};

        System.out.println("Hours now int: " + hoursNowInt);

        int check;
        if (hoursNowInt > 6 && hoursNowInt < 12) {
            check = 0;
        } else if (hoursNowInt < 16) {
            check = 1;
        } else if (hoursNowInt < 18) {
            check = 2;
        } else {
            check = 3;
        }

                                                                                        /*.child(mealTypeArray[check]).child(itemNameLocal)*/
        DatabaseReference child = dbRef.child("users").child(user.getUid()).child("log").child(timeNow).child("food").child(itemNameLocal);

        child.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    System.out.println("onDataChange: IF");
                    System.out.println(dataSnapshot.getValue());
                    FoodItem tFood = (dataSnapshot.getValue(FoodItem.class));
                    dbRef.child("users").child(user.getUid()).child("log").child(timeNow).child("food").child(itemNameLocal).child("servings").setValue(tFood.getServings()+servingAmountValue);

                } else {
                    System.out.println("onDataChange: ELSE");

                                                                                        /*.child(mealTypeArray[check])*/
                                                                                        /*.child(mealTypeArray[check])*/
                                                                                        /*.child(mealTypeArray[check])*/
                                                                                        /*.child(mealTypeArray[check])*/
                                                                                        /*.child(mealTypeArray[check])*/
                                                                                        /*.child(mealTypeArray[check])*/
                                                                                        /*.child(mealTypeArray[check])*/
                                                                                        /*.child(mealTypeArray[check])*/
                    dbRef.child("users").child(user.getUid()).child("log").child(timeNow).child("food").child(itemNameLocal).child("servings").setValue(servingAmountValue);
                    dbRef.child("users").child(user.getUid()).child("log").child(timeNow).child("food").child(itemNameLocal).child("calories").setValue(calValLocal);
                    dbRef.child("users").child(user.getUid()).child("log").child(timeNow).child("food").child(itemNameLocal).child("protein").setValue(protValLocal);
                    dbRef.child("users").child(user.getUid()).child("log").child(timeNow).child("food").child(itemNameLocal).child("total_fat").setValue(totFatValLocal);
                    dbRef.child("users").child(user.getUid()).child("log").child(timeNow).child("food").child(itemNameLocal).child("saturated_fat").setValue(satFatValLocal);
                    dbRef.child("users").child(user.getUid()).child("log").child(timeNow).child("food").child(itemNameLocal).child("cholesterol").setValue(cholValLocal);
                    dbRef.child("users").child(user.getUid()).child("log").child(timeNow).child("food").child(itemNameLocal).child("carbs").setValue(carbValLocal);
                    dbRef.child("users").child(user.getUid()).child("log").child(timeNow).child("food").child(itemNameLocal).child("fiber").setValue(fibValLocal);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
        Log.i("GetNutritionButton_Pressed", "Value:" + queryValue);
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

    public void showConf() {
        confBottomBar.setVisibility(VISIBLE);
        confItemScrollViewParent.setVisibility(VISIBLE);
        confTopBar.setVisibility(VISIBLE);
    }

    public void hideConf() {
        confBottomBar.setVisibility(GONE);
        confItemScrollViewParent.setVisibility(GONE);
        confTopBar.setVisibility(GONE);
    }

    private void hideAllForConf() {
        hideLLButtons();
        hideButtons();
        imageView.setVisibility(GONE);
    }

    private void showAllAfterConf() {
        hideConf();
        showButtons();
        imageView.setVisibility(VISIBLE);
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
        hideAllForConf();
        showConf();
        setConfScreenLocalValues(val);
        setConfScreenTextViews();
//        TODO: Set all the values in here
    }

    /*
    * [END] Layout Modifiers - Show/Hide elements [END]
    */

    //    [START] Layout Modifiers - Edit Text / Images [START] Q

    public void setConfScreenLocalValues(int val) {
        itemNameLocal = (foodItemsList.get(val).getFood_name());
        calValLocal = (foodItemsList.get(val).getCalories());
        totFatValLocal = (foodItemsList.get(val).getTotal_fat());
        satFatValLocal = (foodItemsList.get(val).getSaturated_fat());
        cholValLocal = (foodItemsList.get(val).getCholesterol());
        carbValLocal = (foodItemsList.get(val).getCarbs());
        fibValLocal = (foodItemsList.get(val).getFiber());
        sugValLocal = (foodItemsList.get(val).getSugars());
        protValLocal = (foodItemsList.get(val).getProtein());
        potasValLocal = (foodItemsList.get(val).getPotassium());
    }

    private void setConfScreenTextViews() {

//      TODO: format values to 1 decimal place before setting text
        confItemName.setText(itemNameLocal);
        botCal.setText(String.valueOf(calValLocal * servingAmountValue));
        botCarb.setText(String.valueOf(carbValLocal * servingAmountValue));
        botFat.setText(String.valueOf(totFatValLocal * servingAmountValue));
        botProtein.setText(String.valueOf(protValLocal * servingAmountValue));
        calVal.setText(String.valueOf(calValLocal * servingAmountValue));
        totFatVal.setText(String.valueOf(totFatValLocal * servingAmountValue));
        satFatVal.setText(String.valueOf(satFatValLocal * servingAmountValue));
        cholVal.setText(String.valueOf(cholValLocal * servingAmountValue));
        carbVal.setText(String.valueOf(carbValLocal * servingAmountValue));
        fibVal.setText(String.valueOf(fibValLocal * servingAmountValue));
        sugVal.setText(String.valueOf(sugValLocal * servingAmountValue));
        protVal.setText(String.valueOf(protValLocal * servingAmountValue));
        potasVal.setText(String.valueOf(potasValLocal * servingAmountValue));


    }

    @SuppressLint("SetTextI18n")
    public void setTextResultsButtons(int val) {
        if (val == 0) {
            L11TV1.setText(searchInstantNamesArray1.get(0));
            L12TV1.setText(searchInstantNamesArray1.get(1));
            L13TV1.setText(searchInstantNamesArray1.get(2));

            L11TV2.setText("Serving Size:" + searchInstantServingUnitArray1.get(0));
            L12TV2.setText("Serving Size:" + searchInstantServingUnitArray1.get(1));
            L13TV2.setText("Serving Size:" + searchInstantServingUnitArray1.get(2));
        } else if (val == 1) {
            L21TV1.setText(searchInstantNamesArray2.get(0));
            L22TV1.setText(searchInstantNamesArray2.get(1));
            L23TV1.setText(searchInstantNamesArray2.get(2));

            L21TV2.setText("Serving Size:" + searchInstantServingUnitArray2.get(0));
            L22TV2.setText("Serving Size:" + searchInstantServingUnitArray2.get(1));
            L23TV2.setText("Serving Size:" + searchInstantServingUnitArray2.get(2));
        } else if (val == 2) {
            L31TV1.setText(searchInstantNamesArray3.get(0));
            L32TV1.setText(searchInstantNamesArray3.get(1));
            L33TV1.setText(searchInstantNamesArray3.get(2));

            L31TV2.setText("Serving Size:" + searchInstantServingUnitArray3.get(0));
            L32TV2.setText("Serving Size:" + searchInstantServingUnitArray3.get(1));
            L33TV2.setText("Serving Size:" + searchInstantServingUnitArray3.get(2));
        }
    }

    /*
    *    [END] Layout Modifier - Edit Text / Images [END]
    */

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
            //TODO: go to enter text mode
            Intent intent = new Intent(this, BottomNavActivity.class);
            startActivity(intent);
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
//            TODO: DO THE THING, SEND THE THING TO FIREBASE
            sendDataToDataBase();


        }


    }
}
