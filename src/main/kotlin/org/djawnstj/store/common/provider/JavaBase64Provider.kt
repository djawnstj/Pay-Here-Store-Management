package org.djawnstj.store.common.provider

import java.util.Base64

class JavaBase64Provider: Base64Provider {

    private val encoder = Base64.getEncoder()
    private val decoder = Base64.getDecoder()

    override fun decode(src: String): ByteArray = decoder.decode(src)

}