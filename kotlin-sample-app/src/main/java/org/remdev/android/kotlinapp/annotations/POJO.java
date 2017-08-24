package org.remdev.android.kotlinapp.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker.
 * <br>
 *     Just to show that object should be a Plain Old Java Object
 *     as depending on implementation it may be converted somehow
 *     (e.g. to JSON).
 */
@Retention(RetentionPolicy.SOURCE)
@Target({
        ElementType.TYPE,
        ElementType.FIELD,
        ElementType.PARAMETER
})
public @interface POJO {
}
