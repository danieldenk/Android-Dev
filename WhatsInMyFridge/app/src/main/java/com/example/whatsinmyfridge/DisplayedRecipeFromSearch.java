package com.example.whatsinmyfridge;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This is the class for all of the functionality once we have pressed on a recipe
 * based on the previous recipe search and now we want to see more details about the recipe
 * we have clicked at
 */
public class DisplayedRecipeFromSearch extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setting the layout activity
        setContentView(R.layout.activity_displayed_recipe_from_search);

        // Setting the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Opening up a new communication with the database
        DB_Communicator db_communicator = DB_Communicator.getInstance(this);
        db_communicator.open();

        // Receiving the recipe that matches out recipe name, which has been passed from the
        // activity before. Then we are searching for it within the database to get all the
        // corresponding data tuples.
        final ArrayList<String> flat_recipe = db_communicator.searchRecipe(getIntent().getExtras().getString("RecipeName"));
        db_communicator.close();

        // This is the TextView that is should contain the title of the recipe
        TextView title = findViewById(R.id.displayed_recipe_title);
        // ... therefore it is being set to the title
        title.setText(flat_recipe.get(0));

        // This is the TextView that is should contain the description + instruction of the recipe
        TextView desc = findViewById(R.id.displayed_recipe_desc);
        // ... therefore it is being set to the description + instruction
        desc.setText(flat_recipe.get(1));

        // This is the TextView that is should show the difficulty level of the recipe
        TextView diff = findViewById(R.id.displayed_recipe_diff);
        // ... therefore it is being set to the difficulty
        diff.setText(flat_recipe.get(2));

        // This is the TextView that is should show the ingredients of the recipe
        TextView ing = findViewById(R.id.displayed_recipe_Ing);
        // ... therefore it is being set to the ingredients
        ing.setText(flat_recipe.get(3));

        // This is the ImageView that is should display if the recipe is a favorite of the user
        ImageView fav = findViewById(R.id.displayed_recipe_fav);
        // If it is then we are going to display the full heart
        // Otherwise the user will see just the border of the heart
        if (flat_recipe.get(4).equals("1"))
            fav.setImageResource(R.drawable.ic_favorite_yellow_24dp);
        else
            fav.setImageResource(R.drawable.ic_favorite_border_black_24dp);

        // If the user clicks on the favorites icon described before...
        fav.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // ... we are going to open a new connection to the database
                ImageView fav = findViewById(R.id.displayed_recipe_fav);
                DB_Communicator db_com = DB_Communicator.getInstance(DisplayedRecipeFromSearch.this);
                db_com.open();

                // If the recipe with the title exists, it is not really needed as
                // we can be sure at this point that the clicked item is present in the database
                // I am leaving it in, because it does not harm to have an additional check that things
                // are running smoothly as they are intended to do
                if (!db_com.searchRecipe(flat_recipe.get(0)).isEmpty()) {
                    // We have to check whether the recipe has already been saved as a favorite
                    // In this case we are going to remove it and reset the fav image to the one with the border
                    if (db_com.searchRecipe(flat_recipe.get(0)).get(4).equals("1")) {
                        Log.d("Debug", "Removed from favorite");
                        Toast.makeText(DisplayedRecipeFromSearch.this, flat_recipe.get(0) + " was removed from favorite", Toast.LENGTH_SHORT).show();
                        db_com.setFavorite(flat_recipe.get(0), 0);
                        fav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                        // Otherwise it means that the user wants to add it as a favorite, which means we have to
                        // tell the database to set it as a favorite and then we change the image to the full heart
                    } else {
                        Log.d("Debug", "Set to favorite");
                        db_com.setFavorite(flat_recipe.get(0), 1);
                        Toast.makeText(DisplayedRecipeFromSearch.this, flat_recipe.get(0) + " was added to favorite", Toast.LENGTH_SHORT).show();
                        fav.setImageResource(R.drawable.ic_favorite_yellow_24dp);
                    }
                }
            }
        });

        // For memory efficiency and because I don't have any space left to save images on my phone
        // the recipes contain an URL image that is being loaded by Glide, instead of saving images locally
        ImageView img = findViewById(R.id.displayed_recipe_img);
        Glide.with(this).load(flat_recipe.get(5)).dontAnimate().into(img);


        //////////////////////// RETROFIT PART ///////////////////////////////////////
        //                                                                          //
        //      THIS IS BEING USED TO DISPLAY MORE INFORMATION FROM WIKIPEDIA       //
        //                                                                          //
        //////////////////////////////////////////////////////////////////////////////

        /*
        https://en.wikipedia.org/w/api.php
        ?action=opensearch
        &search=Spaghetti          # Search term
        &limit=1                   # only one URL is being pulled
        &namespace=0               # we are only interested in articles
        &format=json               # the result should be returned as a json file
         */

        // Initializing the Retrofit Builder with a gson factory and the base url from the wikipedia API
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://en.wikipedia.org/w/")
                .build();

        // "Initializing" the interface preciously created to perform retrofit
        IRetrofitAPI retrofit_api = retrofit.create(IRetrofitAPI.class);

        // Getting the needed Information from Wikipedia
        getWikipediaSnippet(retrofit_api, flat_recipe);
        getWikipediaURL(retrofit_api, flat_recipe);

        // Creating an animation for the favorite heart
        YoYo.with(Techniques.Pulse).delay(3000).duration(1000).repeat(-1).playOn(findViewById(R.id.displayed_recipe_fav));
    }

    // FIRST METHOD FOR RETROFIT: USED TO GET TEXT SNIPPET WITH ADDITIONAL INFO
    public void getWikipediaSnippet(IRetrofitAPI retrofit_api, ArrayList<String> flat_recipe) {
        // Getting a short description from Wikipedia based on the recipe title
        Call<JsonElement> first_call = retrofit_api.getEntry(flat_recipe.get(0));

        first_call.enqueue(new Callback<JsonElement>() {

            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                // The TextView which should contain the additional info
                TextView add_info = findViewById(R.id.displayed_recipe_addInfo);

                // If the established connection via retrofit has not been successful
                // The failed attempt is being displayed in the console, otherwise it does not appear
                if (!response.isSuccessful())
                    Log.d("Debug", "not valid http request");

                // Here we are getting the snippet containing a short introductory text by wikipedia
                JsonObject snippet = response.body().getAsJsonObject().get("query").getAsJsonObject();
                // This array is a part of the passed json which is why we need to take it separately
                JsonArray jsonArray = snippet.getAsJsonArray("search");
                // Based on the array we can now convert the snippet part we are interested at into a String
                String snippet_html = jsonArray.get(0).getAsJsonObject().get("snippet").toString();
                // Now the only thing left is to clean the snippet from the HTML Brackets and perform a cut on
                // the last sentence
                snippet_html = snippet_html.substring(0, snippet_html.lastIndexOf(".") + 1).replaceAll("\\<.*?\\>|\"", "");
                // Once this is done we can add the parsed text to our application
                if (snippet_html.equals(""))
                    add_info.setText("Wikipedia says:\n" + "nothing found");
                else
                    add_info.setText("Wikipedia says:\n" + snippet_html);
            }

            // In case something has f*cked up then we get a display of the error message
            // The tag is being set to Debug for a better debugging, because we can walk through the
            // process steps
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("Debug", t.getMessage());
                Log.d("Debug", call.request().url().toString());
            }
        });
    }

    // SECOND RETROFIT METHOD USED TO GET THE RECIPE URL
    public void getWikipediaURL(IRetrofitAPI retrofit_api, ArrayList<String> flat_recipe) {

        // This is the second call to the wikipedia api, but we are calling a different part of the
        // API.
        Call<JsonElement> second_call = retrofit_api.getURL(flat_recipe.get(0));

        second_call.enqueue(new Callback<JsonElement>() {
            // If a connection could not be established then we are going to see a
            // notification within the Logcat console
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                TextView readMore = findViewById(R.id.displayed_recipe_readMore);
                if (!response.isSuccessful())
                    Log.d("Debug", "not valid http request");

                // Otherwise we now have to get the JSON response
                // NOTE: Because this result is totally different than the JSON Object we received
                // in the query before, we now have to pick a different approach to get to the data.
                JsonArray jsonArray = response.body().getAsJsonArray();

                // Log just for checkup
                // Log.d("Debug", "Link: " + jsonArray.get(3).toString().replaceAll("\\[|\\]|\"", ""));

                // We get rid of all the unnecessary items of the String and show the link in the app
                String txt = jsonArray.get(3).toString().replaceAll("\\[|\\]|\"", "");
                if (txt.equals(""))
                    readMore.setText("Read more:\n" + "nothing found");
                else
                    readMore.setText("Read more:\n" + jsonArray.get(3).toString().replaceAll("\\[|\\]|\"", ""));


            }

            // If f*cked up then see Logcat
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("Debug", t.getMessage());
                Log.d("Debug", call.request().url().toString());
            }
        });
    }
}