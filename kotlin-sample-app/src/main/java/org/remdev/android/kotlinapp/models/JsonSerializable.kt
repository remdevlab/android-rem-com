package org.remdev.android.kotlinapp.models

interface JsonSerializable<out T> {

    /**
     * Converts object to json
     * @return JSON representation of the object
     */
    fun toJson(): String?

    /**
     * Converts object from json string
     * @return converted object
     * *
     * @param json the source JSON to convert
     */
    fun fromJson(json: String?): T
}
