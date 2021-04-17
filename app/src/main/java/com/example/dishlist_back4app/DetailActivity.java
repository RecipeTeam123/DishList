package com.example.dishlist_back4app;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import org.parceler.Parcels;

public class DetailActivity extends AppCompatActivity {

    TextView dishID;
    TextView list;
    TextView steps;
    //ImageView idFood;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        dishID = findViewById(R.id.dishID);
        list = findViewById(R.id.list);
        steps = findViewById(R.id.steps);



        String recipeName = getIntent().getStringExtra("recipe name");
        String recipeIngredients = getIntent().getStringExtra("recipe ingredients");
        String recipeSteps = getIntent().getStringExtra("recipe steps");



        dishID.setText(recipeName);
        list.setText(recipeIngredients);
        steps.setText(recipeSteps);



    }
}