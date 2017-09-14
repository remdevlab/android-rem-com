package org.remdev.android.common.utils;

import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * Used for converting Java objects to Json and vice versa
 * without necessity to create new {@link Gson} instance
 */
public class ConversionUtils {

    private static final Gson gson = new Gson();

    /**
     * Tryis to convert the given json to java object
     * @param json the JSON string for conversion
     * @param target the target class to convert
     * @return {@code null} if json is null and converted object otherwise
     */
    public static <T> T fromJson(String json, Class<T> target) {
        return gson.fromJson(json, target);
    }

    /**
     * Tryis to convert the given json to {@link List} java objects of type {@link T}
     * @param json the JSON string for conversion
     * @param target the target class to convert
     * @return {@code null} if json is null and list of converted object otherwise
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> fromJsonToList(String json, Class<T> target) {
        T[] array = (T[]) Array.newInstance(target, 0);
        T[] items = (T[]) gson.fromJson(json, array.getClass());
        return items == null ? Collections.<T>emptyList() : Arrays.asList(items);
    }

    /**
     * Converts the given Java object to JSON string
     * @param source the object to be converted
     * @return {@code null} if the given object is null and JSON string otherwise
     */
    public static<T> String toJson(T source) {
        return gson.toJson(source);
    }

    /**
     * Tries to parse the given string to int or returns the alternative
     * @param str the target string to parse
     * @param alt the alternative in case the string can no be parsed
     * @return parsed value if success and alt otherwise
     */
    public static int parseInt(String str, int alt) {
        try {
            return Integer.parseInt(str);
        } catch (Throwable e) {
            return alt;
        }
    }

    /**
     * Tries to parse the given string to double or returns the alternative
     * @param str the target string to parse
     * @param alt the alternative in case the string can no be parsed
     * @return parsed value if success and alt otherwise
     */
    public static double parseDouble(String str, double alt) {
        try {
            return Double.parseDouble(str);
        } catch (Throwable e) {
            return alt;
        }
    }
}
