package com.example.projectui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class PopUpWindowImageView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_window_image_view);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .6));

        Intent intent = getIntent();

        byte[] pic = intent.getByteArrayExtra("typeOfFishPictureByte");

        ImageView imageViewTypeOfFish = (ImageView) getWindow().getDecorView().findViewById(R.id.imageViewPopUpWindow);

        if (Objects.nonNull(pic)) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(pic, 0, pic.length);
            imageViewTypeOfFish.setImageBitmap(bitmap);
        }

        imageViewTypeOfFish.setOnClickListener(event -> {

        });
    }
}