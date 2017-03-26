package com.example.android.popularmovies.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Neil on 3/16/2017.
 */

public class NetworkUtils
{
    private final static String MOVIE_DB_QUERIES_BASE_URL   = "http://api.themoviedb.org/3";
    private final static String MOVIE_DB_IMAGE_BASE_URL     = "http://image.tmdb.org/t/p";
    private final static String DEFAULT_IMAGE_SIZE          = "w185";
    private final static String PARAM_API_KEY               = "api_key";

    // TODO: Set API Key
    private final static String API_KEY                     = "";

    public static URL buildMovieDBUrl(String queryPath)
    {
        URL url = null;

        Uri movieDBUri = Uri.parse(MOVIE_DB_QUERIES_BASE_URL)
                            .buildUpon()
                            .appendEncodedPath(queryPath)
                            .appendQueryParameter(PARAM_API_KEY, API_KEY)
                            .build();

        try
        {
            url = new URL(movieDBUri.toString());
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildImageUrl(String imageFilename)
    {
        URL url = null;

        Uri movieDBImageUri = Uri.parse(MOVIE_DB_IMAGE_BASE_URL)
                                .buildUpon()
                                .appendPath(DEFAULT_IMAGE_SIZE)
                                .appendEncodedPath(imageFilename)
                                .build();

        try
        {
            url = new URL(movieDBImageUri.toString());
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException
    {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        String response = null;

        try
        {
            InputStream in = connection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            if (scanner.hasNext())
            {
                response = scanner.next();
            }
        }
        finally
        {
            connection.disconnect();
        }

        return response;
    }
}

