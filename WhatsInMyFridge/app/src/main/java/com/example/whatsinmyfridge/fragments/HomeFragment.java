package com.example.whatsinmyfridge.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.whatsinmyfridge.R;
import com.example.whatsinmyfridge.search_mapping.SearchResults;
import com.example.whatsinmyfridge.database.DB_Communicator;
import com.example.whatsinmyfridge.ingredients.IngredientAdapter;
import com.example.whatsinmyfridge.ingredients.IngredientItem;

import java.util.ArrayList;
import java.util.HashMap;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.LandingAnimator;


/**
 * This is a subclass of {@link Fragment}
 * It provides functionality to add ingredients and perform a search for recipes
 */
public class HomeFragment extends Fragment implements IngredientAdapter.IngredientInfoListener {
    // a list of all of the ingredients we allow to exist
    public HashMap<String, ArrayList<String>> all_ingredients = new HashMap<>();
    // and the view
    View v;
    // The recyclerview
    private RecyclerView rv;
    // its adapter
    private RecyclerView.Adapter rv_adapt;
    // the ingredients that are present
    private ArrayList<IngredientItem> ingredients = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Setting the global view
        v = inflater.inflate(R.layout.fragment_home, container, false);
        // Initiating the recyclerview
        rv = (RecyclerView) v.findViewById(R.id.recyclerViewHomeFragment);
        // Initiating the adapter on the list of ingredients
        rv_adapt = new IngredientAdapter(getContext(), ingredients, this);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        rv.setAdapter(new AlphaInAnimationAdapter(rv_adapt));
        rv.setItemAnimator(new LandingAnimator());

        // Setting Progressbar size
        ProgressBar progressBar = v.findViewById(R.id.homeProgress);
        progressBar.setScaleY(3f);

        // Opening a connection to the database to get all of the ingredients
        DB_Communicator db_communicator = new DB_Communicator(getContext());
        db_communicator.open();

        // Adding all the ingredients we allow to exist
        // Hashmap because it conforms to DB logic (the arraylist has all of the columns for title)
        for (int i = 0; i < db_communicator.getIngredientsData(0).size(); i++)
            all_ingredients.put(db_communicator.getIngredientsData(0).get(i), db_communicator.getIngredientsData(db_communicator.getIngredientsData(0).get(i)));

        // Closing the database connection afterwards
        db_communicator.close();

        // Initiating the auto complete input field
        AutoCompleteTextView ac_text = v.findViewById(R.id.auto_complete_input);

        // Setting the adapter on the ingredient list so we get choices once we type sth into
        // the auto_complete field
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, all_ingredients.keySet().toArray(new String[0]));

        // Setting the adapter to the auto_complete field
        ac_text.setAdapter(adapter);

        // This is the button to add ingredients
        final Button add_button = v.findViewById(R.id.button_add);

        // Initializing the search button
        final Button button_search = v.findViewById(R.id.button_search);

        // The TextChangeListener checks whether we type sth in the input field
        // if this is the case then we want the button to be pressable
        // NOTE: Of course we could also just check if the input is present within the HashMap
        // and then make the button clickable. The reason why I used this listener was to get
        // familiar with more functionality that is offered by Android.
        ac_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // If the text is being changed and is not empty and a valid ingredient then you can add ingredients
                if (!String.valueOf(charSequence).equals("") && all_ingredients.containsKey(charSequence.toString()))
                    add_button.setEnabled(true);
                else
                    add_button.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!ingredients.isEmpty())
                    button_search.setEnabled(true);
                else
                    button_search.setEnabled(false);
            }
        });

        // Setting up an onclick listener for the ingredient adding functionality
        // If the button is clicked then the item present in the autocomplete field is being added
        // to the ingredient list.
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem(0);
                Toast.makeText(getContext(), "Item was successfully added", Toast.LENGTH_SHORT).show();
            }
        });

        // Creating an onclick listener for it
        button_search.setOnClickListener(new View.OnClickListener() {
            // When we click on search then a new activity is being created and the
            // list of picked ingredients is being passed for later processing
            @Override
            public void onClick(View v) {
                // Setting progress as search loading indicator
                ProgressBar progressBar = (ProgressBar) getView().getRootView().findViewById(R.id.homeProgress);
                progressBar.setVisibility(ProgressBar.VISIBLE);
                Intent intent = new Intent(getActivity(), SearchResults.class);
                intent.putParcelableArrayListExtra(IngredientItem.class.getSimpleName(), ingredients);
                getActivity().startActivity(intent);
            }
        });
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // This is some fancy extra functionality I have implemented
    // getting calories and nutritiousness from database
    @Override
    public void ingredientInfo(int position) {
        // I created this dialog box with some additional information about ingredients
        // if an ingredient is being clicked then it pops up and we get more information about it
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.infobox_ingredient_dialog);

        // Creating an animation for the dialog information
        YoYo.with(Techniques.RollIn).repeat(0).repeatMode(1).duration(600).playOn(dialog.findViewById(R.id.infobox));

        TextView info_title = (TextView) dialog.findViewById(R.id.info_title);
        TextView info_desc = (TextView) dialog.findViewById(R.id.info_desc);
        TextView info_kcal = (TextView) dialog.findViewById(R.id.info_kcal);
        TextView info_nutr = (TextView) dialog.findViewById(R.id.info_nutr);

        info_title.setText(ingredients.get(position).getTitle());
        info_desc.setText(ingredients.get(position).getDesc());
        info_kcal.setText(ingredients.get(position).getKcal());
        info_nutr.setText(ingredients.get(position).getNutr());

        dialog.show();
    }

    // In case we make a mistake with an ingredient it makes sense to have functionality to delete it again
    // with a click on the delete symbol next to every ingredient, the ingredient can be remove again from
    // the recyclerview
    @Override
    public void deleteItem(int position) {
        // User feedback that he deleted an ingredient
        Toast.makeText(getContext(), "Deleted " + ingredients.get(position).getTitle() + " Successfully", Toast.LENGTH_SHORT).show();
        // Actually removing the ingredient from the list
        ingredients.remove(position);
        // Notifying the adapter so he updates his display
        rv_adapt.notifyItemRemoved(position);

        // In case the last item was deleted, we shouldn't be able to perform a search
        if (ingredients.isEmpty()) {
            Button button_search = v.findViewById(R.id.button_search);
            button_search.setEnabled(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        ProgressBar progressBar = getActivity().findViewById(R.id.homeProgress);
        progressBar.setVisibility(ProgressBar.INVISIBLE);
    }

    // Adding functionality for Ingredients
    @Override
    public void addItem(int position) {
        // Getting the auto complete input field
        AutoCompleteTextView source = (AutoCompleteTextView) v.findViewById(R.id.auto_complete_input);
        // Getting the Title of the ingredient
        String title = source.getText().toString();
        // Check if we have already added this ingredient
        boolean item_seen = false;

        // Creating a new object for the recyclerview that can later be displayed
        IngredientItem temp = new IngredientItem(R.drawable.ic_restaurant_menu_green_24dp, source.getText().toString(), all_ingredients.get(title).get(1), all_ingredients.get(title).get(2), all_ingredients.get(title).get(3));

        // For every item in our recyclerview ...
        for (int i = 0; i < rv_adapt.getItemCount(); i++)
            // ... if the ingredient we want to add equals the item we recognize as it is already present within
            // our list of ingredients, then we set the item as already seen, so it does not get added again
            if (ingredients.get(i).getTitle().equals(title)) {
                // This log can be uncommented to check if the item is known yet
                // Log.d("Debug", ingredients.get(i).getTitle() + " " + title + " " + rv_adapt.getItemCount());
                item_seen = true;
            }

        // If we recognize the item which should be added as a valid item and we haven't added it
        // yet, then we can add it now to our list of ingredients and notify our adaptor to refresh.
        if (all_ingredients.containsKey(title) && !item_seen) {
            // Notification in Logcat that item xyz has been added
            Log.d("Debug", "Adding " + title + "  " + all_ingredients.get(title));
            ingredients.add(temp);
            rv_adapt.notifyItemInserted(position);
        }

        // After adding an item the auto complete input field is getting emptied again, so
        // a new item can be added. Further the add button is being disabled for cosmetic purposes.
        source.setText("");
        v.findViewById(R.id.button_add).setEnabled(false);

        // If we have 1+ Items in the recyclerview then the search is being enabled, as
        // it makes no sense to search for 0 items
        if (rv_adapt.getItemCount() > 0)
            v.findViewById(R.id.button_search).setEnabled(true);

        // Just a quick log to see which ingredients are present in our list
        // (Debugging purposes)
        Log.d("Debug", ingredients.toString());

        // Notifying the adapter to refresh
        rv_adapt.notifyDataSetChanged();
    }
}