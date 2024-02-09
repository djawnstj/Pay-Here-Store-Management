package org.djawnstj.store.common.provider

interface Base64Provider {

    fun decode(src: String): ByteArray

}
