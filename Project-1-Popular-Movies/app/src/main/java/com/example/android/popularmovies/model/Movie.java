package com.example.android.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

/**
 * Created by Neil on 3/18/2017.
 */

public class Movie implements Parcelable
{
    // Constants
    public static final String MOVIE_INTENT_KEY = "movie";

    // JSON Elements
    private static final String MOVIE_ELEMENT_ID                = "id";
    private static final String MOVIE_ELEMENT_ORIGINAL_TITLE    = "original_title";
    private static final String MOVIE_ELEMENT_OVERVIEW          = "overview";
    private static final String MOVIE_ELEMENT_RELEASE_DATE      = "release_date";
    private static final String MOVIE_ELEMENT_VOTE_AVERAGE      = "vote_average";
    private static final String MOVIE_ELEMENT_POSTER_PATH       = "poster_path";
    private static final String MOVIE_ELEMENT_BACKDROP_PATH     = "backdrop_path";
    private static final String MOVIE_ELEMENT_ADULT             = "adult";

    // Properties
    private Integer mId;
    private String mOriginalTitle;
    private String mOverview;
    private Date mReleaseDate;
    private Double mVoteAverage;
    private String mPosterPath;
    private String mBackdropPath;
    private Boolean mAdult;

    public Integer getId()
    {
        return mId;
    }

    public String getOriginalTitle()
    {
        return mOriginalTitle;
    }

    public String getOverview()
    {
        return mOverview;
    }

    public Date getReleaseDate()
    {
        return mReleaseDate;
    }

    public Double getVoteAverage()
    {
        return mVoteAverage;
    }

    public String getPosterPath()
    {
        return mPosterPath;
    }

    public String getBackdropPath()
    {
        return mBackdropPath;
    }

    public Boolean getAdult()
    {
        return mAdult;
    }

    private Movie(Parcel in)
    {
        this.mId             = in.readInt();
        this.mOriginalTitle  = in.readString();
        this.mOverview       = in.readString();
        this.mReleaseDate    = new Date(in.readLong());
        this.mVoteAverage    = in.readDouble();
        this.mPosterPath     = in.readString();
        this.mBackdropPath   = in.readString();
        this.mAdult          = in.readByte() != 0;
    }

    private Movie(Integer id, String originalTitle, String overview, Date releaseDate, Double voteAverage, String posterPath, String backdropPath, Boolean adult)
    {
        this.mId             = id;
        this.mOriginalTitle  = originalTitle;
        this.mOverview       = overview;
        this.mReleaseDate    = releaseDate;
        this.mVoteAverage    = voteAverage;
        this.mPosterPath     = posterPath;
        this.mBackdropPath   = backdropPath;
        this.mAdult          = adult;
    }

    public static Movie movieFromJSON(JSONObject jsonObject)
    {
        Movie movie = null;

        try
        {
            Integer movieId             = jsonObject.getInt(MOVIE_ELEMENT_ID);
            String movieOriginalTitle   = jsonObject.getString(MOVIE_ELEMENT_ORIGINAL_TITLE);
            String movieOverview        = jsonObject.getString(MOVIE_ELEMENT_OVERVIEW);
            String dateString           = jsonObject.getString(MOVIE_ELEMENT_RELEASE_DATE);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date movieReleaseDate       = dateFormat.parse(dateString);
            Double movieVoteAverage     = jsonObject.getDouble(MOVIE_ELEMENT_VOTE_AVERAGE);
            String moviePosterPath      = jsonObject.getString(MOVIE_ELEMENT_POSTER_PATH);
            String movieBackdropPath    = jsonObject.getString(MOVIE_ELEMENT_BACKDROP_PATH);
            Boolean movieAdult          = jsonObject.getBoolean(MOVIE_ELEMENT_ADULT);

            movie = new Movie(movieId, movieOriginalTitle, movieOverview, movieReleaseDate, movieVoteAverage, moviePosterPath, movieBackdropPath, movieAdult);
        }
        catch (ParseException | JSONException e)
        {
            e.printStackTrace();
        }

        return movie;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>()
    {
        public Movie createFromParcel(Parcel in)
        {
            return new Movie(in);
        }

        public Movie[] newArray(int size)
        {
            return new Movie[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(mId);
        dest.writeString(mOriginalTitle);
        dest.writeString(mOverview);
        dest.writeLong(mReleaseDate.getTime());
        dest.writeDouble(mVoteAverage);
        dest.writeString(mPosterPath);
        dest.writeString(mBackdropPath);
        dest.writeByte((byte) (mAdult ? 1 : 0));
    }
}
