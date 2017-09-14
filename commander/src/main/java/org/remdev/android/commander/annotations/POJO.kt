package org.remdev.android.commander.annotations


/**
 * Marker.
 * <br></br>
 * Just to show that object should be a Plain Old Java Object
 * as depending on implementation it may be converted somehow
 * (e.g. to JSON).
 */
@Retention(AnnotationRetention.SOURCE)
@Target(
        AnnotationTarget.CLASS,
        AnnotationTarget.FILE,
        AnnotationTarget.FIELD,
        AnnotationTarget.VALUE_PARAMETER
)
annotation class POJO
