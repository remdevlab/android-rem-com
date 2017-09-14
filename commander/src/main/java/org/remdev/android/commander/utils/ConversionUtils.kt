package org.remdev.android.commander.utils

import com.google.gson.Gson
import java.lang.reflect.Array
import java.util.*

/**
 * Used for converting Java objects to Json and vice versa
 * without necessity to create new [Gson] instance
 */
private val gson = Gson()

fun <T> fromJson(source: String?, target: Class<T>?): T? = gson.fromJson(source, target)

@Suppress("UNCHECKED_CAST")
fun <T> fromJsonToList(source: String, target: Class<T>): List<T> {
    val array: kotlin.Array<T> = Array.newInstance(target, 0) as kotlin.Array<T>
    val items = gson.fromJson(source, array.javaClass)
    return items?.let { Arrays.asList(*it) } ?: emptyList()
}

fun getClass(obj: Any): Class<*> = obj.javaClass

fun <T> toJson(source: T): String = gson.toJson(source)