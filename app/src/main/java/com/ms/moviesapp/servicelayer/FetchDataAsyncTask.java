package com.ms.moviesapp.servicelayer;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ms.moviesapp.Constants;
import com.ms.moviesapp.Constants.Errors;
import com.ms.moviesapp.R;
import com.ms.moviesapp.callbacks.OnErrorListener;
import com.ms.moviesapp.callbacks.OnSuccessListener;
import com.ms.moviesapp.entities.ErrorException;

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

    private final String LOG_TAG = FetchDataAsyncTask.class.getSimpleName();
    private Context mContext;
    private String mOperationName;
    private OnSuccessListener mOnSuccessListener;
    private OnErrorListener mOnErrorListener;

    public FetchDataAsyncTask(Context context, String operationName, OnSuccessListener onSuccessListener, OnErrorListener onErrorListener) {
        this.mContext = context;
        this.mOperationName = operationName;
        this.mOnSuccessListener = onSuccessListener;
        this.mOnErrorListener = onErrorListener;
    }

    @Override
    protected String doInBackground(String... params) {
        String jsonString = null;
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
                return Errors.ERROR_CONNECTION;
            bufferedInputStream = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = bufferedInputStream.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            if (stringBuilder.length() == 0)
                return Errors.ERROR_NO_DATA_RETRIEVED;
            jsonString = stringBuilder.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            jsonString = Errors.ERROR_URL;
        } catch (IOException e) {
            e.printStackTrace();
            jsonString = Errors.ERROR_FETCH_DATA;
        } finally {
            if (httpURLConnection != null)
                httpURLConnection.disconnect();

            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    jsonString = Errors.ERROR_CLOSING_CONNECTION;
                }
            }
        }
        return jsonString;
    }

    @Override
    protected void onPostExecute(String s) {
        switch (s) {
            case Errors.ERROR_CONNECTION:
                returnError(Constants.ErrorCodes.ERROR_CONNECTION, mContext.getString(R.string.error_connection));
                break;
            case Errors.ERROR_CLOSING_CONNECTION:
                returnError(Constants.ErrorCodes.ERROR_CLOSING_CONNECTION, mContext.getString(R.string.error_closing_connection));
                break;
            case Errors.ERROR_NO_DATA_RETRIEVED:
                returnError(Constants.ErrorCodes.ERROR_NO_DATA_RETRIEVED, mContext.getString(R.string.error_no_data_retrieved));
                break;
            case Errors.ERROR_URL:
                returnError(Constants.ErrorCodes.ERROR_URL, mContext.getString(R.string.error_url));
                break;
            case Errors.ERROR_FETCH_DATA:
                returnError(Constants.ErrorCodes.ERROR_FETCH_DATA, mContext.getString(R.string.error_fetch_data));
                break;
            default:
                returnSuccess(s);
        }
    }

    private StackTraceElement[] getStackTrace() {
        return Thread.currentThread().getStackTrace();
    }

    private void returnSuccess(String jsonString) {
        mOnSuccessListener.onSuccess(mOperationName, jsonString);
    }

    private void returnError(int code, String message) {
        ErrorException error = new ErrorException(code, message, getStackTrace());
        mOnErrorListener.onError(mOperationName, error);
    }
}
