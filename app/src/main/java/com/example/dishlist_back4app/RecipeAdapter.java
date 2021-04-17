package com.example.dishlist_back4app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import org.parceler.Parcel;
import org.parceler.Parcels;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private Context context;
    private List<Recipe> recipes;

    public RecipeAdapter(Context context, List<Recipe> recipes) {
        this.context = context;
        this.recipes= recipes;
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
        RelativeLayout container;
        private ImageView ivImage;
        private TextView tvRecipeName;
        private TextView tvRecipeDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivRecipePicture);
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
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, DetailActivity.class);

                    i.putExtra("recipe name", recipe.getRecipeName());
                    i.putExtra("recipe ingredients", recipe.getIngredients());
                    i.putExtra("recipe steps", recipe.getMethod());

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
