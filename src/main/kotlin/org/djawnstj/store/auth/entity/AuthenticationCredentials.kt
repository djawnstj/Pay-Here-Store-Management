package org.djawnstj.store.auth.entity

import jakarta.persistence.*

@Entity
@Table(
    name = "authentication_credentials",
    uniqueConstraints = [UniqueConstraint(name = "uk_authentication_credentials_jti", columnNames = ["jti"])]
)
class AuthenticationCredentials(
    @field:Column(name = "jti", nullable = false)
    var jti: String,
    @field:Column(name = "access_token", nullable = false)
    var accessToken: String,
    @field:Column(name = "refresh_token", nullable = false)
    var refreshToken: String,
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
)