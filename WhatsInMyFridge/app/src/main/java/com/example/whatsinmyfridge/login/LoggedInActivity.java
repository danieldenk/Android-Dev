package com.example.whatsinmyfridge.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.example.whatsinmyfridge.fragments.About;
import com.example.whatsinmyfridge.R;
import com.example.whatsinmyfridge.fragments.FavoriteFragment;
import com.example.whatsinmyfridge.fragments.HomeFragment;
import com.example.whatsinmyfridge.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

/**
 * This is the class of the activity which is being called when the login succeeded
 * (Basically the main activity)
 */
public class LoggedInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        // Setting the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setting them bottom navigation bar
        BottomNavigationView nav = findViewById(R.id.navigation_bottom);

        // Setting an item listener so we know where to navigate to once an item has been pressed
        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                ///////////////////////////////////////////////////////////
                //   If Item X has been pressed navigate to Fragment X   //
                ///////////////////////////////////////////////////////////

                if (menuItem.getItemId() == R.id.home) {
                    HomeFragment frg = new HomeFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frame_layout, frg);
                    ft.commit();
                }
                if (menuItem.getItemId() == R.id.fav) {
                    FavoriteFragment frg = new FavoriteFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frame_layout, frg);
                    ft.commit();
                }
                if (menuItem.getItemId() == R.id.search) {
                    SearchFragment frg = new SearchFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frame_layout, frg);
                    ft.commit();
                }
                return true;
            }
        });
        // Default Option pressed
        nav.setSelectedItemId(R.id.home);
    }

    // Inflating bottom menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items_toolbar, menu);
        return true;
    }

    // For the top menu in the toolbar...
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // For debugging purposes to display what has been pressed on
        Log.d("Debug", "You pressed on: " + item.getTitle().toString());
        // If we pressed on About then we need to start the activity for About
        // Accessing the string value directly because of multi language support!
        if (item.getTitle().toString().equals(getString(R.string.about))) {
            Intent intent = new Intent(this, About.class);
            startActivity(intent);
            return true;
        } else {
            // Else (There is only Logout left as an else option)
            // we are going to logout from this app and return to the login screen
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            // The following flags make sure we cannot return back to the activity as we are logged out
            // and should therefore not have access to the activity in the back
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }
    }
}