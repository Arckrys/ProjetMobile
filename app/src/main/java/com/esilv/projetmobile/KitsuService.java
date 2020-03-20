package com.esilv.projetmobile;

import com.esilv.projetmobile.ui.home.HomeFragment;
import com.esilv.projetmobile.ui.notifications.NotificationsFragment;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface KitsuService {

    @GET("/api/edge/trending/anime")
    Call<HomeFragment.KitsuManga> getKitsuTrendingAnime();
    Call<HomeFragment.KitsuManga> getKitsuManga();

    @POST("/api/oauth/token")
    Call<NotificationsFragment.KitsuLogin> getKitsuLogin(@Query("grant_type") String type,
                                                         @Query("username") String username,
                                                         @Query("password") String password);
}

