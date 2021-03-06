package com.example.dishlist_back4app.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import androidx.appcompat.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.dishlist_back4app.R;
import com.example.dishlist_back4app.Recipe;
import com.example.dishlist_back4app.RecipeAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    public static final String TAG = "HomeFragment";
    private RecyclerView rvRecipe;
    protected RecipeAdapter adapter;
    protected List<Recipe> allRecipes;
    private SwipeRefreshLayout swipeContainer;
    private List<Recipe> localRecipes;

    public HomeFragment() {
        //empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvRecipe = view.findViewById(R.id.rvRecipes);
        allRecipes = new ArrayList<>();
        adapter = new RecipeAdapter(getContext(), allRecipes);

        // set the adapter on the recycler view
        rvRecipe.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvRecipe.setLayoutManager(new LinearLayoutManager(getContext()));

        swipeContainer = view.findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeColors(getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light));

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "fetching new data!");
                queryRecipes();
            }
        });
        queryRecipes();
    }

    protected void queryRecipes() {
        ParseQuery<Recipe> query = ParseQuery.getQuery(Recipe.class);
        query.include(Recipe.KEY_USER);
        //query.setLimit(20);

        //list by the created at
        query.addDescendingOrder(Recipe.KEY_CREATED_KEY);

        query.findInBackground(new FindCallback<Recipe>() {
            @Override
            public void done(List<Recipe> recipes, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting recipes", e);
                    return;
                }
                for (Recipe recipe : recipes) {
                    Log.i(TAG, "Recipe: " + recipe.getDescription() + ", username: " + recipe.getUser().getUsername());
                }
                adapter.clear();
                allRecipes.addAll(recipes);
                System.out.println("The special recipe " + allRecipes.size());
                adapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);

                //store the recipes in local for search. This is the copy of the original allRecipes.
                localRecipes = new ArrayList<>();
                localRecipes.addAll(allRecipes);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_toolbar, menu);
        MenuItem menuItem = menu.findItem(R.id.tiSearch);
        MenuItem menuItemFilterLike = menu.findItem(R.id.tiFilterLikes);
        MenuItem menuItemFilterView = menu.findItem(R.id.tiFilterViews);
        SearchView searchView = (SearchView) menuItem.getActionView();

        //change keyboard search icon inorder mix search.
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Log.i(TAG, "copy of the local list " + storeRecipesLocal.size());
                adapter.filterSearch(newText, localRecipes);
                return false;
            }
        });

        //filter icon
        menuItemFilterLike.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.i(TAG, "filter icon clicked");
                adapter.filterLike(localRecipes);
                return false;
            }
        });

        //filter icon
        menuItemFilterView.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.i(TAG, "filter icon clicked");
                adapter.filterViews(localRecipes);
                return false;
            }
        });
    }
}
