package org.djawnstj.store.common.exception

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging

class GlobalException @JvmOverloads constructor(
    val errorCode: ErrorCode,
    message: String? = null,
    override val cause: Throwable? = null,
): RuntimeException(message, cause) {

    private val log: KLogger = KotlinLogging.logger {  }

    override val message: String = message ?: errorCode.message

    init {
        log.error {
            """
                server error
                cause: $cause
                message: ${message ?: errorCode.message}
                errorCode: $errorCode
            """.trimIndent()
        }
    }

}