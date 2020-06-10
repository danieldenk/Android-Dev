package com.example.whatsinmyfridge;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * This is the class providing the functionality for the database
 * Basically everything that is needed to communicate with it so we get the data we are interested in
 */
public class DB_Communicator {
    private static DB_Communicator dc;
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;

    // Initialization of the SQL_Open_Helper
    public DB_Communicator(Context context) {
        this.openHelper = new DatabaseHelper(context);
    }

    // Initialization of our object instance
    public static DB_Communicator getInstance(Context context) {
        if (dc == null)
            dc = new DB_Communicator(context);
        return dc;
    }

    // Method for opening a connection
    public void open() {
        this.db = openHelper.getWritableDatabase();
    }

    // Method for closing the connection
    public void close() {
        if (db != null)
            this.db.close();
    }

    /**
     * This method takes in an index and provides you with an recipe_attribute at index[index]
     * for all recipes that have been saved to the favorite list
     *
     * @param index, the index of the attribute you want to get as int >= 0
     * @return ArrayList<String> containing the corresponding data entries
     */
    public ArrayList<String> getFavoritesData(int index) {
        Cursor c = db.rawQuery("SELECT * FROM recipes WHERE ISFAVORITE=1", new String[]{});

        ArrayList<String> query_result = new ArrayList<>();

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
            query_result.add(c.getString(index));

        c.close();
        return query_result;
    }

    /**
     * This method takes in a recipe name (Prim. Key of DB) and returns the corresponding data touple
     *
     * @param name, the title of the recipe you are searching for
     * @return ArrayList<String> containing the data values for a recipe
     */
    public ArrayList<String> searchRecipe(String name) {
        Cursor c = db.rawQuery("SELECT * FROM recipes WHERE NAME=\"" + name + "\"", new String[]{});
        ArrayList<String> query_result = new ArrayList<>();

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            for (int index = 0; index < c.getColumnCount(); index++) {
                query_result.add(c.getString(index));
            }
        }
        c.close();
        return query_result;
    }

    /**
     * This method is used to return the data not filtered by anything
     *
     * @param index, int >= 0 corresponding to the attribute at position index you want to get
     * @return ArrayList<String> being attribute list at position index
     */
    public ArrayList<String> getData(int index) {
        Cursor c = db.rawQuery("SELECT * FROM recipes", new String[]{});

        ArrayList<String> query_result = new ArrayList<>();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
            query_result.add(c.getString(index));
        c.close();
        return query_result;
    }

    /**
     * This method is being used to set a recipe to favorite / to remove it from the favorite list
     *
     * @param name,   the recipes title
     * @param status, 0 if not a favorite and 1 if a favorite
     */
    public void setFavorite(String name, int status) {
        db.execSQL("UPDATE recipes SET  ISFAVORITE= " + status + " WHERE NAME=\"" + name + "\";");
    }

    /**
     * This method returns the data from a column at int index >=0 for the table ingredients
     *
     * @param index, the index for the column we are interested in
     * @return ArrayList<String> with the list of data values for the column
     */
    public ArrayList<String> getIngredientsData(int index) {
        Cursor c = db.rawQuery("SELECT * FROM ingredients", new String[]{});

        ArrayList<String> query_result = new ArrayList<>();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
            query_result.add(c.getString(index));
        c.close();
        return query_result;
    }

    /**
     * This method returns the data from all columns where the name of the ingredient
     * equals the passed name.
     *
     * @param name, the name of the recipe we are searching for
     * @return The result is ArrayList<String> of columns for the table ingredients of ingredient name
     */
    public ArrayList<String> getIngredientsData(String name) {
        Cursor c = db.rawQuery("SELECT * FROM ingredients WHERE NAME=\"" + name + "\"", new String[]{});
        ArrayList<String> query_result = new ArrayList<>();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
            for (int index = 0; index < c.getColumnCount(); index++)
                query_result.add(c.getString(index));
        c.close();
        return query_result;
    }
}