package com.adam.camerawithsaveapi24.tools;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.adam.camerawithsaveapi24.R;

import clarifai2.api.ClarifaiBuilder;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

import java.util.concurrent.TimeUnit;

//Clarifai client builder
public class ClarifaiClient extends Application {

    private static ClarifaiClient INSTANCE;

    @NonNull
    public static ClarifaiClient get() {
        final ClarifaiClient instance = INSTANCE;
        if (instance == null) {
            throw new IllegalStateException("ClarifaiClient has not been created yet!");
        }
        return instance;
    }

    @Nullable
    private clarifai2.api.ClarifaiClient client;

    @Override
    public void onCreate() {
        super.onCreate();

        INSTANCE = this;
        client = new ClarifaiBuilder(getString(R.string.clarifai_api_key))
                // Optionally customize HTTP client via a custom OkHttp instance
                .client(new OkHttpClient.Builder()
                        .readTimeout(30, TimeUnit.SECONDS) // Increase timeout for poor mobile networks

                        // Log all incoming and outgoing data
                        // NOTE: You will not want to use the BODY log-level in production, as it will leak your API request details
                        // to the (publicly-viewable) Android log
                        .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                            @Override public void log(String logString) {
                                Timber.e(logString);
                            }
                        }).setLevel(HttpLoggingInterceptor.Level.BODY))
                        .build()
                )
                .buildSync(); // use build() instead to get a Future<ClarifaiClient>, if you don't want to block this thread

        // Initialize our logging
        Timber.plant(new Timber.DebugTree());
    }

    @NonNull
    public clarifai2.api.ClarifaiClient clarifaiClient() {
        final clarifai2.api.ClarifaiClient client = this.client;
        if (client == null) {
            throw new IllegalStateException("Cannot use Clarifai client before initialized");
        }
        return client;
    }
}

