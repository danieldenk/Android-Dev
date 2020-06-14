package com.example.whatsinmyfridge.recipes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.whatsinmyfridge.R;

import java.util.ArrayList;

/**
 * This is the adapter class for the recipes
 * We need it to dynamically display the list of recipes
 */
public class RecipeAdapter extends BaseAdapter {
    // Context
    Context context;
    // Layout
    private int layout;
    // List of recipes to hold
    private ArrayList<Recipe> recipes;

    // Constructor
    public RecipeAdapter(Context c, int l, ArrayList<Recipe> lst) {
        this.context = c;
        this.layout = l;
        this.recipes = lst;
    }

    ////////////// SIMPLE GETTERS AND SETTERS /////////////

    @Override
    public int getCount() {
        return recipes.size();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    public ArrayList<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }

    @Override
    public Object getItem(int i) {
        return recipes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        ViewHolder holder;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(layout, null);
            holder = new ViewHolder(v);
            holder.title = (TextView) v.findViewById(R.id.grid_item_title);
            holder.diff = (TextView) v.findViewById(R.id.grid_item_diff);
            holder.thumbnail = (ImageView) v.findViewById(R.id.grid_item_image);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        Recipe recipe = recipes.get(i);
        holder.title.setText(recipe.getTitle());
        holder.diff.setText(recipe.getDiff());

        Glide.with(context).load(recipes.get(i).getImg_url()).dontAnimate().into(holder.thumbnail);

        return v;
    }

    private class ViewHolder {
        ImageView thumbnail;
        TextView diff, title;

        public ViewHolder(View v) {
            this.thumbnail = (ImageView) v.findViewById(R.id.grid_item_image);
            this.diff = (TextView) v.findViewById(R.id.grid_item_diff);
            this.title = (TextView) v.findViewById(R.id.grid_item_title);
        }
    }
}
