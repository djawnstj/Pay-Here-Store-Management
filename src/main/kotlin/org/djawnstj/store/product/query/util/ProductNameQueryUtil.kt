package org.djawnstj.store.product.query.util

import org.springframework.stereotype.Component

@Component
class ProductNameQueryUtil {

    companion object {
        private val KOREAN_INITIALS = arrayOf('ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ')
    }

    fun hasKoreanInitials(query: String): Boolean = query.any { isKoreanChar(it) }

    fun extractKoreanInitialsQuery(query: String): String {
        val sb = StringBuilder()

        query.forEach { ch ->
            if (isKoreanChar(ch)) {
                val index = (ch - '\uAC00') / (21 * 28)
                sb.append(KOREAN_INITIALS[index])
            } else {
                sb.append(ch)
            }
        }

        return sb.toString()
    }

    private fun isKoreanChar(ch: Char) = ch in '\uAC00'..'\uD7A3'

    fun isOnlyKoreanInitials(query: String): Boolean = query.all { it in KOREAN_INITIALS }

}