package com.esilv.projetmobile.ui.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.esilv.projetmobile.KitsuService;
import com.esilv.projetmobile.R;
import com.esilv.projetmobile.ui.search.SearchFragment;
import com.esilv.projetmobile.ui.search.SearchViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.annotations.SerializedName;

import java.io.InputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    public class KitsuManga {
        @SerializedName("data")
        public List<data> dataList;
    }
    public class data {
        @SerializedName("attributes")
        public attributes attributes;
    }
    public class attributes{
        @SerializedName("synopsis")
        public String synopsis;
        @SerializedName("canonicalTitle")
        public String canonicalTitle;
        @SerializedName("titles")
        public titles titles;
        @SerializedName("averageRating")
        public String averageRating;
        @SerializedName("startDate")
        public String startDate;
        @SerializedName("endDate")
        public String endDate;
        @SerializedName("ageRating")
        public String ageRating;
        @SerializedName("posterImage")
        public posterImage posterImage;

    }
    public class titles{
        @SerializedName("en")
        public String englishTitle;
        @SerializedName("en_jp")
        public String japaneseTitle;
    }
    public class posterImage {
        @SerializedName("medium")
        public String posterURL;
    }
    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    private HomeViewModel homeViewModel;

    public void loadManga(final RecyclerView recyclerView){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://kitsu.io")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        KitsuService service = retrofit.create(KitsuService.class);
        Call<KitsuManga> call = service.getKitsuTrendingManga();
        call.enqueue(new Callback<KitsuManga>() {
            @Override
            public void onResponse(Call<KitsuManga> call, Response<KitsuManga> response) {
                KitsuManga result = response.body();
                MangaAdapter adapter = new MangaAdapter(result);
                recyclerView.setAdapter(adapter);



            }

            @Override
            public void onFailure(Call<KitsuManga> call, Throwable t) {
            }
        });

    }

    public void loadAnime(final RecyclerView recyclerView){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://kitsu.io")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        KitsuService service = retrofit.create(KitsuService.class);
        Call<KitsuManga> call = service.getKitsuTrendingAnime();
        call.enqueue(new Callback<KitsuManga>() {
            @Override
            public void onResponse(Call<KitsuManga> call, Response<KitsuManga> response) {
                KitsuManga result = response.body();
                MangaAdapter adapter = new MangaAdapter(result);
                recyclerView.setAdapter(adapter);



            }

            @Override
            public void onFailure(Call<KitsuManga> call, Throwable t) {
            }
        });

    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerViewManga = root.findViewById(R.id.recyclerViewManga);
        loadManga(recyclerViewManga);
        RecyclerView recyclerViewAnime = root.findViewById(R.id.recyclerViewAnime);
        loadAnime(recyclerViewAnime);
        return root;
    }
}