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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    private int counter = 0;
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

    private List<FoodItem> foodItemsList = new ArrayList<>();

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


    //fake JSONObject for test purposes
    private JSONObject fakeJson;

    //    private String queryString = null;
    private final String NUTRITION_INSTANT_URL_PREFIX = "https://trackapi.nutritionix.com/v2/search/instant?query=";
    private final String NUTRITION_INSTANT_URL_POSTFIX = "&branded=false";

    private final String NUTRITION_NUTRIENTS_URL_PREFIX = "https://trackapi.nutritionix.com/v2/natural/nutrients";
    private final String NUTRITION_NUTRIENTS_URL_POSTFIX = "";


    protected byte[] getBytesFromFile(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    public void sendDataToDataBase(int value) {
        String timeNow = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        String hoursNow = new SimpleDateFormat("HH").format(new Date());
        int hoursNowInt = Integer.parseInt(hoursNow);
        String[] mealTypeArray = {"Breakfast","Lunch","Dinner", "Snack" };
        int check;

        System.out.println(hoursNowInt);
        if(hoursNowInt > 6 && hoursNowInt < 12 ){
            check = 0;
        } else if (hoursNowInt < 16){
            check = 1;
        }else if (hoursNowInt < 18){
            check = 2;
        }else {
            check = 3;
        }

        dbRef.child("users").child(user.getUid()).child(timeNow).child("food").child(mealTypeArray[check]).child("1").child("calories").setValue(202);
        dbRef.child("users").child(user.getUid()).child(timeNow).child("food").child(mealTypeArray[check]).child("biscuit").child("carbs").setValue(333);
        dbRef.child("users").child(user.getUid()).child(timeNow).child("food").child(mealTypeArray[check]).child("biscuit").child("protein").setValue(400);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_display);

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

        fakeJson = createFakeJson();

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


    }

    @Override
    protected void onStart() {
        super.onStart();
        hideButtons();
        onImagePicked(imageBytes);
    }

    public void changeButtonText() {
        showButtons();
        b1.setText(concepts.get(0).name());
        b2.setText(concepts.get(1).name());
        b3.setText(concepts.get(2).name());
//        b4.setText(concepts.get(3).name());
//        b5.setText(concepts.get(4).name());
//        b6.setText(concepts.get(5).name());
    }

    protected void hideButtons() {
        b1.setVisibility(GONE);
        b2.setVisibility(GONE);
        b3.setVisibility(GONE);
//        b4.setVisibility(View.GONE);
//        b5.setVisibility(View.GONE);
//        b6.setVisibility(View.GONE);
        noneOfThese.setVisibility(GONE);
    }

    protected void showButtons() {
        dialog.setVisibility(GONE);
        b1.setVisibility(VISIBLE);
        b2.setVisibility(VISIBLE);
        b3.setVisibility(VISIBLE);
//        b4.setVisibility(View.VISIBLE);
//        b5.setVisibility(View.VISIBLE);
//        b6.setVisibility(View.VISIBLE);
        noneOfThese.setVisibility(VISIBLE);
    }


    public JSONObject createFakeJson() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("query", "apple and banana");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

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

    /**
     * This method contacts Nutrition API and gets response
     **/
    public void getNutritionInstantSearch(String queryValue, final int flag) {
        Log.i("GetNutritionButton_Pressed", "Value:" + queryValue);
        String REQUEST_TAG = "Nutrition_GET";
//        queryString = q;
        String completedGetString = NUTRITION_INSTANT_URL_PREFIX + queryValue + NUTRITION_INSTANT_URL_POSTFIX;

        if (queryValue != null) {

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(completedGetString, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    extractInstantData(response, flag);
                    setTextResultsButtons(flag);
                    counter++;
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

//    public void postNutritionNutrients(String queryValue) {
//        Log.i("POST_nutrients", "Value: " + queryValue);
//        String REQUEST_TAG = "Nutrition_Nutrients_POST";
//        String completedPostString = NUTRITION_NUTRIENTS_URL_PREFIX;
//
//        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, completedPostString,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.i("Nutrition_INFO", response.toString());
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
//            }
//        }) {
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//                headers.put("x-app-key", "c030c944f416765d8674debb3322fc2d");
//                headers.put("x-app-id", "7b43b860");
//                return headers;
//            }
//        };
//        NutritionSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest, REQUEST_TAG);
//    }


    public void postNutritionNutrients(JSONObject jsonObject) {
        String url = NUTRITION_NUTRIENTS_URL_PREFIX;
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

    //    TODO: Add code to change buttons
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

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == b1.getId()) {
            //First concept
            if (buttonFirstClick) {
                postNutritionNutrients(buildJsonPostNutrientsBody());
                sendDataToDataBase(1);
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
            //TODO: go to enter text mode4
        } else if (i == L11.getId()) {
            System.out.println(foodItemsList.get(0));
        } else if (i == L12.getId()) {
            System.out.println(foodItemsList.get(1));

        } else if (i == L13.getId()) {
            System.out.println(foodItemsList.get(3));

        } else if (i == L21.getId()) {

        } else if (i == L22.getId()) {

        } else if (i == L23.getId()) {

        } else if (i == L31.getId()) {

        } else if (i == L32.getId()) {

        } else if (i == L33.getId()) {

        }


    }
}
