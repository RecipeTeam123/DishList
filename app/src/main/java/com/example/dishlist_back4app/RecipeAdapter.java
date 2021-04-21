package com.example.dishlist_back4app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ParseUser;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private Context context;
    private List<Recipe> recipes;

    public RecipeAdapter(Context context, List<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.ViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.bind(recipe);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivImage;
        private TextView tvRecipeName;
        private TextView tvRecipeDescription;
        private TextView tvLikes;
        private ImageView ivHeart;
        RelativeLayout container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivRecipePicture);
            ivHeart = itemView.findViewById(R.id.ivHeart);

            //resize the image height size, assign 400 now.
            ivImage.getLayoutParams().height = 400;
            tvRecipeName = itemView.findViewById(R.id.tvRecipeName);
            tvRecipeDescription = itemView.findViewById(R.id.tvRecipeDescription);
            tvLikes = itemView.findViewById(R.id.tvLikes);
            container = itemView.findViewById(R.id.container);
        }

        public void bind(Recipe recipe) {
            //Bind the recipe data to the view elements
            tvRecipeName.setText(recipe.getRecipeName());
            tvRecipeDescription.setText(recipe.getDescription());
            tvLikes.setText(String.valueOf(recipe.getRecipeLikes()));

            ParseFile image = recipe.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivImage);
            }

            ParseUser user = ParseUser.getCurrentUser();
            //sets the heart to be either empty or filled depending
            //on if the user liked the post in the past
            if(recipe.UserLiked(user.getObjectId())){
                //fills the heart and sets it at active
                ivHeart.setImageResource(R.drawable.filled_like);
            }else{
                //empties the heart and sets it as inactive
                ivHeart.setImageResource(R.drawable.recipe_like_heart);
            }

            ivHeart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //check if the user already liked the post
                    //if they have, then unlike the post
                    if(recipe.UserLiked(user.getObjectId())){
                        recipe.removeLikedUser(user.getObjectId());
                        ivHeart.setImageResource(R.drawable.recipe_like_heart);
                        recipe.setRecipeLikes(recipe.getRecipeLikes()-1);
                        recipe.saveInBackground();
                        notifyDataSetChanged();
                    }else{    //else, like the post
                        recipe.addLikedUser(user.getObjectId());
                        ivHeart.setImageResource(R.drawable.filled_like);
                        recipe.setRecipeLikes(recipe.getRecipeLikes()+1);
                        recipe.saveInBackground();
                        notifyDataSetChanged();
                    }
                }
            });

            //click the home container to the detail stream
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, DetailActivity.class);

                    i.putExtra("recipe name", recipe.getRecipeName());
                    i.putExtra("recipe ingredients", recipe.getIngredients());
                    i.putExtra("recipe method", recipe.getMethod());
                    i.putExtra("recipe description", recipe.getDescription());
                    i.putExtra("recipe image", recipe.getImage());
                    i.putExtra("recipe likes",recipe.getRecipeLikes());

                    context.startActivity(i);
                }
            });
        }
    }

    public void clear() {
        recipes.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Recipe> list) {
        recipes.addAll(list);
        notifyDataSetChanged();
    }

    public void filterSearch(String text, List<Recipe> listPassed) {
        try {
            recipes.clear();
            if (text.isEmpty()) {
                recipes.addAll(listPassed);
            } else {
                text = text.toLowerCase();
                for (Recipe recipe : listPassed) {
                    if (recipe.getRecipeName().toLowerCase().contains(text) || recipe.getDescription().toLowerCase().contains(text)) {
                        recipes.add(recipe);
                    }
                }
            }
        } catch (java.util.ConcurrentModificationException exception) {
            //exception
        }
        notifyDataSetChanged();
    }

    public void filterLike(List<Recipe> listPassed) {
        recipes.clear();
        Collections.sort(listPassed, new Comparator<Recipe>() {
            @Override
            public int compare(Recipe o1, Recipe o2) {
                return o2.getRecipeLikes() - o1.getRecipeLikes();
            }
        });
        recipes.addAll(listPassed);
        notifyDataSetChanged();
    }
}
