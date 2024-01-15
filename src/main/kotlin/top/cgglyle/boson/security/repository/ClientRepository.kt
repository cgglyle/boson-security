package top.cgglyle.boson.security.repository

import top.cgglyle.boson.security.common.entity.basic.AbstractIDRepository
import top.cgglyle.boson.security.domain.entity.ClientEntity

interface ClientRepository : AbstractIDRepository<ClientEntity> {
    fun findByClientId(clientId: String): ClientEntity?
}