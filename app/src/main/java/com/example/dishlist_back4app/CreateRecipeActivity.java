package com.example.dishlist_back4app;

import android.os.Bundle;
import android.os.PersistableBundle;
import androidx.appcompat.app.AppCompatActivity;

public class CreateRecipeActivity extends AppCompatActivity {

    //This now need go to the AddRecipeFragment
    @Override
    public void onCreate( Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.fragment_addrecipe);
    }
}
