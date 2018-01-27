package com.adam.camerawithsaveapi24;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.File;

public class PhotoDisplayActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_display);


        String fpath;
        ImageView imageView = findViewById(R.id.photoDisplayView);
        Intent intent = getIntent();
        fpath = intent.getStringExtra("fpath");
        Bitmap bm = BitmapFactory.decodeFile(fpath);
        imageView.setImageBitmap(bm);
        File imageFile = new File(fpath);

    }
}
