package com.example.accountserver.social

import com.example.accountserver.domain.enum.SocialProvider
import com.example.accountserver.`interface`.auth.OAuth2RequestWithAuthCode
import com.fasterxml.jackson.annotation.JsonProperty
import com.nimbusds.jwt.SignedJWT
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.*

@Component("APPLE")
class AppleClient(
    private val webClientHelper: WebClientHelper,
    clientRegistrationRepository: ReactiveClientRegistrationRepository,
    @Value("\${spring.security.oauth2.client.registration.apple.key-id}") private val keyId: String,
    @Value("\${spring.security.oauth2.client.registration.apple.private-key}") privateKeyString: String,
    @Value("\${spring.security.oauth2.client.registration.apple.team-id}") private val teamId: String,
) : OAuth2Client {

    private val webClient = webClientHelper.buildWebClient()
    private val clientRegistration = clientRegistrationRepository.findByRegistrationId(
        SocialProvider.APPLE.value
    ).block()!!

    private val decoder = Base64.getDecoder()
    private val factory = KeyFactory.getInstance("EC")
    private val privateKey = factory.generatePrivate(PKCS8EncodedKeySpec(decoder.decode(privateKeyString)))

    override suspend fun getMeWithAuthCode(oAuth2RequestWithAuthCode: OAuth2RequestWithAuthCode): OAuth2Response? {
        return webClient
            .post()
            .uri(clientRegistration.providerDetails.tokenUri)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .bodyValue(
                webClientHelper.makeMultiValueMap(
                    mapOf(
                        "grant_type" to "authorization_code",
                        "client_id" to clientRegistration.clientId,
                        "client_secret" to buildJwt(),
                        "redirect_uri" to oAuth2RequestWithAuthCode.redirectUri,
                        "code" to oAuth2RequestWithAuthCode.authorizationCode
                    )
                )
            )
            .retrieve()
            .bodyToMono<AppleOAuth2TokenResponse>()
            .onErrorResume {
                WebClientHelper.log.error(it.message, it)
                Mono.empty()
            }.awaitSingleOrNull()
            ?.let {
                getMe(it.accessToken)
            }
    }

    override suspend fun getMe(token: String): OAuth2Response? {
        return SignedJWT.parse(token).jwtClaimsSet.getStringListClaim("email").firstOrNull()?.let {
            OAuth2Response(
                socialId = it,
                email = it,
            )
        }
    }

    private fun buildJwt(): String {
        return Jwts.builder()
            .setHeaderParam("kid", keyId)
            .setIssuer(teamId)
            .setSubject(clientRegistration.clientId)
            .setAudience("https://appleid.apple.com")
            .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
            .setExpiration(Timestamp.valueOf(LocalDateTime.now().plusDays(1)))
            .signWith(privateKey, SignatureAlgorithm.ES256)
            .compact()
    }

    data class AppleOAuth2TokenResponse(
        @JsonProperty("token_type")
        val tokenType: String,
        @JsonProperty("access_token")
        val accessToken: String,
        @JsonProperty("expires_in")
        val expiresIn: Int,
        @JsonProperty("refresh_token")
        val refreshToken: String,
        @JsonProperty("id_token")
        val idToken: String,
        @JsonProperty("user")
        val user: AppleOAuth2Response,
    )
}