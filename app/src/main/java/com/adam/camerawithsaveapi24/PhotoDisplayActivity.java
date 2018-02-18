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
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import clarifai2.api.ClarifaiResponse;
import clarifai2.dto.input.ClarifaiImage;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.model.ConceptModel;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;

public class PhotoDisplayActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "PhotoDisplayActivity";
    private String fpath;
    private byte[] imageBytes;
    protected List<Concept> concepts = new ArrayList<>();
    private Button b1, b2, b3;
    private Button noneOfThese;
    private ImageView imageView;
    private ProgressBar dialog;

    //    private String queryString = null;
    private final String NUTRITION_URL_PREFIX = "https://trackapi.nutritionix.com/v2/search/instant?query=";
    private final String NUTRITION_URL_POSTFIX = "&branded=false";
    private String completedQueryString;


    protected byte[] getBytesFromFile(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_display);

        //Set up views findViewById
        imageView = findViewById(R.id.photoDisplayView);
        b1 = findViewById(R.id.food_select_button_one);
        b1.setOnClickListener(this);
        b2 = findViewById(R.id.food_select_button_two);
        b2.setOnClickListener(this);
        b3 = findViewById(R.id.food_select_button_three);
        b3.setOnClickListener(this);

        noneOfThese = findViewById(R.id.food_select_button_none);
        dialog = findViewById(R.id.photo_display_progress_bar);

        Intent intent = getIntent();
        fpath = intent.getStringExtra("fpath");
        Bitmap bm = BitmapFactory.decodeFile(fpath);
        imageBytes = getBytesFromFile(bm);
        imageView.setImageBitmap(bm);
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
        b1.setVisibility(View.GONE);
        b2.setVisibility(View.GONE);
        b3.setVisibility(View.GONE);
//        b4.setVisibility(View.GONE);
//        b5.setVisibility(View.GONE);
//        b6.setVisibility(View.GONE);
        noneOfThese.setVisibility(View.GONE);
    }

    protected void showButtons() {
        dialog.setVisibility(View.GONE);
        b1.setVisibility(View.VISIBLE);
        b2.setVisibility(View.VISIBLE);
        b3.setVisibility(View.VISIBLE);
//        b4.setVisibility(View.VISIBLE);
//        b5.setVisibility(View.VISIBLE);
//        b6.setVisibility(View.VISIBLE);
        noneOfThese.setVisibility(View.VISIBLE);
    }


    /**
     * This method contacts Nutrition API and gets response
     **/
    public void getNutrition(String queryValue) {
        Log.i("GetNutritionButton Pressed", "Value:" +queryValue);
        String REQUEST_TAG = "Nutrition_GET";
//        queryString = q;
        completedQueryString = NUTRITION_URL_PREFIX + queryValue + NUTRITION_URL_POSTFIX;

        if (queryValue != null) {

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(completedQueryString, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
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

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == b1.getId()) {
            //First concept
            getNutrition(concepts.get(0).name());
        } else if (i == b2.getId()) {
            //Second concept
            getNutrition(concepts.get(1).name());
        } else if (i == b3.getId()) {
            //Third concept
            getNutrition(concepts.get(2).name());
        }
    }
}
