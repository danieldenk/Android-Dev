package com.example.whatsinmyfridge;

import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * These are the tests for the database logic. As this app is highly dependent
 * on the database, it makes sense to test it thoroughly.
 * @see DB_Communicator
 */
@RunWith(JUnit4.class)
public class DatabaseTests {
    @Rule
    public ActivityTestRule<LoggedInActivity> activityRule = new ActivityTestRule(LoggedInActivity.class);
    private DB_Communicator db_communicator;

    @Before
    public void setUp() throws Exception {
        // Setting up database connection
        db_communicator = new DB_Communicator(activityRule.getActivity());
        db_communicator.open();
    }

    // Testing getFavoritesData together with setFavorite
    @Test
    public void getFavoritesDataTest() {
        // Getting all of the Recipe Titles from the Recipes within the Favorites List
        assertEquals(db_communicator.getFavoritesData(0), new ArrayList<String>(Arrays.asList("Fried Egg", "Schnitzel", "Spaghetti")));

        // Now we are testing if the method is reacting to changes in the favorites list
        db_communicator.setFavorite("Boiled Egg", 1);
        assertEquals(db_communicator.getFavoritesData(0), new ArrayList<String>(Arrays.asList("Fried Egg", "Schnitzel", "Boiled Egg", "Spaghetti")));
    }

    @Test(expected = IllegalStateException.class)
    public void getFavoritesDataTestException() {
        // Index Out of Bounds because we have less/too many columns
        assertTrue(db_communicator.getFavoritesData(-1).toString().contains(new IllegalStateException().toString()));
        assertEquals(db_communicator.getFavoritesData(db_communicator.getData(0).size() + 1), new IndexOutOfBoundsException());
    }

    @Test
    public void searchRecipeTest() {
        // Avocado Toast does not exist so the result should be empty
        assertEquals(db_communicator.searchRecipe("Avocado Toast").size(), 0);
        // Fried Egg does exist and the values returned should equal the ones provided
        assertEquals(db_communicator.searchRecipe("Fried Egg"), new ArrayList<String>(Arrays.asList("Fried Egg", "An egg that has been fried\n" +
                "\n" +
                "Instruction:\n" +
                "1. Crack the egg\n" +
                "2. Insert the cracked egg into the pan\n" +
                "3. Season it\n" +
                "4. You are done", "Super Easy", "Eggs", "1", "http://www.lecker.de/assets/field/image/wie-macht-man-spiegelei-b2.jpg")));
    }

    @Test
    public void getDataTest(){
        // Checks if the amount of data returned matches the size of elements in the database
        assertEquals(db_communicator.getData(5).size(), 5);
        // Gives the difficulty of all of the recipes in the database
        assertEquals(db_communicator.getData(2), new ArrayList<String>(Arrays.asList("Super Easy", "Medium", "Super Easy", "Easy", "Medium")));
    }

    @Test
    public void getIngredientsDataTest(){
        // Checks if an ingredient of type banana exists
        assertEquals(db_communicator.getIngredientsData("Banana").size(), 0);
        // Checks if an ingredient of type flour exists
        assertNotSame(db_communicator.getIngredientsData("Flour").size(), 0);
        // Checks all ingredients for the ones we expect to exist
        assertEquals(db_communicator.getIngredientsData(0), new ArrayList<String>(Arrays.asList("Noodles", "Meat", "Flour", "Eggs", "Sauce", "Onion", "Bread")));
    }

    @Test(expected = IllegalStateException.class)
    public void getIngredientsDataTestException(){
        // Checking indices against columns
        assertSame(db_communicator.getIngredientsData(-1), new IllegalStateException());
        assertSame(db_communicator.getIngredientsData(99), new IllegalStateException());
        }

    //Rolling back changes that were made before
    @Before
    public void tearDown() throws Exception {
        db_communicator.setFavorite("Boiled Egg", 0);
        //db_communicator.close();
    }


}