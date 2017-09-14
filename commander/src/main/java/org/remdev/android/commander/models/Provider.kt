package org.remdev.android.commander.models

interface Provider<out T> {

    /**
     * Provides the item instance
     */
    fun get(): T
}
