package top.cgglyle.boson.security.repository

import top.cgglyle.boson.security.common.entity.AbstractIDRepository
import top.cgglyle.boson.security.domain.entity.UserEntity

interface UserRepository : AbstractIDRepository<UserEntity> {
    fun findUserEntityByUsername(username: String): UserEntity?
}