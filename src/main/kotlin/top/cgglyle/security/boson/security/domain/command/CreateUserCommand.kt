package top.cgglyle.security.boson.security.domain.command

import top.cgglyle.security.boson.security.domain.entity.RoleEntity
import java.io.Serializable

/**
 * DTO for {@link top.cgglyle.security.boson.security.domain.entity.UserEntity}
 */
data class CreateUserCommand(
    val username: String = "",
    val password: String = "",
    val roles: Set<RoleEntity>?
) : Serializable