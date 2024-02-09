package org.djawnstj.store.auth.service

import org.djawnstj.store.auth.dto.signin.SignInResponse
import org.djawnstj.store.auth.dto.refresh.TokenRefreshRequest
import org.djawnstj.store.auth.dto.refresh.TokenRefreshResponse
import org.djawnstj.store.auth.dto.signin.SignInRequest
import org.djawnstj.store.auth.entity.AuthenticationCredentials
import org.djawnstj.store.auth.repository.TokenRepository
import org.djawnstj.store.common.exception.ErrorCode
import org.djawnstj.store.common.exception.GlobalException
import org.djawnstj.store.member.entity.Member
import org.djawnstj.store.member.repository.MemberQueryRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AuthenticationService(
    private val tokenRepository: TokenRepository,
    private val memberQueryRepository: MemberQueryRepository,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager,
) {

    fun signIn(request: SignInRequest): SignInResponse {
        val member: Member = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(request.phoneNumber, request.loginPassword)
        ).principal as Member

        val authenticationCredentials = jwtService.generateAuthenticationCredentials(member)

        tokenRepository.save(authenticationCredentials)

        return authenticationCredentials.run { SignInResponse(accessToken, refreshToken) }
    }

    fun refreshToken(request: TokenRefreshRequest): TokenRefreshResponse {
        val presentedRefreshToken = request.refreshToken

        val phoneNumber: String = jwtService.getUsername(presentedRefreshToken!!)
        val jti = jwtService.getJti(presentedRefreshToken)

        val member = memberQueryRepository.findByPhoneNumber(phoneNumber).isValidMember()

        val foundTokens = tokenRepository.findByJti(jti).isValidToken()

        validateRefreshToken(presentedRefreshToken, foundTokens.refreshToken, member)

        validateActiveAccessToken(foundTokens.accessToken, jti)

        val authenticationCredentials = jwtService.generateAuthenticationCredentials(member)

        tokenRepository.save(authenticationCredentials)

        return authenticationCredentials.run { TokenRefreshResponse(accessToken, refreshToken) }
    }

    private fun validateActiveAccessToken(accessToken: String, jti: String) {
        if (!jwtService.checkTokenExpiredByTokenString(accessToken)) {
            tokenRepository.deleteByJti(jti)
            throw GlobalException(ErrorCode.INVALID_TOKEN_REISSUE_REQUEST)
        }
    }

    private fun validateRefreshToken(jwt: String, refreshToken: String, user: UserDetails) {
        if (jwt != refreshToken) throw GlobalException(ErrorCode.MISS_MATCH_TOKEN)
        if (!jwtService.isTokenValid(jwt, user)) throw GlobalException(ErrorCode.INVALID_REFRESH_TOKEN)
    }

    private fun Member?.isValidMember(): Member = this ?: throw GlobalException(ErrorCode.MEMBER_NOT_FOUND)

    private fun AuthenticationCredentials?.isValidToken(): AuthenticationCredentials =
        this?.let { this } ?: throw throw GlobalException(ErrorCode.TOKEN_NOT_FOUND)

}



