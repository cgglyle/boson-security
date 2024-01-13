package top.cgglyle.boson.security.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.stereotype.Service
import top.cgglyle.boson.security.domain.command.CreateClientCommand
import top.cgglyle.boson.security.domain.entity.ClientEntity
import top.cgglyle.boson.security.repository.ClientRepository

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