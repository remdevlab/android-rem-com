package org.remdev.android.commander.models

interface JsonSerializable<out T> {

    /**
     * Converts object to json
     * @return JSON representation of the object
     */
    fun toJson(): String
}
