package com.example.android.popularmovies.utils;

import android.content.Context;
import android.util.Pair;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Neil on 3/16/2017.
 */

public class JSONUtils
{
    private static final String JSON_ELEMENT_STATUS_MESSAGE = "";
    private static final String JSON_ELEMENT_RESULTS        = "results";

    public static Pair<String, List<Movie>> buildMovieList(Context context, String jsonRepresentation)
    {
        String errorMessage = null;
        List<Movie> movies  = new ArrayList<Movie>();

        try
        {
            JSONObject rootObject = new JSONObject(jsonRepresentation);

            if (rootObject.has(JSON_ELEMENT_STATUS_MESSAGE))
            {
                errorMessage = rootObject.getString(JSON_ELEMENT_STATUS_MESSAGE);
            }
            else if (rootObject.has(JSON_ELEMENT_RESULTS))
            {
                JSONArray resultsArray = rootObject.getJSONArray(JSON_ELEMENT_RESULTS);

                for (int i=0; i<resultsArray.length(); i++)
                {
                    JSONObject movieJSON = resultsArray.getJSONObject(i);
                    Movie movie = Movie.movieFromJSON(movieJSON);

                    if (movie != null)
                    {
                        movies.add(movie);
                    }
                }
            }
        }
        catch (JSONException e)
        {
            movies = null;
            errorMessage = context.getString(R.string.error_could_not_parse_response);
            e.printStackTrace();
        }

        return Pair.create(errorMessage, movies);
    }
}
