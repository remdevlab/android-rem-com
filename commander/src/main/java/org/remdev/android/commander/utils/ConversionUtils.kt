package org.remdev.android.commander.utils

import com.google.gson.Gson

import java.lang.reflect.Array
import java.util.Arrays
import java.util.Collections

/**
 * Used for converting Java objects to Json and vice versa
 * without necessity to create new [Gson] instance
 */
class ConversionUtils {

    companion object {
        private val gson = Gson()

        fun <T> fromJson(source: String?, target: Class<T>?): T?
                = gson.fromJson(source, target)

        inline fun <reified T> fromJson(source: String?): T?
                = fromJson(source, T::class.java)

        @Suppress("UNCHECKED_CAST")
        fun <T> fromJsonToList(source: String, target: Class<T>): List<T> {
            val array: kotlin.Array<T> = Array.newInstance(target, 0) as kotlin.Array<T>
            val items = gson.fromJson(source, array.javaClass)
            return items?.let { Arrays.asList(*it) } ?: emptyList()
        }

        fun <T> toJson(source: T): String = gson.toJson(source)

        /**
         * Tries to parse the given string to int or returns the alternative
         * @param str the target string to parse
         * @param alt the alternative in case the string can no be parsed
         * @return parsed value if success and alt otherwise
         */
        fun parseInt(str: String, alt: Int): Int {
            try {
                return str.toInt()
            } catch (e: Throwable) {
                return alt
            }

        }

        fun getClass(obj: Any): Class<*> = obj.javaClass
    }
}
