package com.esilv.projetmobile;

import android.content.Context;
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

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.esilv.projetmobile.ui.home.HomeFragment;
import com.esilv.projetmobile.ui.home.MangaAdapter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class BibliAdapter extends RecyclerView.Adapter<BibliAdapter.BibliViewHolder>{
    private List<UserPage.data> mDataList =  new ArrayList<>();

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class BibliViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mangaTitle;
        public ImageView mangaImage;
        public BibliViewHolder(View v) {
            super(v);
            mangaTitle = (TextView) v.findViewById(R.id.mangaTitle);
            mangaImage = (ImageView) v.findViewById(R.id.mangaImage);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public BibliAdapter(UserPage.KitsuBibli myResults) {
        mDataList = myResults.dataList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public BibliAdapter.BibliViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View bibliView = inflater.inflate(R.layout.bibli_recycler,parent,false);
        return new BibliViewHolder(bibliView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(BibliViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        UserPage.data results = mDataList.get(position);

        TextView title = holder.mangaTitle;
        title.setText(results.attributes.canonicalTitle);
        new DownloadImageTask(holder.mangaImage).execute(results.attributes.posterImage.posterURL);
        Bundle bundle = new Bundle();
        bundle.putString("InfoTitle", results.attributes.canonicalTitle);
        bundle.putString("InfoImage",results.attributes.posterImage.posterURL);
        bundle.putString("InfoDesc",results.attributes.synopsis);
        if(results.attributes.averageRating == null){
            bundle.putString("InfoRating","no data");
        }
        else{
            bundle.putString("InfoRating",results.attributes.averageRating);
        }
        bundle.putString("InfoStart",results.attributes.startDate);
        if(results.attributes.endDate == null){
            bundle.putString("InfoEnd","ongoing");
        }
        else{
            bundle.putString("InfoEnd",results.attributes.endDate);
        }
        holder.mangaImage.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_user_page_to_mangaInfo, bundle));
        holder.mangaTitle.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_user_page_to_mangaInfo, bundle));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataList.size();
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
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
}
