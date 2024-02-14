package com.example.accountserver.social

import com.example.accountserver.domain.enum.SocialProvider
import com.example.accountserver.`interface`.auth.OAuth2RequestWithAuthCode
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.http.MediaType
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

@Component("GITHUB")
class GithubClient(
    private val webClientHelper: WebClientHelper,
    clientRegistrationRepository: ReactiveClientRegistrationRepository
): OAuth2Client {

    private val webClient = webClientHelper.buildWebClient()
    private val clientRegistration = clientRegistrationRepository.findByRegistrationId(
        SocialProvider.GITHUB.value
    ).block()!!

    override suspend fun getMeWithAuthCode(oAuth2RequestWithAuthCode: OAuth2RequestWithAuthCode): OAuth2Response? {
        return webClient
            .post()
            .uri(clientRegistration.providerDetails.tokenUri)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .bodyValue(
                webClientHelper.makeMultiValueMap(
                    mapOf(
                        "client_id" to clientRegistration.clientId,
                        "client_secret" to clientRegistration.clientSecret,
                        "code" to oAuth2RequestWithAuthCode.authorizationCode,
                        "redirect_uri" to oAuth2RequestWithAuthCode.redirectUri,
                    )
                )
            )
            .retrieve()
            .bodyToMono<GithubOAuth2TokenResponse>()
            .onErrorResume {
                WebClientHelper.log.error(it.message, it)
                Mono.empty()
            }.awaitSingleOrNull()
            ?.let {
                getMe(it.accessToken)
            }
    }

    override suspend fun getMe(token: String): OAuth2Response? {
        return webClient
            .get()
            .uri(clientRegistration.providerDetails.userInfoEndpoint.uri)
            .headers {
                it.setBearerAuth(token)
            }.retrieve()
            .bodyToMono<GithubOAuth2Response>()
            .onErrorResume {
                WebClientHelper.log.error(it.message, it)
                Mono.empty()
            }
            .map {
                OAuth2Response(it.id.toString(), it.email)
            }
            .awaitSingleOrNull()
    }

    data class GithubOAuth2TokenResponse(
        @JsonProperty("access_token")
        val accessToken: String,
        @JsonProperty("scope")
        val scope: String,
        @JsonProperty("token_type")
        val tokenType: String,
    )
}