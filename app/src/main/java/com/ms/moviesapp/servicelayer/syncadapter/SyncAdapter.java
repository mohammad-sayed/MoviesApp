package com.ms.moviesapp.servicelayer.syncadapter;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.ms.moviesapp.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Mohammad-Sayed-PC on 11/4/2016.
 */

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private final String LOG_TAG = SyncAdapter.class.getSimpleName();
    private static boolean mIsRunning = false;


    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        if (mIsRunning)
            return;
        mIsRunning = true;
        String jsonString = null;
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedInputStream = null;

        String operationTag = extras.getString(Constants.Extras.OPERATION_TAG);
        String urlString = extras.getString(Constants.Extras.URI);

        try {
            Log.d(LOG_TAG, urlString);
            URL url = new URL(urlString);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();

            StringBuilder stringBuilder = new StringBuilder();
            if (inputStream == null) {
                notifyResult(operationTag, Constants.Errors.ERROR_CONNECTION);
                return;
            }
            bufferedInputStream = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = bufferedInputStream.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            if (stringBuilder.length() == 0) {
                notifyResult(operationTag, Constants.Errors.ERROR_NO_DATA_RETRIEVED);
                return;
            }
            jsonString = stringBuilder.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            jsonString = Constants.Errors.ERROR_URL;
        } catch (IOException e) {
            e.printStackTrace();
            jsonString = Constants.Errors.ERROR_FETCH_DATA;
        } finally {
            if (httpURLConnection != null)
                httpURLConnection.disconnect();

            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    jsonString = Constants.Errors.ERROR_CLOSING_CONNECTION;
                }
            }
            notifyResult(operationTag, jsonString);
            mIsRunning = false;
        }
    }

    private void notifyResult(String operationTag, String message) {
        Intent intent = new Intent(Constants.BroadcastReceiversActions.SYNC_RESULT_ACTION);
        intent.putExtra(Constants.Extras.OPERATION_TAG, operationTag);
        intent.putExtra(Constants.Extras.SYNC_RESULT, message);
        getContext().sendBroadcast(intent);
        //StartBroadcast Receiver
    }
}
