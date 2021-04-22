package com.example.dishlist_back4app.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import com.example.dishlist_back4app.R;
import com.example.dishlist_back4app.Recipe;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class AddRecipeFragment extends Fragment {

    private static final String TAG = "AddRecipeFragment";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    private EditText etDescription;
    private EditText etIngredients;
    private EditText etTitle;
    private EditText etMethod;
    private Button btnCaptureImage;
    private ImageView ivPostImage;
    private Button btnSubmit;
    private TextView tvDescription;
    private TextView tvIngredients;
    private TextView tvTitle;
    private TextView tvMethod;
    private File photoFile;
    public String photoFileName = "photo.jpg";
    private View view;
    private ProgressBar pb;

    public AddRecipeFragment() {
    } //empty constructor

    @Override
    public View onCreateView(LayoutInflater inflater, @NonNull ViewGroup container, @NonNull Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_addrecipe, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvDescription = view.findViewById(R.id.tvDescription);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvIngredients = view.findViewById(R.id.tvIngredients);
        tvMethod = view.findViewById(R.id.tvMethod);
        etMethod = view.findViewById(R.id.etMethod);
        etDescription = view.findViewById(R.id.etDescription);
        etTitle = view.findViewById(R.id.etTitle);
        etIngredients = view.findViewById(R.id.etIngredients);
        ivPostImage = view.findViewById(R.id.ivPicture);
        btnCaptureImage = view.findViewById(R.id.btnAddImage);
        btnSubmit = view.findViewById(R.id.btnSubmit);

        //declare the process bar
        pb = (ProgressBar) view.findViewById(R.id.pbPosting);

        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = etDescription.getText().toString();
                String ingredients = etIngredients.getText().toString();
                String title = etTitle.getText().toString();
                String method = etMethod.getText().toString();

                if (description.isEmpty() || ingredients.isEmpty() || title.isEmpty() || method.isEmpty()) {
                    Toast.makeText(getContext(), "Not enough info to submit", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (photoFile == null || ivPostImage.getDrawable() == null) {
                    Toast.makeText(getContext(), "An image is needed to submit!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //set process bar
                pb.setVisibility(ProgressBar.VISIBLE);

                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(title, ingredients, description, method, currentUser, photoFile);
            }
        });
    }

    private void savePost(String title, String ingredients, String description, String method, ParseUser currentUser, File photoFile) {
        Recipe post = new Recipe();
        post.setDescription(description);
        post.setIngredients(ingredients);
        post.setRecipeName(title);
        post.setMethod(method);
        post.setImage(new ParseFile(photoFile));
        post.setRecipeLikes(0);
        post.setRecipeViews(0);
        post.setLikedUsers(new ArrayList<>());
        post.setViewedUsers(new ArrayList<>());
        post.setUser(currentUser);
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(getContext(), "Error while saving!", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Post save was successful!");
                etTitle.setText("");
                etDescription.setText("");
                etIngredients.setText("");
                etMethod.setText("");
                ivPostImage.setImageResource(0);

                //if the post success saved, progressBar invisible.
                pb.setVisibility(ProgressBar.INVISIBLE);
            }
        });
    }

    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ivPostImage.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory");
        }
        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }
}
