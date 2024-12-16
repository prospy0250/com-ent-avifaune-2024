package com.example.myapplication;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface GoogleFormApi {

    // Sends form data to the specified Google Form
    @FormUrlEncoded
    @POST("/forms/d/e/1FAIpQLScC1-0J39OuIBUedhf_xK_w7qYT8GXVtVpl7SvL4yCPN0VLWA/formResponse")
    Call<Void> sendFormData(
            // Birds fields
            @Field("entry.592245432") String becasseauNumber,
            @Field("entry.1372224824") String bernacheNumber,
            @Field("entry.582994154") String goelandNumber,
            @Field("entry.608828728") String mouetteNumber,
            @Field("entry.2059411273") String pluvierNumber,
            @Field("entry.2099434669") String cormoranNumber,
            @Field("entry.1505904869") String foulqueNumber,
            @Field("entry.1113424666") String tadorneNumber,
            @Field("entry.1340159476") String chevalierNumber,

            // Other fields
            @Field("entry.311834827") String unknownSpecies,
            @Field("entry.1710251376") String zone,
            @Field("entry.1284627072") String surroundings,
            @Field("entry.1321597415") String time
    );
}

