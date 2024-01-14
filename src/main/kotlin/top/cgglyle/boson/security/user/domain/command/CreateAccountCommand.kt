package top.cgglyle.boson.security.user.domain.command

import top.cgglyle.boson.security.domain.entity.RoleEntity
import java.io.Serializable

/**
 * DTO for {@link top.cgglyle.boson.security.domain.entity.auth.UserEntity}
 */
data class CreateAccountCommand(
    val username: String?,
    val email: String?,
    val roles: Set<RoleEntity>,
    val isAccountNonLocked: Boolean = true,
    val isEnable: Boolean = true,
) : Serializable