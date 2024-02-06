package com.example.accountserver.social

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.time.Duration

@Component
class WebClientHelper(
    private val webClientBuilder: WebClient.Builder,

) {
    companion object {
        val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    fun buildWebClient(): WebClient {
        val httpClient: HttpClient = HttpClient.create().responseTimeout(Duration.ZERO)

        return webClientBuilder
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .build()
    }

    fun makeMultiValueMap(map: Map<String, String>): MultiValueMap<String, String> {
        return LinkedMultiValueMap(map.mapValues { listOf() })
    }
}