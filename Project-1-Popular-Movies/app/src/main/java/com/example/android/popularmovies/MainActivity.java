package com.example.android.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.dialogs.SortTypeChooserDialog;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.MovieSortType;
import com.example.android.popularmovies.utils.JSONUtils;
import com.example.android.popularmovies.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler, SortTypeChooserDialog.OnItemSelectedListener
{
    private static final String MOVIE_QUERY_MOST_POPULAR    = "movie/popular";
    private static final String MOVIE_QUERY_TOP_RATED       = "movie/top_rated";
    private static final int GRID_SPAN_COUNT                = 2;
    private static final double GRID_IMAGE_RATIO            = 1.5;

    private RecyclerView mMoviesRecyclerView;
    private TextView mErrorTextView;
    private ProgressBar mProgressBar;

    private MoviesAdapter mMoviesAdapter;

    private MovieSortType mSortType = MovieSortType.MOST_POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesRecyclerView = (RecyclerView) findViewById(R.id.rv_movies_grid);
        mErrorTextView      = (TextView) findViewById(R.id.tv_error_message);
        mProgressBar        = (ProgressBar) findViewById(R.id.pb_progressBar);

        GridLayoutManager layoutManager = new GridLayoutManager(this, GRID_SPAN_COUNT);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(false);
        mMoviesRecyclerView.setLayoutManager(layoutManager);
        mMoviesRecyclerView.setHasFixedSize(true);

        mMoviesAdapter = new MoviesAdapter(this, this, GRID_SPAN_COUNT, GRID_IMAGE_RATIO);
        mMoviesRecyclerView.setAdapter(mMoviesAdapter);

        loadMovies();
    }

    protected void loadMovies()
    {
        URL urlToDownload = null;

        switch (mSortType)
        {
            case MOST_POPULAR:
                urlToDownload = NetworkUtils.buildMovieDBUrl(MOVIE_QUERY_MOST_POPULAR);
                break;

            case TOP_RATED:
                urlToDownload = NetworkUtils.buildMovieDBUrl(MOVIE_QUERY_TOP_RATED);
                break;
        }

        if (urlToDownload != null)
        {
            new DownloadMoviesTask().execute(urlToDownload);
        }
    }

    protected void showError(String errorMessage)
    {
        mMoviesRecyclerView.setVisibility(View.INVISIBLE);
        mErrorTextView.setText(errorMessage);
        mErrorTextView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    protected void showLoadingIndicator()
    {
        mMoviesRecyclerView.setVisibility(View.INVISIBLE);
        mErrorTextView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    protected void showMovies(List<Movie> returnedMovies)
    {
        if (returnedMovies.isEmpty())
        {
            setTitle("");
            String noDataError = getString(R.string.error_no_movies_to_display);
            showError(noDataError);
        }
        else
        {
            switch (mSortType)
            {
                case MOST_POPULAR:
                    setTitle(getString(R.string.title_popular_movies));
                    break;

                case TOP_RATED:
                    setTitle(getString(R.string.title_top_rated_movies));
                    break;
            }

            mMoviesAdapter.setMovieList(returnedMovies);

            mMoviesRecyclerView.setVisibility(View.VISIBLE);
            mErrorTextView.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(Movie selectedMovie)
    {
        Bundle b = new Bundle();
        b.putParcelable(Movie.MOVIE_INTENT_KEY, selectedMovie);

        Intent i = new Intent(getApplicationContext(), DetailActivity.class);
        i.putExtras(b);

        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.mi_sort)
        {
            SortTypeChooserDialog dialog = SortTypeChooserDialog.newInstance(mSortType);
            dialog.setOnItemSelectedListener(this);
            dialog.show(getSupportFragmentManager(), "sort");

            return true;
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
    }

    public void sortTypSelected(MovieSortType newSortType)
    {
        if (newSortType != mSortType)
        {
            mSortType = newSortType;
            loadMovies();
        }
    }

    private class DownloadMoviesTask extends AsyncTask<URL, Void, List<Movie>>
    {
        private String downloadError;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            showLoadingIndicator();
        }

        @Override
        protected List<Movie> doInBackground(URL... params)
        {
            List<Movie> returnedMovies = null;

            if (params.length > 0)
            {
                URL url = params[0];

                try
                {
                    String jsonResponse = NetworkUtils.getResponseFromHttpUrl(url);

                    Pair<String, List<Movie>> pairResult = JSONUtils.buildMovieList(getApplicationContext(), jsonResponse);

                    if (pairResult.first != null)
                    {
                        downloadError = pairResult.first;
                    }
                    else
                    {
                        returnedMovies = pairResult.second;
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    downloadError = getString(R.string.error_could_not_download);
                }
            }
            else
            {
                downloadError = getString(R.string.error_could_not_make_request);
            }

            return returnedMovies;
        }

        @Override
        protected void onPostExecute(List<Movie> returnedMovies)
        {
            super.onPostExecute(returnedMovies);

            if (downloadError == null)
            {
                showMovies(returnedMovies);
            }
            else
            {
                showError(downloadError);
            }
        }
    }
}
