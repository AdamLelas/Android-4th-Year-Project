package com.adam.camerawithsaveapi24;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;



/**
 * Created by Adam on 16/02/2018.
 */


public class NutritionSingleton extends Application {

    private static NutritionSingleton instance = null;
    private static Context context;
    private final String TAG = "NutritionSingleton";



    //Volley
    private RequestQueue requestQueue;

    public static synchronized NutritionSingleton getInstance(Context context) {
        if (null == instance) {
            instance = new NutritionSingleton(context);
        }
        return instance;
    }

    public static synchronized NutritionSingleton getInstance() {
        if (null == instance) {
            throw new IllegalStateException(NutritionSingleton.class.getSimpleName() +
                    " is not initalised, call getInstance(Context c) first");
        }
        return instance;
    }



    private NutritionSingleton(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public RequestQueue getRequestQueue(){
        if(requestQueue == null){
            getInstance(context.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(tag);
        getRequestQueue().add(req);
    }


    public void onCreate() {
        super.onCreate();
        NutritionSingleton.getInstance(this);
    }
/**
 * This method contacts Nutrition API and returns
 **/
//    public void getNutritionInstantSearch(String q) {
//        String REQUEST_TAG = "Nutrition_GET";
//
//        queryString = q;
//        if (queryString != null) {
//
//            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(completedQueryString, null, new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
//
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    VolleyLog.d(TAG, "Error: " + error.getMessage());
//                }
//            }) {
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    Map<String, String> headers = new HashMap<>();
//                    headers.put("x-app-key", "c030c944f416765d8674debb3322fc2d");
//                    headers.put("x-app-id", "7b43b860");
//                    return headers;
//                }
//            };
//            requestQueue.add(jsonObjectRequest);
//
//        }
//    }
}
