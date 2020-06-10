package com.example.whatsinmyfridge;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * The interface which is used for retrofit and the communication regarding JSON
 */
public interface IRetrofitAPI {
    // The first connection will be for the data entry
    // Short snippet information from wikipedia
    @GET("api.php?action=query&list=search&utf8=&format=json&srlimit=1")
    Call<JsonElement> getEntry(@Query("srsearch") String srsearch);

    // the second connection will be for the URL of a data entry
    // it is running through a different part of the API and it has to be processed differently
    // therefore I have put it here instead. Also to reduce complexity.
    @GET("api.php?action=opensearch&list=search&utf8=&format=json&limit=1&namespace=0")
    Call<JsonElement> getURL(@Query("search") String search);

    //////////////////////////////////////////////////////////////////////////////////////////////
    // PLEASE NOTE: Both of the Queries seem similar at the first impression,                   //
    // Nevertheless this is not the case. We have two different statements that                 //
    // need to be passed:                                                                       //
    // Ex.: srsearch vs. search || srlimit vs. limit                                            //
    // Together with all of the other parameters that only apply to either one of them.         //
    // To keep things simple and, as there is no benefit in bloating the code with parameters   //
    // that are not actually needed to supply functionality, I decided to create two Requests   //
    // instead. Each one for one API Interface. I might add more functionality to either of     //
    // them later and then I will make use of the other query parameters.                       //
    //////////////////////////////////////////////////////////////////////////////////////////////
}
