package org.remdev.android.common;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * Created by Alexandr.Salin on 1/13/16.
 */
public class FragmentFactory {

    public static void showIn(FragmentActivity context, Fragment fragment, @IdRes int idRes) {
        context.getSupportFragmentManager().beginTransaction().replace(idRes, fragment).commit();
    }
}
