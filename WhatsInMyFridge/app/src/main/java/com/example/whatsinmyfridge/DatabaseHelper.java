package com.example.whatsinmyfridge;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;


/**
 * This class is being used to connect us to the database
 * We are encapsulating the communication with the database itself so its easier to make the changes
 * i.e. if we create a new local database, at one place
 */
public class DatabaseHelper extends SQLiteAssetHelper {
    // Database Name
    private static final String DB_Name = "recipes_db_7.db";

    // DB Version
    private static final int DB_Version = 1;

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DB_Name, null, DB_Version);
    }
}
