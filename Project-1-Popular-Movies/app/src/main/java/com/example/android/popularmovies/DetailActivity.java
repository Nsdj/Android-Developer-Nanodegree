package com.example.android.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.text.DateFormat;

public class DetailActivity extends AppCompatActivity
{
    private Movie mMovie;
    private ScrollView mMovieDetailsLayout;
    private ImageView mPosterImageView;
    private TextView mTitleTextView;
    private TextView mOverviewTextView;
    private TextView mReleaseDateTextView;
    private TextView mUserScoreTextView;
    private TextView mNoDataTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        setTitle(R.string.title_movie_details);

        mMovieDetailsLayout     = (ScrollView) findViewById(R.id.sv_content);
        mPosterImageView        = (ImageView) findViewById(R.id.iv_movie_poster);
        mOverviewTextView       = (TextView) findViewById(R.id.tv_overview);
        mTitleTextView          = (TextView) findViewById(R.id.tv_movie_title);
        mReleaseDateTextView    = (TextView) findViewById(R.id.tv_movie_release_date);
        mUserScoreTextView      = (TextView) findViewById(R.id.tv_movie_user_score);
        mNoDataTextView         = (TextView) findViewById(R.id.tv_movie_details_error_message);

        Intent sourceIntent = getIntent();

        if (sourceIntent != null)
        {
            Bundle b = sourceIntent.getExtras();

            mMovie = b.getParcelable(Movie.MOVIE_INTENT_KEY);

            URL url = NetworkUtils.buildImageUrl(mMovie.getPosterPath());
            Picasso.with(getApplicationContext()).load(url.toString()).into(mPosterImageView);

            mOverviewTextView.setText(mMovie.getOverview());
            mTitleTextView.setText(mMovie.getOriginalTitle());
            mReleaseDateTextView.setText(getString(R.string.movie_release_date) + " " + DateFormat.getDateInstance().format(mMovie.getReleaseDate()));
            mUserScoreTextView.setText(getString(R.string.movie_user_score) + " " + mMovie.getVoteAverage().toString());
        }
        else
        {
            showNoDataError();
        }
    }

    private void showNoDataError()
    {
        mMovieDetailsLayout.setVisibility(View.INVISIBLE);
        mNoDataTextView.setVisibility(View.VISIBLE);
    }
}
