package com.example.dishlist_back4app;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.parse.ParseFile;
public class DetailActivity extends AppCompatActivity {

    TextView tvDetailTitle;
    TextView tvDetailDescription;
    TextView tvDetailIngredients;
    TextView tvDetailMethod;
    ImageView ivDetailImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailDescription = findViewById(R.id.tvDetailDescription);
        tvDetailIngredients = findViewById(R.id.tvDetailIngredients);
        tvDetailMethod = findViewById(R.id.tvDetailMethod);
        ivDetailImage = findViewById(R.id.ivDetailImage);

        String recipeName = getIntent().getStringExtra("recipe name");
        String recipeIngredients = getIntent().getStringExtra("recipe ingredients");
        String recipeMethod = getIntent().getStringExtra("recipe method");
        String recipeDescription= getIntent().getStringExtra("recipe description");

        tvDetailTitle.setText(recipeName);
        tvDetailDescription.setText(recipeDescription);
        tvDetailIngredients.setText(recipeIngredients);
        tvDetailMethod.setText(recipeMethod);

        ParseFile imageF = getIntent().getParcelableExtra("recipe image");
        if (imageF != null) {
            Glide.with(this).load(imageF.getUrl()).into(ivDetailImage);
        }
    }
}
