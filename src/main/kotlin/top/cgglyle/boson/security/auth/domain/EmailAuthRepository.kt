package top.cgglyle.boson.security.auth.domain

import top.cgglyle.boson.security.auth.domain.entity.EmailAuthEntity
import top.cgglyle.boson.security.common.entity.basic.AbstractIDRepository

interface EmailAuthRepository : AbstractIDRepository<EmailAuthEntity> {
    fun findByAccountEmail(email: String): EmailAuthEntity?
}