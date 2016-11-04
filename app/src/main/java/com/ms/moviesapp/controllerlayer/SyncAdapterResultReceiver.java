package com.ms.moviesapp.controllerlayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ms.moviesapp.Constants;
import com.ms.moviesapp.R;
import com.ms.moviesapp.callbacks.OnErrorListener;
import com.ms.moviesapp.callbacks.OnSuccessListener;
import com.ms.moviesapp.entities.ErrorException;

/**
 * Created by Mohammad-Sayed-PC on 11/4/2016.
 */

public class SyncAdapterResultReceiver extends BroadcastReceiver {

    private OnSuccessListener mOnSuccessListener;
    private OnErrorListener mOnErrorListener;
    private String mOperationTag;

    public SyncAdapterResultReceiver(OnSuccessListener onSuccessListener, OnErrorListener onErrorListener) {
        this.mOnSuccessListener = onSuccessListener;
        this.mOnErrorListener = onErrorListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!intent.hasExtra(Constants.Extras.OPERATION_TAG))
            return;
        mOperationTag = intent.getStringExtra(Constants.Extras.OPERATION_TAG);
        String syncResult = intent.getStringExtra(Constants.Extras.SYNC_RESULT);

        switch (syncResult) {
            case Constants.Errors.ERROR_CONNECTION:
                returnError(Constants.ErrorCodes.ERROR_CONNECTION, context.getString(R.string.error_connection));
                break;
            case Constants.Errors.ERROR_CLOSING_CONNECTION:
                returnError(Constants.ErrorCodes.ERROR_CLOSING_CONNECTION, context.getString(R.string.error_closing_connection));
                break;
            case Constants.Errors.ERROR_NO_DATA_RETRIEVED:
                returnError(Constants.ErrorCodes.ERROR_NO_DATA_RETRIEVED, context.getString(R.string.error_no_data_retrieved));
                break;
            case Constants.Errors.ERROR_URL:
                returnError(Constants.ErrorCodes.ERROR_URL, context.getString(R.string.error_url));
                break;
            case Constants.Errors.ERROR_FETCH_DATA:
                returnError(Constants.ErrorCodes.ERROR_FETCH_DATA, context.getString(R.string.error_fetch_data));
                break;
            default:
                returnSuccess(syncResult);
        }
    }

    private StackTraceElement[] getStackTrace() {
        return Thread.currentThread().getStackTrace();
    }

    private void returnSuccess(String jsonString) {
        mOnSuccessListener.onSuccess(mOperationTag, jsonString);
    }

    private void returnError(int code, String message) {
        ErrorException error = new ErrorException(code, message, getStackTrace());
        mOnErrorListener.onError(mOperationTag, error);
    }
}
