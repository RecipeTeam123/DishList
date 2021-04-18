package com.example.dishlist_back4app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import java.util.List;

public class CurrentUserRecipeAdapter extends RecyclerView.Adapter<CurrentUserRecipeAdapter.ViewHolder> {

    private Context context;
    private List<Recipe> recipes;

    public CurrentUserRecipeAdapter(Context context, List<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recipe_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrentUserRecipeAdapter.ViewHolder holder, int position) {
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
        LinearLayout userContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivRecipePictureUser);
            ivImage.getLayoutParams().height = 400;
            tvRecipeName = itemView.findViewById(R.id.tvRecipeNameUser);
            userContainer = itemView.findViewById(R.id.userContainer);
        }

        public void bind(Recipe recipe) {
            //Bind the recipe data to the view elements
            tvRecipeName.setText(recipe.getRecipeName());

            ParseFile image = recipe.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivImage);
            }

            //click the home container to the detail stream
            userContainer.setOnClickListener(new View.OnClickListener() {
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
}
