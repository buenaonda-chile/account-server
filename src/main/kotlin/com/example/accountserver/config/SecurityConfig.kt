package com.example.accountserver.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.password.PasswordEncoder

import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.CorsUtils
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {
    @Bean
    fun filterChain(
        http: ServerHttpSecurity
    ): SecurityWebFilterChain {
        http
            .httpBasic { httpConfig -> httpConfig.disable() }
            .csrf { csrfConfig -> csrfConfig.disable() }
            .headers { headerConfig -> headerConfig.frameOptions { frameOptionConfig -> frameOptionConfig.disable() } }

        http.authorizeExchange { request ->
            request.pathMatchers("/health_check").permitAll()
                .pathMatchers(HttpMethod.POST, "/v1/users").permitAll()
                .pathMatchers("/v1/**").authenticated()
        }

        return http.build();
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return passwordEncoder()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val corsConfiguration = CorsConfiguration()

        corsConfiguration.setAllowedOriginPatterns(listOf("*"))
        corsConfiguration.allowedHeaders = listOf("*")
        corsConfiguration.allowedMethods = listOf("*")
        corsConfiguration.exposedHeaders = listOf("*")
        corsConfiguration.allowCredentials = true

        val source = UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", corsConfiguration)
        }

        return source
    }
}