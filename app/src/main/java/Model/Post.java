package Model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("TodoItem")

public class Post extends ParseObject {
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_INGREDIENTS = "ingredients";
    public static final String KEY_RECIPENAME="recipeName";

    public String getIngredients(){
        return getString(KEY_INGREDIENTS);
    }

    public void setIngredients(String ingredients){
        put(KEY_INGREDIENTS,ingredients);
    }

    public String getRecipeName() {
        return getString(KEY_RECIPENAME);
    }

    public void setRecipeName(String name){
        put(KEY_RECIPENAME,name);
    }

    public String getDescription(){
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description){
        put(KEY_DESCRIPTION,description);
    }

    public ParseFile getImage(){
        return getParseFile(KEY_IMAGE);
    }
    public void setImage(ParseFile parseFile){
        put(KEY_IMAGE,parseFile);
    }
    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }
    public void setUser(ParseUser user){
        put(KEY_USER,user);
    }
}
