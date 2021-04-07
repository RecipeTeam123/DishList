package com.example.dishlist_back4app;

import android.app.Application;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //register the recipe class in the parse
        ParseObject.registerSubclass(Recipe.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("zo7VzkiVVjEYTZAxTHiy71wDYh6L1pYjspRAjcu0")
                .clientKey("a94Ir9hoaSmolpPy3KxmVpLQIyaLM1EjZlui3Sg7")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
