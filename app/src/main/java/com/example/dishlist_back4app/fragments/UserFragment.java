package com.example.dishlist_back4app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.dishlist_back4app.CurrentUserRecipeAdapter;
import com.example.dishlist_back4app.LoginActivity;
import com.example.dishlist_back4app.R;
import com.example.dishlist_back4app.Recipe;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment {

    private RecyclerView rvRecipe;
    private SwipeRefreshLayout swipeContainer;
    protected CurrentUserRecipeAdapter adapter;
    protected List<Recipe> currentUserRecipe;
    private TextView tvCurrentUser;
    public static final String TAG = "UserFragment";

    public UserFragment() {
        //empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvCurrentUser = view.findViewById(R.id.tvUserName);
        rvRecipe = view.findViewById(R.id.rvRecipesInUser);
        currentUserRecipe = new ArrayList<>();
        adapter = new CurrentUserRecipeAdapter(getContext(), currentUserRecipe);

        rvRecipe.setAdapter(adapter);

        // First param is number of columns and second param is orientation i.e Vertical or Horizontal
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        // Attach the layout manager to the recycler view
        rvRecipe.setLayoutManager(gridLayoutManager);
        tvCurrentUser.setText("Hi, " + ParseUser.getCurrentUser().getUsername());
        swipeContainer = view.findViewById(R.id.swipeContainerUser);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "fetching this user!");
                queryPosts();
            }
        });
        queryPosts();
    }

    protected void queryPosts() {
        ParseQuery<Recipe> query = ParseQuery.getQuery(Recipe.class);
        query.include(Recipe.KEY_USER);
        query.addDescendingOrder(Recipe.KEY_CREATED_KEY);

        query.findInBackground(new FindCallback<Recipe>() {
            @Override
            public void done(List<Recipe> recipes, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }

                adapter.clear();

                for (Recipe post : recipes) {
                    ParseUser user = post.getParseUser(Recipe.KEY_USER);
                    String userName = user.getUsername();

                    //get the current user data
                    if(userName.equals(ParseUser.getCurrentUser().getUsername())) {
                        currentUserRecipe.add(post);
                    }
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }

                adapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }
        });
    }

    /**
     * For the user tool bar sign out
     **/
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_user_toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.btnLogOut) {
            ParseUser.logOut();
            ParseUser currentUser = ParseUser.getCurrentUser();
            Intent i = new Intent(getActivity(), LoginActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
