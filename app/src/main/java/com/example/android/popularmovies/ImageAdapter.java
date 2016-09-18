package com.example.android.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageAdapter extends BaseAdapter {

    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return ShowMovies.urls.size();
    }

    public String getItem(int position) {
        return ShowMovies.urls.get(position);
    }

    public String getTitle(int position) {
        return ShowMovies.title[position];
    }

    public String getSynopsis(int position) {
        return ShowMovies.synopsis[position];
    }

    public String getUserRating(int position) {
        return ShowMovies.user_rating[position];
    }

    public String getReleaseDate(int position) {
        return ShowMovies.release_date[position];
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            imageView.setAdjustViewBounds(true);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }

        Uri imgUri = Uri.parse(ShowMovies.urls.get(position));
        Picasso picasso = Picasso.with(mContext);
        picasso.setIndicatorsEnabled(false);
        picasso.load(imgUri).placeholder(R.drawable.placeholder).into(imageView);

        return imageView;
    }
}