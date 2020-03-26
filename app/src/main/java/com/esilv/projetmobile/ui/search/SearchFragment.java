package com.esilv.projetmobile.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.esilv.projetmobile.KitsuService;
import com.esilv.projetmobile.R;
import com.esilv.projetmobile.ui.home.HomeFragment;
import com.esilv.projetmobile.ui.home.MangaAdapter;

import java.io.Console;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchFragment extends Fragment {


    public void loadSearch(final RecyclerView recyclerView, boolean isManga, String searchText){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://kitsu.io")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        KitsuService service = retrofit.create(KitsuService.class);
        Call<HomeFragment.KitsuManga> call = service.getKitsuManga();
        if(searchText == null || searchText.equals("")){
            if(isManga){
                call = service.getKitsuManga();
            }
            else{
                call = service.getKitsuAnime();
            }
        }
        else{
            if(isManga){
                call = service.getKitsuMangaSearch(searchText);
            }
            else{
                call = service.getKitsuAnimeSearch(searchText);
            }
        }
        call.enqueue(new Callback<HomeFragment.KitsuManga>() {
            @Override
            public void onResponse(Call<HomeFragment.KitsuManga> call, Response<HomeFragment.KitsuManga> response) {
                HomeFragment.KitsuManga result = response.body();
                SearchAdapter adapter = new SearchAdapter(result);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<HomeFragment.KitsuManga> call, Throwable t) {
            }
        });

    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);

        final CheckBox manga_check = root.findViewById(R.id.manga_check);
        final CheckBox anime_check = root.findViewById(R.id.anime_check);
        final TextView searchText = root.findViewById(R.id.searchText);
        final Button searchButton = root.findViewById((R.id.searchButton2));
        final RecyclerView searchRecycler = root.findViewById(R.id.searchRecycler);
        loadSearch(searchRecycler,manga_check.isChecked(),searchText.getText().toString());
        manga_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(manga_check.isChecked()){
                    anime_check.setChecked(false);
                }
                else{
                    anime_check.setChecked(true);
                }
                loadSearch(searchRecycler,manga_check.isChecked(),searchText.getText().toString());
            }
        });
        anime_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(anime_check.isChecked()) {
                    manga_check.setChecked(false);
                } else{
                    manga_check.setChecked(true);
                }
                loadSearch(searchRecycler,manga_check.isChecked(),searchText.getText().toString());
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSearch(searchRecycler,manga_check.isChecked(),searchText.getText().toString());
            }
        });
        return root;
    }
}
