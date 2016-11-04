package com.ms.moviesapp.callbacks;

import com.ms.moviesapp.entities.ErrorException;

/**
 * Created by Mohammad-Sayed-PC on 11/3/2016.
 */

public interface OnErrorListener {
    void onError(String operationTag, ErrorException error);
}
