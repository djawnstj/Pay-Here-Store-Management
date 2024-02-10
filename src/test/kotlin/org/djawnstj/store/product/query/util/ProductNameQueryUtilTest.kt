package org.djawnstj.store.product.query.util

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ProductNameQueryUtilTest {

    private val productNameQueryUtil = ProductNameQueryUtil()

    @Test
    fun `주어진 문자열에 한글이 포함되었는지 확인한다`() {
        // given
        val query1 = "choco latte"
        val query2 = "녹차 latte"

        // when
        val result1 = productNameQueryUtil.hasKoreanInitials(query1)
        val result2 = productNameQueryUtil.hasKoreanInitials(query2)

        // then
        assertThat(result1).isFalse()
        assertThat(result2).isTrue()
    }

    @Test
    fun `주어진 문자열에서 한글은 초성으로 변환하여 반환한다`() {
        // given
        val query = "녹차 latte"

        // when
        val result = productNameQueryUtil.extractKoreanInitialsQuery(query)

        // then
        assertThat(result).isEqualTo("ㄴㅊ latte")
    }

    @Test
    fun `주어진 문자열이 한글 초성으로만 이루어져 있는지 확인한다`() {
        // given
        val query1 = "ㄴㅊ"
        val query2 = "녹ㅊ"
        val query3 = "cho코"

        // when
        val result1 = productNameQueryUtil.isOnlyKoreanInitials(query1)
        val result2 = productNameQueryUtil.isOnlyKoreanInitials(query2)
        val result3 = productNameQueryUtil.isOnlyKoreanInitials(query3)

        // then
        assertThat(result1).isTrue()
        assertThat(result2).isFalse()
        assertThat(result3).isFalse()
    }

}