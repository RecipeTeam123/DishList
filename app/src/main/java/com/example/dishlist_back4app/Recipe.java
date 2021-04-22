package com.example.dishlist_back4app;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.parceler.Parcel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@ParseClassName("Recipe")
public class Recipe extends ParseObject {
    public static final String KEY_RECIPE_NAME = "recipeName";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_INGREDIENTS = "ingredients";
    public static final String KEY_METHOD = "method";
    public static final String KEY_USER = "user";
    public static final String KEY_CREATED_KEY = "createdAt";
    public static final String KEY_LIKES = "likes";
    public static final String KEY_LIKED_USERS = "liked_users";
    public static final String KEY_VIEWS = "views";
    public static final String TAG = "Recipe;";

    public int getRecipeViews(){return getInt(KEY_VIEWS);}

    public void setRecipeViews(int views){put(KEY_VIEWS,views);}

    public List getLiked_Users(){
        return getList(KEY_LIKED_USERS);
    }

    public void setLikedUsers(List<String> list){ put(KEY_LIKED_USERS, list);}

    public boolean UserLiked(String user){
        List<String> list =new ArrayList<String>();
        list=getList(KEY_LIKED_USERS);
        for(int i=0;i<list.size();i++){
            if(user.matches(list.get(i))){
                return true;
            }
        }
        return false;
    }

    public void addLikedUser(String user){
        List<String> list =new ArrayList<String>();
        list.addAll(getList(KEY_LIKED_USERS));
        list.add(user);
        put(KEY_LIKED_USERS, list);
    }

    public void removeLikedUser(String user){
        List<String> list =new ArrayList<String>();
        list.addAll(getList(KEY_LIKED_USERS));
        for(int i=0;i<list.size();i++){
            if(user.matches(list.get(i))){
                list.remove(i);
                put(KEY_LIKED_USERS, list);
                return;
            }
        }
    }

    public int getRecipeLikes(){return getInt(KEY_LIKES);}

    public void setRecipeLikes(int likes){ put(KEY_LIKES,likes);}

    public String getRecipeName() {
        return getString(KEY_RECIPE_NAME);
    }

    public void setRecipeName(String recipeName) {
        put(KEY_RECIPE_NAME, recipeName);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile) {
        put(KEY_IMAGE, parseFile);
    }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public String getIngredients() {
        return getString(KEY_INGREDIENTS);
    }

    public void setIngredients(String ingredients) {
        put(KEY_INGREDIENTS, ingredients);
    }

    public String getMethod() {
        return getString(KEY_METHOD);
    }

    public void setMethod(String method) {
        put(KEY_METHOD, method);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }
}
