package org.remdev.android.common.utils;

import android.support.annotation.NonNull;

public class StringUtils {

    /**
     * Checks whether the string is null or empty.
     * @param str the string to be checked
     * @return {@code true} if the given string is null or empty and {@code false} otherwise
     */
    public static boolean isBlank(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * Checks whether the string is not null and not empty.
     * @param str the string to be checked
     * @return {@code false} if the given string is null or empty and {@code true} otherwise
     */
    public static boolean isNotBlank(String str) {
        return str != null && str.length() > 0;
    }

    /**
     * Tests whether two strings are equal considering null string as empty one ("")
     * @param first the first string to test
     * @param second the seconds string to test
     * @return {@code true} if the given strings both blank or equal and {@code false} otherwise
     */
    public static boolean equalsOrBlank(String first, String second) {
        if (isBlank(first) && isBlank(second)) {
            return true;
        }
        first = isBlank(first) ? "" : first;
        second = isBlank(second) ? "" : second;
        return first.equals(second);
    }

    /**
     * Tests whether two strings are equal considering null string as empty one ("")
     * @param first the first string to test
     * @param second the seconds string to test
     * @return {@code true} if the given strings both blank or equal ignoring case and {@code false} otherwise
     */
    public static boolean equalsOrBlankIgnoreCase(String first, String second) {
        if (isBlank(first) && isBlank(second)) {
            return true;
        }
        first = isBlank(first) ? "" : first;
        second = isBlank(second) ? "" : second;
        return first.equalsIgnoreCase(second);
    }

    /**
     * Creates the string with the given length based on the given string filling empty spaces
     * with the given character
     * @param target the base string to adjust
     * @param length the target length
     * @param padSym the symbol to be used for padding
     * @return updated string
     * @see StringUtils#rightPad(String, int, char)
     */
    @NonNull
    public static String leftPad(@NonNull String target, int length, char padSym) {
        if (target.length() >= length) {
            return target;
        }
        StringBuilder builder = new StringBuilder(length);
        builder.append(target);
        while (builder.length() < length) {
            builder.insert(0, padSym);
        }
        return builder.toString();
    }

    /**
     * Creates the string with the given length based on the given string filling empty spaces
     * with the given character
     * @param target the base string to adjust
     * @param length the target length
     * @param padSym the symbol to be used for padding
     * @return updated string
     * @see StringUtils#leftPad(String, int, char)
     */
    @NonNull
    public static String rightPad(@NonNull String target, int length, char padSym) {
        if (target.length() >= length) {
            return target;
        }
        StringBuilder builder = new StringBuilder(length);
        builder.append(target);
        while (builder.length() < length) {
            builder.append(padSym);
        }
        return builder.toString();
    }

    /**
     * Joins the strings from the given array to a single string
     * using provided separator
     * @param separator the separator to be used as a delimiter
     * @param words the array of words to join
     * @return joined string
     * @see StringUtils#join(String, Iterable)
     */
    @NonNull
    public static String join(@NonNull String separator, @NonNull String[] words) {
        StringBuilder joined = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (i > 0) {
                joined.append(separator);
            }
            joined.append(String.valueOf(word));
        }
        return joined.toString();
    }

    /**
     * Joins the strings from the given iterable to a single string
     * using provided separator
     * @param separator the separator to be used as a delimiter
     * @param words the array of words to join
     * @return joined string
     * @see StringUtils#join(String, String[])
     */
    @NonNull
    public static String join(@NonNull String separator, @NonNull Iterable<String> words) {
        return joinObjects(separator, words);
    }

    /**
     * Joins the strings from the given iterable to a single string
     * using provided separator
     * @param separator the separator to be used as a delimiter
     * @param words the array of words to join
     * @return joined string
     * @see StringUtils#join(String, String[])
     * @see StringUtils#join(String, Iterable)
     */
    @NonNull
    public static String joinObjects(@NonNull String separator, @NonNull Iterable<?> words) {
        StringBuilder joined = new StringBuilder();
        int index = 0;
        for (Object word : words) {
            if (index++ > 0) {
                joined.append(separator);
            }
            joined.append(word);
        }
        return joined.toString();
    }
}
