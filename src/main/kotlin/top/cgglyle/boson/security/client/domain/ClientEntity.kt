/*
 * Copyright 2024 Lyle Liu<cgglyle@outlook.com> and all contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.cgglyle.boson.security.client.domain

import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Table
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings
import top.cgglyle.boson.security.client.CreateClientCommand
import top.cgglyle.boson.security.common.AbstractIDEntity
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

    constructor(command: CreateClientCommand) : this() {
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