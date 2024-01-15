package top.cgglyle.boson.security.user.domain.entity

import top.cgglyle.boson.security.common.entity.basic.AbstractIDRepository

interface AccountRepository : AbstractIDRepository<Account> {
    fun findByUsername(username: String): Account?

    fun findByEmail(email: String): Account?

    fun existsByUsernameOrEmail(username: String?, email: String?): Boolean
}