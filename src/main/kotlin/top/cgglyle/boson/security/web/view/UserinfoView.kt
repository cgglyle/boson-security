package top.cgglyle.boson.security.web.view

import java.io.Serializable
import java.time.Instant

/**
 * DTO for {@link top.cgglyle.boson.security.domain.entity.auth.UserEntity}
 */
data class UserinfoView(
    val id: Long = -1,
    val lastModifiedBy: Long? = null,
    val lastModifiedDate: Instant? = null,
    val username: String = "",
    val roles: MutableSet<RoleinfoView> = mutableSetOf(),
    val accountNonExpired: Boolean = false,
    val accountNonLocked: Boolean = false,
    val credentialsNonExpired: Boolean = false,
    val enable: Boolean = false
) : Serializable