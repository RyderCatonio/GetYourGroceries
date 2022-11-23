package com.example.getyourgroceries.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.example.getyourgroceries.GlideApp;
import com.example.getyourgroceries.R;
import com.example.getyourgroceries.adapters.RecipeIngredientAdapter;
import com.example.getyourgroceries.control.RecipeDB;
import com.example.getyourgroceries.entity.Ingredient;
import com.example.getyourgroceries.entity.IngredientStorage;
import com.example.getyourgroceries.entity.Recipe;
import com.example.getyourgroceries.entity.RecipeStorage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RecipeViewFragment extends Fragment {

    private static final String TAG = "RecipeViewFrag";
    private Recipe viewRecipe;
    FirebaseStorage storage;
    ViewGroup containerView;
    StorageReference imageRef;
    ListView ingredientListView;
    private RecipeIngredientAdapter ingredientAdapter;

    public RecipeViewFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_recipe, container, false);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("View Recipe");
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);
        containerView= container;

        return view;
    }

    /**
     * The onViewCreated method will be called once the view has been created.
     *
     * @param view               The created view.
     * @param savedInstanceState The saved state.
     */
    @SuppressLint("SimpleDateFormat")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // Initialization.
        NestedScrollView viewIngredientLayout = requireActivity().findViewById(R.id.view_recipe_layout);
        viewIngredientLayout.setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO);


        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();

        if (getArguments() != null) {
            viewRecipe = RecipeStorage.getInstance().getRecipe(getArguments().getInt("viewRecipe"));
            actionBar.setTitle(viewRecipe.getName());
        }



        //Populate fields if its a view
        ImageView image = requireActivity().findViewById(R.id.image);
        TextView prepTime = requireActivity().findViewById(R.id.prepTimeTextField);
        TextView category = requireActivity().findViewById(R.id.categoryTextField);
        TextView servings = requireActivity().findViewById(R.id.servingsTextField);
        TextView commentsText = requireActivity().findViewById(R.id.commentsTextField);


        // Output all of the ingredients from Firebase.
        ingredientListView = requireActivity().findViewById(R.id.ingredientListView);
        // Set the values to the previous values.
        if (viewRecipe != null){
            int prep_hours = viewRecipe.getPrepTime() / 60;
            int prep_min = viewRecipe.getPrepTime() % 60;
            String prepTimeText = prep_hours + "h " + prep_min + "m";
            prepTime.setText(prepTimeText);

            String catStr = "Category: " + viewRecipe.getRecipeCategory();
            category.setText(catStr);

            String servingsStr = "Servings: " + viewRecipe.getNumOfServings();
            servings.setText(servingsStr);

            commentsText.setText(viewRecipe.getComment());

            ingredientAdapter = new RecipeIngredientAdapter(requireActivity().getBaseContext(), viewRecipe.getIngredientList());
            ingredientListView.setAdapter(ingredientAdapter);

            // get photo
            storage = FirebaseStorage.getInstance();
            try {
                imageRef = storage.getReference().child(viewRecipe.getPhoto());

                GlideApp.with(view)
                        .load(imageRef)
                        .into(image);
            } catch (IllegalArgumentException e) {
                image.setImageResource(R.drawable.placeholder);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.view_recipe_menu, menu);
    }

    /**
     * The onOptionsItemSelected method will go to the previous fragment when the back button is pressed.
     * @param item The item selected.
     * @return On success, true.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.editRecipeButton) {
            Bundle bundle = new Bundle();
            Recipe editRecipe = viewRecipe;
            bundle.putInt("editRecipe", getArguments().getInt("viewRecipe"));
            RecipeChangeHandlerFragment recipeChangeHandlerFragment = new RecipeChangeHandlerFragment();
            recipeChangeHandlerFragment.setArguments(bundle);
            requireActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out).replace(containerView.getId(), recipeChangeHandlerFragment, "EDIT_RECIPE").addToBackStack("EDIT_RECIPE").commit();
        } else {
            return requireActivity().getSupportFragmentManager().popBackStackImmediate();
        }

        return true;
    }

}