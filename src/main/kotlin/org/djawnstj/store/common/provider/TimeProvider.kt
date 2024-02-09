package org.djawnstj.store.common.provider

interface TimeProvider {
    fun getCurrentTimeMillis(): Long

}