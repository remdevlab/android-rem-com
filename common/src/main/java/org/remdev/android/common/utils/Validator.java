package org.remdev.android.common.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Alexandr.Salin on 1/17/16.
 */
public class Validator {
    /**
     * {@see java.util.Objects#requireNonNull(Object)}
     */
    @NonNull
    public static <T> T requireNonNull(@Nullable T o) {
        if (o == null) {
            throw new NullPointerException();
        }
        return o;
    }

    /**
     * {@see java.util.Objects#requireNonNull(Object, String)}
     */
    @NonNull
    public static <T> T requireNonNull(@Nullable T o, String message) {
        if (o == null) {
            throw new NullPointerException(message);
        }
        return o;
    }
}
