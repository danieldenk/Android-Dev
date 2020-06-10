package com.example.whatsinmyfridge;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * This is the Testing Environment for the Retrofit Part of the App
 * Here I wanted to see (by the use of Mockito) if we can receive the URLs of Wikipedia
 **/
@RunWith(MockitoJUnitRunner.class)
public class IsolatedTests {

    @Mock
    private Context context;

    @Mock
    private Intent intent;

    // This is the method to test if we receive a Wikipedia URL
    @Test
    public void testWikipediaURL() {

        // Setting context
        context = Mockito.mock(Context.class);

        // Setting/Mocking activity
        DisplayedRecipeFromSearch activity = Mockito.mock(DisplayedRecipeFromSearch.class);

        // Mocking the getStringExtra method
        when(intent.getStringExtra("RecipeName"))
                .thenReturn("Boiled Egg");


        // Initializing the Retrofit Builder with a gson factory and the base url from the wikipedia API
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://en.wikipedia.org/w/")
                .build();

        // "Initializing" the interface preciously created to perform retrofit
        IRetrofitAPI retrofit_api = retrofit.create(IRetrofitAPI.class);
        ArrayList<String> flat_lst = new ArrayList<String>();
        flat_lst.add(intent.getStringExtra("RecipeName"));

        // Testing if the mocking works (JUNIT helps with that)
        assertEquals("Boiled Egg", intent.getStringExtra("RecipeName"));

        // Attempting to receive the URL for the passed ingredient
        retrofit_api.getURL(flat_lst.get(0)).enqueue(new Callback<JsonElement>() {

            // If a connection could not be established then we are going to see a
            // notification within the Logcat console
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (!response.isSuccessful())
                    Log.d("Debug", "not valid http request");
                JsonArray jsonArray = response.body().getAsJsonArray();

                // Making sure that the output of the JSON Processing will evaluate to the link provided
                assertThat(jsonArray.get(3).toString().replaceAll("\\[|\\]|\"", ""), is("https://en.wikipedia.org/wiki/Boiled_egg"));
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
