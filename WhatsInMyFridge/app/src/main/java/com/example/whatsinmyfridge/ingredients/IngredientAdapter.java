package com.example.whatsinmyfridge.ingredients;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsinmyfridge.R;

import java.util.ArrayList;

/**
 * The class responsible for holding and managing the ingredient items
 */
public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    // Context
    Context context;
    // List of ingredients
    ArrayList<IngredientItem> listOfIngredients;
    // Onclick listener for ingredient items
    private IngredientInfoListener iil;

    // Constructor
    public IngredientAdapter(Context context, ArrayList<IngredientItem> data, IngredientInfoListener iil) {
        this.listOfIngredients = data;
        this.context = context;
        this.iil = iil;
    }

    ////// IMPLEMENTATION OF VIEWHOLDER & GETTERS and SETTERS //////
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.ingredient_item, parent, false);
        return new ViewHolder(v, iil);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(listOfIngredients.get(position).getTitle());
        holder.desc.setText(listOfIngredients.get(position).getDesc());
        holder.img.setImageResource(listOfIngredients.get(position).getImg_res());
    }

    @Override
    public int getItemCount() {
        return listOfIngredients.size();
    }

    // Interface that is being used to model advanced onclick functionality
    // Information Screen via Dialog
    // Adding of ingredient
    // Deleting of ingredient
    public interface IngredientInfoListener {
        void ingredientInfo(int position);

        void deleteItem(int position);

        void addItem(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // The interface providing additional functionality
        IngredientInfoListener iil;
        // Title and description of the ingredient
        private TextView title, desc;
        // Image (already set) and deletion image
        private ImageView img, del;

        // Constructor
        public ViewHolder(@NonNull View itemView, IngredientInfoListener iil) {
            super(itemView);
            this.title = itemView.findViewById(R.id.ingredient_title);
            this.desc = itemView.findViewById(R.id.ingredient_desc);
            this.img = itemView.findViewById(R.id.ingredient_image);
            this.del = itemView.findViewById(R.id.imageDelete);

            // Adding of Onclick Listeners for all of the items that should provide click functionality
            del.setOnClickListener(this);
            title.setOnClickListener(this);
            desc.setOnClickListener(this);
            img.setOnClickListener(this);

            this.iil = iil;
        }

        @Override
        public void onClick(View view) {
            // If delete image was clicked then delete functionality is performed
            if (view.getResources().getResourceEntryName(view.getId()).equals(view.getResources().getResourceEntryName(R.id.imageDelete))) {
                iil.deleteItem(getAdapterPosition());
                // If the add ingredients button is clicked then add item is being called
            } else if (view.getResources().getResourceEntryName(view.getId()).equals(view.getResources().getResourceEntryName(R.id.button_add))) {
                iil.addItem(getAdapterPosition());
            } else {
                // For the others if we press them then we get a dialog with extra information
                iil.ingredientInfo(getAdapterPosition());
            }

            // Just a quick debugging check if deletion is working:
            //Boolean bool = view.getResources().getResourceEntryName(view.getId()).equals(view.getResources().getResourceEntryName(R.id.imageDelete));
            //Log.d("Debug", bool.toString());
        }
    }
}