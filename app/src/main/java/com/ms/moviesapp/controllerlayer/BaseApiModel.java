package com.ms.moviesapp.controllerlayer;

import android.content.Context;

import com.ms.moviesapp.Constants;
import com.ms.moviesapp.R;
import com.ms.moviesapp.callbacks.OnErrorListener;
import com.ms.moviesapp.callbacks.OnServiceErrorListener;
import com.ms.moviesapp.callbacks.OnServiceSuccessListener;
import com.ms.moviesapp.callbacks.OnSuccessListener;
import com.ms.moviesapp.entities.ErrorException;

/**
 * Created by Mohammad Sayed on 8/20/2015.
 */
public abstract class BaseApiModel implements OnServiceSuccessListener, OnServiceErrorListener {


    protected OnSuccessListener mOnSuccessListener;
    protected OnErrorListener mOnErrorListener;
    protected String mOperationTag;
    protected Context mContext;

    public BaseApiModel(Context context, String operationTag, OnSuccessListener onSuccessListener, OnErrorListener onErrorListener) {
        this.mContext = context;
        this.mOperationTag = operationTag;
        this.mOnSuccessListener = onSuccessListener;
        this.mOnErrorListener = onErrorListener;
    }

    @Override
    public void onServiceError(String dataOperation, String error) {
        switch (error) {
            case Constants.Errors.ERROR_URL:
                returnError(Constants.ErrorCodes.ERROR_URL, mContext.getString(R.string.error_url));
                break;
            case Constants.Errors.ERROR_CONNECTION:
                returnError(Constants.ErrorCodes.ERROR_CONNECTION, mContext.getString(R.string.error_connection));
                break;
            case Constants.Errors.ERROR_CLOSING_CONNECTION:
                returnError(Constants.ErrorCodes.ERROR_CLOSING_CONNECTION, mContext.getString(R.string.error_closing_connection));
                break;
            case Constants.Errors.ERROR_NO_DATA_RETRIEVED:
                returnError(Constants.ErrorCodes.ERROR_NO_DATA_RETRIEVED, mContext.getString(R.string.error_no_data_retrieved));
                break;
            case Constants.Errors.ERROR_FETCH_DATA:
                returnError(Constants.ErrorCodes.ERROR_FETCH_DATA, mContext.getString(R.string.error_fetch_data));
                break;
        }
    }

    private void returnError(int code, String message) {
        ErrorException error = new ErrorException(code, message, getStackTrace());
        mOnErrorListener.onError(mOperationTag, error);
    }

    private StackTraceElement[] getStackTrace() {
        return Thread.currentThread().getStackTrace();
    }

    protected OnSuccessListener getOnSuccessListener() {
        return mOnSuccessListener;
    }

    protected OnErrorListener getOnErrorListener() {
        return mOnErrorListener;
    }
}
