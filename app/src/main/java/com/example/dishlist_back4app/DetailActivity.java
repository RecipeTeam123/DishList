package com.example.dishlist_back4app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ParseUser;

public class DetailActivity extends AppCompatActivity {

    TextView tvDetailTitle;
    TextView tvDetailDescription;
    TextView tvDetailIngredients;
    TextView tvDetailMethod;
    ImageView ivDetailImage;
    ImageView ivDetailHeart;
    TextView tvDetailLikes;
    TextView tvDetailViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailDescription = findViewById(R.id.tvDetailDescription);
        tvDetailIngredients = findViewById(R.id.tvDetailIngredients);
        tvDetailMethod = findViewById(R.id.tvDetailMethod);
        ivDetailImage = findViewById(R.id.ivDetailImage);
        ivDetailHeart = findViewById(R.id.ivDetailHeart);
        tvDetailLikes = findViewById(R.id.tvDetailLikes);
        tvDetailViews = findViewById(R.id.tvDetailViews);

        Recipe recipe = (Recipe)getIntent().getParcelableExtra("recipe");

        tvDetailTitle.setText(recipe.getRecipeName());
        tvDetailDescription.setText(recipe.getDescription());
        tvDetailIngredients.setText(recipe.getIngredients());
        tvDetailMethod.setText(recipe.getMethod());
        tvDetailViews.setText(recipe.getRecipeViews()+" views");
        tvDetailLikes.setText(String.valueOf(recipe.getRecipeLikes()));

        ParseUser user = ParseUser.getCurrentUser();
        //sets the heart to be either empty or filled depending
        //on if the user liked the post in the past
        if(recipe.UserLiked(user.getObjectId())){
            //fills the heart and sets it at active
            ivDetailHeart.setImageResource(R.drawable.filled_like);
        }else{
            //empties the heart and sets it as inactive
            ivDetailHeart.setImageResource(R.drawable.recipe_like_heart);
        }

        ivDetailHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if the user already liked the post
                //if they have, then unlike the post
                if(recipe.UserLiked(user.getObjectId())){
                    recipe.removeLikedUser(user.getObjectId());
                    ivDetailHeart.setImageResource(R.drawable.recipe_like_heart);
                    recipe.setRecipeLikes(recipe.getRecipeLikes()-1);
                    tvDetailLikes.setText(String.valueOf(recipe.getRecipeLikes()));
                    recipe.saveInBackground();

                }else{    //else, like the post
                    recipe.addLikedUser(user.getObjectId());
                    ivDetailHeart.setImageResource(R.drawable.filled_like);
                    recipe.setRecipeLikes(recipe.getRecipeLikes()+1);
                    tvDetailLikes.setText(String.valueOf(recipe.getRecipeLikes()));
                    recipe.saveInBackground();
                }
            }
        });
        ParseFile imageF = recipe.getImage();
        if (imageF != null) {
            Glide.with(this).load(imageF.getUrl()).into(ivDetailImage);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();    }
}
