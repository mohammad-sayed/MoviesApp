package com.ms.moviesapp.bases;

import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * Created by Mohammad-Sayed-PC on 11/3/2016.
 */

public abstract class BaseFragment extends Fragment {

    protected <T> T castInterface(Class<T> interfaceClass, Object object) {
        T interfaceObject = null;
        try {
            interfaceObject = (T) object;
        } catch (ClassCastException ex) {
            throw new ClassCastException(object.getClass().getSimpleName() + " must implement " + interfaceClass.getClass().getSimpleName());
        }
        return interfaceObject;
    }

    protected void toastForLong(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    protected void toastForLong(int stringResId) {
        Toast.makeText(getContext(), stringResId, Toast.LENGTH_LONG).show();
    }
}
