package com.esilv.projetmobile.ui.search;

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

import com.esilv.projetmobile.R;
import com.esilv.projetmobile.ui.home.HomeFragment;

import java.io.InputStream;
import java.util.List;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private List<HomeFragment.data> mDataList;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView SearchTitle;
        public TextView SearchDesc;
        public ImageView SearchImage;
        public SearchViewHolder(View v) {
            super(v);
            SearchTitle = (TextView) v.findViewById(R.id.SearchTitle);
            SearchImage = (ImageView) v.findViewById(R.id.SearchImage);
            SearchDesc = (TextView) v.findViewById((R.id.SearchDesc));
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public SearchAdapter(HomeFragment.KitsuManga myResults) {
        mDataList = myResults.dataList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View searchView = inflater.inflate(R.layout.search_recycler,parent,false);
        return new SearchViewHolder(searchView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        HomeFragment.data results = mDataList.get(position);

        TextView title = holder.SearchTitle;
        String titleText = "Title : " + results.attributes.canonicalTitle;
        title.setText(titleText);
        TextView desc = holder.SearchDesc;
        String descText = "Synopsis : " + results.attributes.synopsis;
        desc.setText(descText);
        new DownloadImageTask(holder.SearchImage).execute(results.attributes.posterImage.posterURL);
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
        holder.SearchImage.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_search_to_mangaInfo, bundle));
        holder.SearchDesc.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_search_to_mangaInfo, bundle));
        holder.SearchTitle.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_search_to_mangaInfo, bundle));


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
