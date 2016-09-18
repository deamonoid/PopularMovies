package com.example.android.popularmovies;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
    }

    public static class DetailFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            Intent intent = getActivity().getIntent();
            if (intent != null && intent.hasExtra("title")) {
                String original_title = intent.getStringExtra("title");
                ((TextView) rootView.findViewById(R.id.detail_title))
                        .setText(original_title);
            }

            if (intent != null && intent.hasExtra("poster_url")) {
                String poster_url = intent.getStringExtra("poster_url");
                Picasso.with(getActivity()).load(poster_url).into((ImageView) rootView.findViewById(R.id.detail_poster));
            }

            if (intent != null && intent.hasExtra("synopsis")) {
                String detail_synopsis = intent.getStringExtra("synopsis");
                ((TextView) rootView.findViewById(R.id.detail_synopsis))
                        .setText(detail_synopsis);
            }

            if (intent != null && intent.hasExtra("user_rating")) {
                String vote_average = intent.getStringExtra("user_rating");
                ((TextView) rootView.findViewById(R.id.detail_vote))
                        .setText(vote_average);
            }

            if (intent != null && intent.hasExtra("release_date")) {
                String release_date = intent.getStringExtra("release_date");
                ((TextView) rootView.findViewById(R.id.detail_releaseDate))
                        .setText(release_date);
            }
            return rootView;
        }
    }
}
