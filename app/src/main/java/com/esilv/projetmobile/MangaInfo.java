package com.esilv.projetmobile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.esilv.projetmobile.ui.home.HomeFragment;
import com.esilv.projetmobile.ui.notifications.NotificationsViewModel;

import java.io.InputStream;


public class MangaInfo extends Fragment {

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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_manga_info, container, false);

        final TextView InfoTitle = root.findViewById(R.id.InfoTitle);
        String Title = "Title : " + getArguments().getString("InfoTitle");
        InfoTitle.setText(Title);

        final TextView InfoRating = root.findViewById(R.id.InfoRating);
        String Rating = "Average rating : " + getArguments().getString("InfoRating");
        InfoRating.setText(Rating);

        final TextView InfoStart = root.findViewById(R.id.InfoStart);
        String Start = "Starting date : " + getArguments().getString("InfoStart");
        InfoStart.setText(Start);

        final TextView InfoEnd = root.findViewById(R.id.InfoEnd);
        String End = "Ending date : " + getArguments().getString("InfoEnd");
        InfoEnd.setText(End);

        final TextView InfoDesc = root.findViewById(R.id.InfoDesc);
        String Desc = "Synopsis : " + getArguments().getString("InfoDesc");
        InfoDesc.setText(Desc);

        final ImageView InfoImage = root.findViewById(R.id.InfoImage);
        new DownloadImageTask(InfoImage).execute(getArguments().getString("InfoImage"));


        return root;

    }
}

