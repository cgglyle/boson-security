package top.cgglyle.security.boson.security.repository

import top.cgglyle.security.boson.security.common.AbstractIDRepository
import top.cgglyle.security.boson.security.domain.entity.ClientEntity

interface ClientRepository : AbstractIDRepository<ClientEntity> {
    fun findByClientId(clientId: String): ClientEntity?
}