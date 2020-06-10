package com.example.whatsinmyfridge;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * This is the class used to represent the activity of all the recipe searches
 * where we can later pick one and display the information
 */
public class SearchResults extends AppCompatActivity {
    // The list of ingredients we received before
    ArrayList<IngredientItem> ingredients;
    // The recipes from which we can choose
    ArrayList<Recipe> recipes;
    // The adapter that holds the recipes
    RecipeAdapter recipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        // Setting the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Getting the list of ingredients from the Home Fragment
        ingredients = getIntent().getParcelableArrayListExtra(IngredientItem.class.getSimpleName());

        // Displaying the ingredients that have been passed in a textual format
        TextView searched_ingredients = findViewById(R.id.searched_ingredients);
        searched_ingredients.setText("The searched ingredients were:\n" + Arrays.toString(ingredients.toArray()) + "\nPress on a recipe if you want to see how it is made!");

        // Initiating the gridview which will hold these recipes and the corresponding adapter
        GridView gridView = (GridView) findViewById(R.id.search_results_grid);
        recipes = new ArrayList<>();
        recipeAdapter = new RecipeAdapter(this, R.layout.recipe_item, recipes);
        gridView.setAdapter(recipeAdapter);

        // Opening a communication with the database
        DB_Communicator db_communicator = DB_Communicator.getInstance(this);
        db_communicator.open();

        //Getting Titles
        ArrayList<String> title_lst = db_communicator.getData(0);
        //Getting Difficulties
        ArrayList<String> diff_lst = db_communicator.getData(2);
        //Getting Ingredients
        ArrayList<String> ing_lst = db_communicator.getData(3);
        //Getting URLs
        ArrayList<String> url_lst = db_communicator.getData(5);

        db_communicator.close();

        boolean seen_one = false;
        int index = 0;
        boolean seen_all = true;
        ArrayList<Integer> index_recipes = new ArrayList<>();

        // First we need to sort the ingredients so we can compare them in the same way as the ingredients are sorted
        // in the database. (The database contains the ingredients by an alphabetical order)
        // Normally, because there are so many ingredients a NoSQL database would make more sense to use
        Collections.sort(ingredients, new Comparator<IngredientItem>() {
            public int compare(IngredientItem s1, IngredientItem s2) {
                return s1.getTitle().compareToIgnoreCase(s2.getTitle());
            }
        });

        // For every ingredient_list passed check...
        for (String recipe : ing_lst) {
            // If you want to display what is actually happening uncomment this:
            // Log.d("Debug", "[" + recipe + "]" + " - " + Arrays.toString(ingredients.toArray()).replace(",", ";"));
            // ... if the list is similar to the lists we we have in our recipes from the database
            if (("[" + recipe + "]").equals(Arrays.toString(ingredients.toArray()).replace(",", ";"))) {
                seen_all = true;
                // if we see all of the ingredients within a recipe then we have to add its index to the index list
                index_recipes.add(index);
            } else {
                // Otherwise we need to get through all of the ingredients to see if
                // there is a recipe that only needs one of the present ingredients
                for (IngredientItem ing : ingredients)
                    if (("[" + recipe + "]").equals("[" + ing.toString() + "]"))
                        index_recipes.add(index);
                // Please Note: We obviously need a scalar multiplication for every item combination
                // As you might already think, this is not really computation performant, which is why
                // I have kept it simple for the moment being, until I find a good and efficient other option
            }
            // Goto next index
            index++;
        }

        // For all indices previously added to the list add the recipes to the recipe list for display
        for (Integer i : index_recipes)
            recipes.add(new Recipe(title_lst.get(i), diff_lst.get(i), url_lst.get(i)));

        // If the recipe list is empty then we obviously could not find any fitting recipe and in
        // this case we let the user know
        if (recipes.size() == 0) {
            TextView searched_ingredients_result = findViewById(R.id.searched_ingredients_result);
            searched_ingredients_result.setText("No Recipe matching your Ingredients could be found :(");
        }

        // The gridview gets an item click listener so we know which element has been clicked
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // For debuging reasons if you want to see the index and title uncomment this:
                // Log.d("Debug", String.valueOf(recipes.get(position).getTitle()) + " clicked");

                // Creating a new intent where we pass the title of the recipe clicked, as it is the
                // Primary Key within the Database and can be used to search more information
                Intent i = new Intent(SearchResults.this, DisplayedRecipeFromSearch.class);
                i.putExtra("RecipeName", recipes.get(position).getTitle());
                startActivity(i);
            }
        });
        // Notifying the adapter as we have added recipes
//      recipeAdapter.notifyDataSetChanged();

    }
}
