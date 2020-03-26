package com.esilv.projetmobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.esilv.projetmobile.ui.home.HomeFragment;
import com.esilv.projetmobile.ui.home.MangaAdapter;
import com.google.gson.annotations.SerializedName;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;
import static com.esilv.projetmobile.ui.notifications.NotificationsFragment.PREFS;
import static com.esilv.projetmobile.ui.notifications.NotificationsFragment.PREFS_ACCESS;
import static com.esilv.projetmobile.ui.notifications.NotificationsFragment.PREFS_INDEX;
import static com.esilv.projetmobile.ui.notifications.NotificationsFragment.PREFS_USER;


public class UserPage extends Fragment {
    SharedPreferences sharedPreferences;
    TextView username;


    public class KitsuUser{
        @SerializedName("data")
        List<UserData> dataList;
    }
    public class UserData {
        @SerializedName("id")
        String id;
    }


    public class KitsuBibliAnime{
        @SerializedName("data")
        List<dataAn> dataList;
    }
    public class dataAn {
        @SerializedName("id")
        String id;
        @SerializedName("relationships")
        Relationship relationship;
    }

    public class Relationship{
        @SerializedName("anime")
        Anime anime;
    }
    public class Anime{
        @SerializedName("links")
        Link link;
    }
    public class Link{
        @SerializedName("related")
        String url;
    }

    public class KitsuBibli{
        @SerializedName("data")
        List<data> dataList;

        public KitsuBibli(List<data> dataList) {
            this.dataList = dataList;
        }
    }
    public class KitsuSimple{
        @SerializedName("data")
        data datasimple;
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
        String averageRating;
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
        sharedPreferences = getContext().getSharedPreferences(PREFS, MODE_PRIVATE);
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://kitsu.io")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final KitsuService service = retrofit.create(KitsuService.class);
        Call<KitsuBibliAnime> call = service.getKitsuBibliAnime(sharedPreferences.getString(PREFS_ACCESS, ""));
        call.enqueue(new Callback<KitsuBibliAnime>() {
            @Override
            public void onResponse(Call<KitsuBibliAnime> call, Response<KitsuBibliAnime> response) {
                KitsuBibliAnime result = response.body();
                int index;
                ArrayList<data> resultAni = new ArrayList<>();
                final KitsuBibli resultBibli = new KitsuBibli(resultAni);
                for (index = 0; index < 10; index++)
                {
                    String url = result.dataList.get(index).relationship.anime.link.url;
                    url = url.replace("https://kitsu.io/api/edge/library-entries/", "");
                    url = url.replace("/anime", "");
                    url.trim();
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.putString(PREFS_INDEX, url);
                    edit.commit();
                    Call<KitsuSimple> call2 = service.getKitsuBibli(url);
                    call2.enqueue(new Callback<KitsuSimple>() {
                        @Override
                        public void onResponse(Call<KitsuSimple> call, Response<KitsuSimple> response) {
                            KitsuSimple resultAnime = response.body();
                            resultBibli.dataList.add(resultAnime.datasimple);
                            if (resultBibli.dataList.size() == 10){
                                BibliAdapter adapter = new BibliAdapter(resultBibli);
                                recyclerView.setAdapter(adapter);
                            }
                        }

                        @Override
                        public void onFailure(Call<KitsuSimple> call, Throwable t) {
                            SharedPreferences.Editor edit = sharedPreferences.edit();
                            edit.putString(PREFS_INDEX, "null3");
                            edit.commit();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<KitsuBibliAnime> call, Throwable t) {
            }
        });

    }

    public void GetUserID (final RecyclerView recyclerView){
        sharedPreferences = getContext().getSharedPreferences(PREFS, MODE_PRIVATE);
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://kitsu.io")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final KitsuService service = retrofit.create(KitsuService.class);
        Call<KitsuUser> call = service.getKitsuUser("banane");
        call.enqueue(new Callback<KitsuUser>() {
            @Override
            public void onResponse(Call<KitsuUser> call, Response<KitsuUser> response) {
                KitsuUser result = response.body();
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString(PREFS_ACCESS, result.dataList.get(0).id);
                edit.commit();
                loadBibli(recyclerView);
            }
            @Override
            public void onFailure(Call<KitsuUser> call, Throwable t) {
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user_page, container, false);

        RecyclerView recyclerViewBibli = root.findViewById(R.id.recyclerViewBibli);
        GetUserID(recyclerViewBibli);
        return root;
    }
}
