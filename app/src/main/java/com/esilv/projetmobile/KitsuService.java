package com.esilv.projetmobile;

import com.esilv.projetmobile.ui.home.HomeFragment;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface KitsuService {
    @GET("/api/edge/trending/manga")
    Call<HomeFragment.KitsuManga> getKitsuManga();
}
