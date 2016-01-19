package com.ms.moviesapp.servicelayer;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Mohammad-Sayed-PC on 1/9/2016.
 */
public class FetchDataAsyncTask extends AsyncTask<String, Void, String> {

    public final String ERROR_NO_DATA_RETRIEVED = "error_no_data_retrieved";
    public final String ERROR_CONNECTION = "error_connection";
    public final String ERROR_CLOSING_CONNECTION = "error_closing_connection";
    public final String ERROR_FETCH_DATA = "error_fetching_data";
    public final String ERROR_URL = "error_url";

    private final String LOG_TAG = FetchDataAsyncTask.class.getSimpleName();
    private final String ERROR = "error";
    private DataFetchedListener mDataFetchedListener;

    public FetchDataAsyncTask(DataFetchedListener dataFetchedListener) {
        this.mDataFetchedListener = dataFetchedListener;
    }

    @Override
    protected String doInBackground(String... params) {
        String moviesJsonString = null;
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedInputStream = null;
        try {
            Log.d(LOG_TAG, params[0]);
            URL url = new URL(params[0]);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();

            StringBuilder stringBuilder = new StringBuilder();
            if (inputStream == null)
                return ERROR_CONNECTION;
            bufferedInputStream = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = bufferedInputStream.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            if (stringBuilder.length() == 0)
                return ERROR_NO_DATA_RETRIEVED;
            moviesJsonString = stringBuilder.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            moviesJsonString = ERROR_URL;
        } catch (IOException e) {
            e.printStackTrace();
            moviesJsonString = ERROR_FETCH_DATA;
        } finally {
            if (httpURLConnection != null)
                httpURLConnection.disconnect();

            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    moviesJsonString = ERROR_CLOSING_CONNECTION;
                }
            }
        }
        return moviesJsonString;
    }

    @Override
    protected void onPostExecute(String s) {
        switch (s) {
            case ERROR:
                mDataFetchedListener.onDataFetchError(ERROR);
                break;
            case ERROR_CONNECTION:
                mDataFetchedListener.onDataFetchError(ERROR_CONNECTION);
                break;
            case ERROR_NO_DATA_RETRIEVED:
                mDataFetchedListener.onDataFetchError(ERROR_NO_DATA_RETRIEVED);
                break;
            case ERROR_URL:
                mDataFetchedListener.onDataFetchError(ERROR_URL);
                break;
            case ERROR_FETCH_DATA:
                mDataFetchedListener.onDataFetchError(ERROR_FETCH_DATA);
                break;
            default:
                mDataFetchedListener.onDataFetched(s);
        }
    }

    public interface DataFetchedListener {
        void onDataFetched(String data);

        void onDataFetchError(String error);
    }
}
