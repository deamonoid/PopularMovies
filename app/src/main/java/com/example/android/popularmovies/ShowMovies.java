package com.example.android.popularmovies;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ShowMovies extends Fragment {


    private final String LOG_TAG = ShowMovies.class.getSimpleName();
    private GridView gridView;
    public static ArrayList<String> urls = new ArrayList<String>();
    public static String[] title = new String[20];
    public static String[] synopsis = new String[20];
    public static String[] user_rating = new String[20];
    public static String[] release_date = new String[20];
    public static String sorting_view = "popular";

    public void onStart() {
        super.onStart();
        updateMoviesList();
    }

    public void updateMoviesList() {

        FetchPopularMoviePoster fetchPopularMoviePoster = new FetchPopularMoviePoster();
        fetchPopularMoviePoster.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, sorting_view);
    }

    public ShowMovies() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.selectmovies_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popular_movies:
                popular_movies();
                return true;
            case R.id.top_rated:
                top_rated();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void popular_movies() {
        sorting_view = "popular";
        updateMoviesList();
        Log.v(LOG_TAG, "Popular Movies is selected");
    }

    public void top_rated() {
        sorting_view = "top_rated";
        updateMoviesList();
        Log.v(LOG_TAG, "Top rated is selected");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) rootView.findViewById(R.id.gridView);
        final ImageAdapter imageAdapter = new ImageAdapter(getActivity());
        gridView.setAdapter(imageAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String poster_url = imageAdapter.getItem(position);
                String original_title = imageAdapter.getTitle(position);
                String detail_synopsis = imageAdapter.getSynopsis(position);
                String vote_average = imageAdapter.getUserRating(position);
                String detail_releaseDate = imageAdapter.getReleaseDate(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("poster_url", poster_url);
                intent.putExtra("title", original_title);
                intent.putExtra("synopsis", detail_synopsis);
                intent.putExtra("user_rating", vote_average);
                intent.putExtra("release_date", detail_releaseDate);
                startActivity(intent);
            }
        });

        return rootView;
    }

    private class FetchPopularMoviePoster extends AsyncTask<String, Void, String[]> {


        private String[] getMoviesDataFromJSON(String getMovieJSONstr)
                throws JSONException {

            String[] poster_url = new String[20];

            JSONObject jsonRootObject = new JSONObject(getMovieJSONstr);
            JSONArray results = jsonRootObject.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject jsonObject = results.getJSONObject(i);
                poster_url[i] = jsonObject.getString("poster_path");
                title[i] = jsonObject.getString("original_title");
                synopsis[i] = jsonObject.getString("overview");
                user_rating[i] = jsonObject.getString("vote_average");
                release_date[i] = jsonObject.getString("release_date");
            }
            return poster_url;

        }

        public FetchPopularMoviePoster() {
            super();
        }

        @Override
        protected String[] doInBackground(String... params) {


            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String getMovieJSONstr = null;

            String popular_sorting = params[0];
            Log.v(LOG_TAG, "popular_sorting value " + popular_sorting);

            try {


                final String BASE_URL = "http://api.themoviedb.org/3/movie/";
                final String API_KEY = "api_key";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendPath(popular_sorting)
                        .appendQueryParameter(API_KEY, BuildConfig.MOVIE_DATABASE_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();


                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }

                getMovieJSONstr = buffer.toString();

            } catch (IOException e) {
                return null;
            } finally {
                if (urlConnection != null && reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            try {
                return getMoviesDataFromJSON(getMovieJSONstr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] results) {
            if (results != null) {
                urls.clear();
                for (String dayDream : results) {
                    urls.add("http://image.tmdb.org/t/p/w185/" + dayDream);
                }
                ImageAdapter imageAdapter = new ImageAdapter(getActivity());
                gridView.setAdapter(imageAdapter);
                Log.v(LOG_TAG, "Urls updated");
            }
            else {
                Toast.makeText(getActivity(), "Oops! Something went wrong, please check your internet connection and try again.", Toast.LENGTH_LONG).show();
            }
        }

    }
}