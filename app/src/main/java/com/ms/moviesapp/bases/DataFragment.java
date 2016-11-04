package com.ms.moviesapp.bases;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ms.moviesapp.Constants;
import com.ms.moviesapp.R;
import com.ms.moviesapp.callbacks.OnErrorListener;
import com.ms.moviesapp.callbacks.OnSuccessListener;
import com.ms.moviesapp.controllerlayer.SyncAdapterResultReceiver;
import com.ms.moviesapp.servicelayer.database.MovieContract;

import java.util.Map;

import static android.content.Context.ACCOUNT_SERVICE;

/**
 * Created by Mohammad-Sayed-PC on 11/3/2016.
 */

public abstract class DataFragment extends BaseFragment implements OnSuccessListener, OnErrorListener {

    // Account
    public static final String ACCOUNT = "default_account";


    private BroadcastReceiver mSyncAdapterResultReceiver;
    private IntentFilter mSyncAdapterResultIntentFilter;
    private Account mAccount;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSyncAdapterResultReceiver = new SyncAdapterResultReceiver(this, this);
        mSyncAdapterResultIntentFilter = new IntentFilter(Constants.BroadcastReceiversActions.SYNC_RESULT_ACTION);
        mAccount = CreateSyncAccount(getContext());
    }

    protected abstract String getUrl(Object... objects);

    protected abstract Map<String, String> getParameters(String... strings);

    protected void fetch(String operationTag, String url, Map<String, String> params) {
        Uri uri = getFullUri(url, params);
        //new FetchDataAsyncTask(getContext(), operationTag, this, this).execute(uri.toString());

        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        settingsBundle.putString(Constants.Extras.OPERATION_TAG, operationTag);
        settingsBundle.putString(Constants.Extras.URI, uri.toString());
        /*
         * Request the sync for the default account, authority, and
         * manual sync settings
         */
        ContentResolver.requestSync(mAccount, MovieContract.CONTENT_AUTHORITY, settingsBundle);
    }

    private Uri getFullUri(String url, Map<String, String> params) {
        Uri.Builder builder = Uri.parse(url).buildUpon();
        for (String key : params.keySet()) {
            builder.appendQueryParameter(key, params.get(key));
        }
        return builder.build();
    }

    @Override
    public void onStart() {
        super.onStart();
        registerReceivers();
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterReceivers();
    }

    private void registerReceivers() {
        if (getContext() == null)
            return;
        try {
            getContext().registerReceiver(mSyncAdapterResultReceiver, mSyncAdapterResultIntentFilter);
        } catch (Exception ex) {
            Log.e("Receiver", "Already Registered");
        }
    }

    private void unregisterReceivers() {
        if (getContext() == null)
            return;
        try {
            getContext().unregisterReceiver(mSyncAdapterResultReceiver);
        } catch (Exception ex) {
            Log.e("Receiver", "Not Registered");
        }
    }

    /**
     * Create a new dummy account for the sync adapter
     *
     * @param context The application context
     */
    public static Account CreateSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(
                ACCOUNT, context.getString(R.string.account_type));
        // Get an instance of the Android account manager
        AccountManager accountManager = (AccountManager) context.getSystemService(ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
        }
        return newAccount;
    }

}
