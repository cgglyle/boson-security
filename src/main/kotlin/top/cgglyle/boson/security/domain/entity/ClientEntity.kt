package top.cgglyle.boson.security.domain.entity

import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Table
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings
import top.cgglyle.boson.security.common.entity.AbstractIDEntity
import top.cgglyle.boson.security.domain.command.CreateClientCommand
import java.util.*


@Entity
@Table(name = "auth_client")
class ClientEntity() : AbstractIDEntity() {
    private lateinit var clientId: String
    private lateinit var clientSecret: String

    @ElementCollection(fetch = FetchType.EAGER)
    private val authenticationMethods: MutableSet<ClientAuthenticationMethod> = mutableSetOf()

    @ElementCollection(fetch = FetchType.EAGER)
    private val authorizationGrantTypes: MutableSet<AuthorizationGrantType> = mutableSetOf()

    @ElementCollection(fetch = FetchType.EAGER)
    private val redirectUris: MutableSet<String> = mutableSetOf()

    @ElementCollection(fetch = FetchType.EAGER)
    private val scopes: MutableSet<String> = mutableSetOf()

    private var requireProofKey: Boolean = false

    constructor(command: CreateClientCommand): this() {
        this.clientId = command.clientId
        this.clientSecret = command.clientSecret
        this.authenticationMethods.addAll(command.authenticationMethods)
        this.authorizationGrantTypes.addAll(command.authorizationGrantTypes)
        this.redirectUris.addAll(command.redirectUris)
        this.scopes.addAll(command.scopes)
        this.requireProofKey = command.requireProofKey
    }


    fun toRegisteredClient(): RegisteredClient {
        return RegisteredClient.withId(clientId)
            .clientId(clientId)
            .clientSecret(clientSecret)
            .clientIdIssuedAt(Date().toInstant())
            .clientAuthenticationMethods {
                it.addAll(authenticationMethods)
            }
            .authorizationGrantTypes {
                it.addAll(authorizationGrantTypes)
            }
            .redirectUris {
                it.addAll(redirectUris)
            }
            .scopes {
                it.addAll(scopes)
            }
            .clientSettings(
                ClientSettings.builder()
                    .requireProofKey(requireProofKey)
                    .build()
            )
            .build()
    }
}