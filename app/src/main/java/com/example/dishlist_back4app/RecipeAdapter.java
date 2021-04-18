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

import org.parceler.Parcels;

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
        RelativeLayout container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivRecipePicture);

            //resize the image height size, assign 400 now.
            ivImage.getLayoutParams().height = 400;
            tvRecipeName = itemView.findViewById(R.id.tvRecipeName);
            tvRecipeDescription = itemView.findViewById(R.id.tvRecipeDescription);
            container = itemView.findViewById(R.id.container);
        }

        public void bind(Recipe recipe) {
            //Bind the recipe data to the view elements
            tvRecipeName.setText(recipe.getRecipeName());
            tvRecipeDescription.setText(recipe.getDescription());

            ParseFile image = recipe.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivImage);
            }

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

    public void filter(String text, List<Recipe> listPassed) {
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
}
