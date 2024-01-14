package top.cgglyle.boson.security.auth.domain

import top.cgglyle.boson.security.auth.domain.entity.UsernameAuthEntity
import top.cgglyle.boson.security.common.entity.AbstractIDRepository

interface UsernameAuthRepository : AbstractIDRepository<UsernameAuthEntity> {
    fun findByAccountUsername(username: String): UsernameAuthEntity?
}