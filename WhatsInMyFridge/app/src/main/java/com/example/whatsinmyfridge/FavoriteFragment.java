package com.example.whatsinmyfridge;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;

/**
 * This is a subclass of {@link Fragment}
 * It provides functionality to display recipes that have been set to favorites
 */
public class FavoriteFragment extends Fragment {
    // The view
    View v;
    // The gridview displaying the recipes
    GridView gridView;
    // The recipes we want to display
    ArrayList<Recipe> recipes;
    // The adapter for our gridview
    RecipeAdapter recipeAdapter;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_favorite, container, false);
        // Getting the grid object
        gridView = (GridView) v.findViewById(R.id.grid);
        // Initializing the recipe list
        recipes = new ArrayList<>();
        // Initializing the adapter with the list
        recipeAdapter = new RecipeAdapter(getContext(), R.layout.recipe_item, recipes);
        // Setting the adapter
        gridView.setAdapter(recipeAdapter);

        // Opening a database connection
        DB_Communicator db_communicator = DB_Communicator.getInstance(getContext());
        db_communicator.open();

        // Getting Title
        ArrayList<String> title_lst = db_communicator.getFavoritesData(0);
        //Getting URLs
        ArrayList<String> url_lst = db_communicator.getFavoritesData(5);
        //Getting Difficulty
        ArrayList<String> diff_lst = db_communicator.getFavoritesData(2);

        // Adding the recipes that are relevant into the array list
        for (int j = 0; j < db_communicator.getFavoritesData(0).size(); j++)
            recipes.add(new Recipe(title_lst.get(j), diff_lst.get(j), url_lst.get(j)));

        // Closing the database connection and notifying the adapter to reload our data
        db_communicator.close();
        recipeAdapter.notifyDataSetChanged();

        // Creating an animation for the grid
        YoYo.with(Techniques.FadeIn).repeatMode(1).playOn(v.findViewById(R.id.grid));

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}