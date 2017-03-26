package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

/**
 * Created by Neil on 3/19/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder>
{
    private List<Movie> mMovieList;
    private Context mContext;
    private MoviesAdapterOnClickHandler mOnClickHandler;
    private int mGridSpanCount;
    private double mGridImageRatio;

    public MoviesAdapter(Context context, MoviesAdapterOnClickHandler onClickHandler, int gridSpanCount, double gridImageRatio)
    {
        mContext        = context;
        mOnClickHandler = onClickHandler;
        mGridSpanCount  = gridSpanCount;
        mGridImageRatio = gridImageRatio;
    }

    public void setMovieList(List<Movie> movies)
    {
        mMovieList = movies;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount()
    {
        if (mMovieList != null)
        {
            return mMovieList.size();
        }
        else
        {
            return 0;
        }
    }

    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.movie_grid_item, parent, false);
        view.getLayoutParams().height =  (int) ( (parent.getWidth() / mGridSpanCount) * mGridImageRatio);

        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder holder, int position)
    {
        Movie movie = mMovieList.get(position);
        URL url = NetworkUtils.buildImageUrl(movie.getPosterPath());
        Picasso.with(mContext).load(url.toString()).into(holder.mImageView);
    }

    public interface MoviesAdapterOnClickHandler
    {
        void onClick(Movie selectedMovie);
    }

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public final ImageView mImageView;

        MoviesAdapterViewHolder(View view)
        {
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.iv_movie_poster);
            mImageView.setOnClickListener(this);
         }

        @Override
        public void onClick(View v)
        {
            Movie selectedMovie = mMovieList.get(getAdapterPosition());
            mOnClickHandler.onClick(selectedMovie);
        }
    }
}
