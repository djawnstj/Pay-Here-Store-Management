package org.djawnstj.store.member.dto

import org.djawnstj.store.member.entity.MemberRole

data class MemberDto(
    val phoneNumber: String? = null,
    val name: String,
    val role: MemberRole = MemberRole.STAFF,
) {
}