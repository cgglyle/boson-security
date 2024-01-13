package top.cgglyle.security.boson.security.repository

import top.cgglyle.security.boson.security.common.entity.AbstractIDRepository
import top.cgglyle.security.boson.security.domain.entity.UserEntity

interface UserRepository : AbstractIDRepository<UserEntity> {
    fun findUserEntityByUsername(username: String): UserEntity?
}