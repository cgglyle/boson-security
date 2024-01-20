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

package top.cgglyle.boson.security.client.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.stereotype.Service
import top.cgglyle.boson.security.client.CreateClientCommand
import top.cgglyle.boson.security.client.domain.ClientEntity
import top.cgglyle.boson.security.client.domain.ClientRepository

@Service
class ClientService(
    private val clientRepository: ClientRepository,
    private val passwordEncoder: PasswordEncoder,
) : RegisteredClientRepository {
    fun clientForm(command: CreateClientCommand): ClientEntity {
        val encodeSecretCommand = command.copy(clientSecret = passwordEncoder.encode(command.clientSecret))
        val clientEntity = ClientEntity(encodeSecretCommand)
        return clientEntity
    }

    fun save(command: CreateClientCommand) {
        val clientEntity = clientForm(command)
        clientRepository.save(clientEntity)
    }

    override fun save(registeredClient: RegisteredClient?) {
    }

    override fun findById(id: String?): RegisteredClient? {
        return if (id != null) {
            clientRepository.findByClientId(id)?.toRegisteredClient()
        } else null
    }

    override fun findByClientId(clientId: String?): RegisteredClient? {
        return if (clientId != null) {
            clientRepository.findByClientId(clientId)?.toRegisteredClient()
        } else null
    }
}