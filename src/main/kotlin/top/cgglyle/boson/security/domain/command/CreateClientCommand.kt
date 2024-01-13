package top.cgglyle.boson.security.domain.command

import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import java.io.Serializable

/**
 * DTO for {@link top.cgglyle.boson.security.domain.entity.ClientEntity}
 */
data class CreateClientCommand(
    val clientId: String = "",
    val clientSecret: String = "",
    val authenticationMethods: MutableSet<ClientAuthenticationMethod> = mutableSetOf(),
    val authorizationGrantTypes: MutableSet<AuthorizationGrantType> = mutableSetOf(),
    val redirectUris: MutableSet<String> = mutableSetOf(),
    val scopes: MutableSet<String> = mutableSetOf(),
    val requireProofKey: Boolean = false
) : Serializable