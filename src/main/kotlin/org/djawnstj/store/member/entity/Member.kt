package org.djawnstj.store.member.entity

import jakarta.persistence.*
import org.djawnstj.store.common.entity.BaseEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(
    name = "member",
    uniqueConstraints = [UniqueConstraint(name = "uk_member_phone_number", columnNames = ["phone_number"])]
)
class Member(
    @field:Column(name = "phone_number")
    val phoneNumber: String?,
    @field:Column(name = "login_password", nullable = false)
    val loginPassword: String,
    @field:Column(name = "name", nullable = false)
    val name: String,
    @field:Column(name = "role", nullable = false)
    @field:Enumerated(EnumType.STRING)
    val role: MemberRole = MemberRole.STAFF
): BaseEntity(), UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> = listOf(SimpleGrantedAuthority(this.role.name))

    override fun getPassword(): String = this.loginPassword

    override fun getUsername(): String? = this.phoneNumber

    override fun isAccountNonExpired(): Boolean = this.deletedAt == null

    override fun isAccountNonLocked(): Boolean = this.deletedAt == null

    override fun isCredentialsNonExpired(): Boolean = this.deletedAt == null

    override fun isEnabled(): Boolean = this.deletedAt == null

}