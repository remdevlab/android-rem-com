package org.remdev.android.common.utils;

import java.util.concurrent.Callable;

/**
 * Util class for wrapping nullable objects. Similar to Java 8's Optional
 * class, but with very limited functionality
 * @param <T> the type of wrapped object
 */
public class Optional<T> {

    private final T value;

    private Optional(T val) {
        this.value = val;
    }

    /**
     * Get wrapped value
     * @return wrapped value. Can be {@code null} if wrapped object is null
     */
    public T get() {
        return value;
    }

    /**
     * Allows to get either wrapped value if it's not null or
     * alternative object passed as a param
     * @param alt the alternative object
     * @return wrapped value if it's not null and {@code alt} otherwise
     */
    public T orElseGet(T alt) {
        return value == null ? alt : value;
    }

    /**
     * Allows to get either wrapped value if it's not null or
     * execute the {@link Callable<T>} action and return its result
     * @param alt the alternative action to execute
     * @return wrapped value if it's not null and the result
     *          of invocation of {@code alt} action otherwise
     */
    public T orElse(Callable<T> alt) {
        if (value != null) {
            return value;
        }
        try {
            return alt.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return value == null ? null : value.toString();
    }

    /**
     * Creates instance of {@link Optional} wrapping provided value
     * @param val the value to be wrapped
     * @return created instance
     */
    public static <T> Optional<T> of(T val) {
        return new Optional<>(val);
    }

    /**
     * Creates instance of {@link Optional} wrapping {@code null} object
     * @return created instance
     */
    public static <T> Optional<T> empty() {
        return new Optional<T>(null);
    }

    /**
     * Maps wrapped value if it's not null applying the given function
     * @param f the transformation function
     * @return {@code null} if the wrapped value is null and result of applying
     * transformation function otherwise
     */
    public <R> R map(Function<T, R> f) {
        return value == null ? null : f.apply(value);
    }

    public interface Function<T, R> {

        /**
         * Applies this function to the given argument.
         *
         * @param t the function argument
         * @return the function result
         */
        R apply(T t);
    }
}
