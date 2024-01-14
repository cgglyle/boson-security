package top.cgglyle.boson.security.auth.domain.command

import top.cgglyle.boson.security.domain.entity.RoleEntity

data class CreateAuthCommand(
    val username: String?,
    val password: String,
    val email: String?,
    val roles: Set<RoleEntity> = mutableSetOf(RoleEntity())
)