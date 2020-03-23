package com.esilv.projetmobile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.esilv.projetmobile.ui.home.HomeFragment;
import com.esilv.projetmobile.ui.home.MangaAdapter;
import com.google.gson.annotations.SerializedName;

import java.io.InputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class UserPage extends Fragment {

    public class KitsuBibli{
        @SerializedName("data")
        List<data> dataList;
    }
    public class data {
        @SerializedName("attributes")
        attributes attributes;
    }
    public class attributes{
        @SerializedName("synopsis")
        String synopsis;
        @SerializedName("canonicalTitle")
        String canonicalTitle;
        @SerializedName("titles")
        titles titles;
        @SerializedName("averageRating")
        Double averageRating;
        @SerializedName("startDate")
        String startDate;
        @SerializedName("endDate")
        String endDate;
        @SerializedName("ageRating")
        String ageRating;
        @SerializedName("posterImage")
        posterImage posterImage;

    }
    public class titles{
        @SerializedName("en")
        String englishTitle;
        @SerializedName("en_jp")
        String japaneseTitle;
    }
    public class posterImage {
        @SerializedName("medium")
        String posterURL;
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

    public void loadBibli(final RecyclerView recyclerView){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://kitsu.io")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        KitsuService service = retrofit.create(KitsuService.class);
        Call<KitsuBibli> call = service.getKitsuBibli();
        call.enqueue(new Callback<KitsuBibli>() {
            @Override
            public void onResponse(Call<KitsuBibli> call, Response<KitsuBibli> response) {
                KitsuBibli result = response.body();
                BibliAdapter adapter = new BibliAdapter(result);
                recyclerView.setAdapter(adapter);



            }

            @Override
            public void onFailure(Call<KitsuBibli> call, Throwable t) {
            }
        });

    }

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user_page, container, false);
        RecyclerView recyclerViewBibli = root.findViewById(R.id.recyclerViewBibli);
        loadBibli(recyclerViewBibli);
        return root;
    }
}
