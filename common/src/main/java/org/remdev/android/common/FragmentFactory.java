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

    public static void replace(FragmentActivity context, Fragment fragment, @IdRes int container, String tag) {
        context.getSupportFragmentManager().beginTransaction().replace(container, fragment, tag).commit();
    }

    public static void remove(FragmentActivity context, String tag) {
        Fragment fragment = context.getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment != null) {
            context.getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }
}
