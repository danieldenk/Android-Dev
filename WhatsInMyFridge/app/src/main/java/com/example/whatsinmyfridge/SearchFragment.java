package com.example.whatsinmyfridge;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


/**
 * This is a subclass of {@link Fragment}
 * It provides functionality to add search for a specific recipe and display its key characteristics
 */
public class SearchFragment extends Fragment {
    // The recipe is displayed by ...
    // Title
    String title;
    // Description
    String desc;
    // Difficulty
    String diff;
    // Ingredients
    String ing;
    // If it is favorite (1) or not (0)
    int fav;
    // an IMG URL
    String url;
    // Further we need the input text that is being searched for
    String input_txt;
    // and a connection to the database
    DB_Communicator db_communicator;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        // Input text by user
        AutoCompleteTextView ac_text = (AutoCompleteTextView) v.findViewById(R.id.auto_complete_input_search);

        // getting  the input text string
        input_txt = ac_text.getText().toString();

        // database connection is open
        db_communicator = DB_Communicator.getInstance(getContext());
        db_communicator.open();

        // Getting all recipe titles
        ArrayList<String> titles = db_communicator.getData(0);

        // Setting the adapter on the ingredient list so we get choices once we type sth into
        // the auto_complete field
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, titles.toArray(new String[0]));

        // Setting the adapter to the auto_complete field
        ac_text.setAdapter(adapter);

        // Adding a Textchange Listener for a cool appearing effect once a recipe was entered
        // makes it more dynamic
        ac_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (db_communicator.getData(0).contains(String.valueOf(s))) {

                    // Getting the corresponding recipe values of a recipe of name = s from the database
                    ArrayList<String> recipe_flat = db_communicator.searchRecipe(String.valueOf(s));

                    // To display the data that is being processed uncomment the following:
                   /* Log.d("Debug",String.valueOf(s));
                   Log.d("Debug",String.valueOf(recipe_flat.get(1)));
                   Log.d("Debug",String.valueOf(recipe_flat.get(2)));
                   Log.d("Debug",String.valueOf(recipe_flat.get(3)));
                   Log.d("Debug",String.valueOf(recipe_flat.get(4)));
                   Log.d("Debug",String.valueOf(recipe_flat.get(5))); */

                    // Initiation of the Variables to set
                    title = String.valueOf(s);
                    desc = recipe_flat.get(1);
                    diff = recipe_flat.get(2);
                    ing = recipe_flat.get(3);
                    fav = Integer.parseInt(recipe_flat.get(4));;
                    url = recipe_flat.get(5);

                    // Setting the display variables to their text values
                    TextView titleView = (TextView) getView().findViewById(R.id.search_name);
                    titleView.setText("Title:\n" + String.valueOf(s));

                    TextView descView = (TextView) getView().findViewById(R.id.search_desc);
                    descView.setText("Description:\n" + desc);

                    TextView diffView = (TextView) getView().findViewById(R.id.search_diff);
                    diffView.setText("Difficulty:\n" + diff);

                    TextView ingView = (TextView) getView().findViewById(R.id.search_ing);
                    ingView.setText("Ingredients needed:\n" + ing);

                    // Loading the image from the url
                    ImageView img = (ImageView) getView().findViewById(R.id.search_img);
                    Glide.with(getContext()).load(url).dontAnimate().into(img);


                    // If the recipe is a favorite then display the full heart
                    // else the heart with the border and no filling
                    ImageView fav = (ImageView) getView().findViewById(R.id.search_heart);
                    if (!db_communicator.searchRecipe(title).isEmpty()) {
                        if (db_communicator.searchRecipe(title).get(4).equals("1"))
                            Glide.with(getView()).load(R.drawable.ic_favorite_yellow_24dp).into(fav);
                        else
                            Glide.with(getView()).load(R.drawable.ic_favorite_border_black_24dp).into(fav);
                    }

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Default is that the Recipe has not been found
        TextView name = (TextView) v.findViewById(R.id.search_name);
        name.setText("RECIPE NOT FOUND");

        // If the favorite heart is being clicked then ...
        ImageView fav = (ImageView) v.findViewById(R.id.search_heart);
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView fav = v.findViewById(R.id.search_heart);
                if (!db_communicator.searchRecipe(title).isEmpty()) {
                    // Remove them recipe from the list of favorites if it has been a favorite before
                    if (db_communicator.searchRecipe(title).get(4).equals("1")) {
                        // For debugging purposes
                        Log.d("Debug", "Removed from favorite");
                        Toast.makeText(getContext(), title + " was removed from favorite", Toast.LENGTH_SHORT).show();
                        db_communicator.setFavorite(title, 0);
                        fav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    } else {
                        // Otherwise set the recipe to favorite if it has not been set to it before
                        // For debugging purposes
                        Log.d("Debug", "Set to favorite");
                        db_communicator.setFavorite(title, 1);
                        Toast.makeText(getContext(), title + " was added to favorite", Toast.LENGTH_SHORT).show();
                        fav.setImageResource(R.drawable.ic_favorite_yellow_24dp);
                    }
                }

            }
        });
        // Last but not least close the connection to the database
        //db_communicator.close();
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
