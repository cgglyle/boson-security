package top.cgglyle.security.boson.security.config

import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.core.oidc.OidcScopes
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.NoSuchAlgorithmException
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.*

@Configuration
class AuthorizationSecurityConfig {
    @Bean
    @Order(1)
    @Throws(Exception::class)
    fun authSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http)
        http.getConfigurer(OAuth2AuthorizationServerConfigurer::class.java)
            .oidc(Customizer.withDefaults())
        http.exceptionHandling {
            it.authenticationEntryPoint(LoginUrlAuthenticationEntryPoint("/login"))
        }.oauth2ResourceServer { it.jwt(Customizer.withDefaults()) }
        return http.build()
    }

    @Bean
    @Order(2)
    @Throws(Exception::class)
    fun webSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests {
            it.requestMatchers("/auth/**").permitAll()
            it.anyRequest().authenticated()
        }.formLogin(Customizer.withDefaults())
        http.csrf {
            it.ignoringRequestMatchers("/auth/**")
        }
        return http.build()
    }


    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }

    @Bean
    fun registeredClientRepository(): RegisteredClientRepository {
        val registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId("client")
            .clientSecret(passwordEncoder().encode("secret"))
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
            .redirectUri("https://oauthdebugger.com/debug")
            .scope(OidcScopes.OPENID)
            .clientSettings(clientSettings())
            .build()
        return InMemoryRegisteredClientRepository(registeredClient)
    }

    @Bean
    fun clientSettings(): ClientSettings {
        return ClientSettings.builder().requireProofKey(true).build()
    }

    @Bean
    fun authorizationServerSettings(): AuthorizationServerSettings {
        return AuthorizationServerSettings.builder().issuer("http://localhost:9001").build()
    }

    @Bean
    fun jwtDecoder(jwkSource: JWKSource<SecurityContext>): JwtDecoder {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource)
    }

    @Bean
    fun jwkSource(): JWKSource<SecurityContext> {
        val rsaKey = generateRSAKey()
        val jwkSet = JWKSet(rsaKey)
        return JWKSource { jwkSelector, _: SecurityContext ->
            jwkSelector.select(
                jwkSet
            )
        }
    }

    private fun generateRSAKey(): RSAKey {
        val keyPair = generateKeyPair()
        val publicKey = keyPair.public as RSAPublicKey
        val privateKey = keyPair.private as RSAPrivateKey
        return RSAKey.Builder(publicKey).privateKey(privateKey).keyID(UUID.randomUUID().toString()).build()
    }

    private fun generateKeyPair(): KeyPair {
        val keyPair: KeyPair
        try {
            val generator = KeyPairGenerator.getInstance("RSA")
            generator.initialize(2048)
            keyPair = generator.generateKeyPair()
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e.message)
        }
        return keyPair
    }
}