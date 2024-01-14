package top.cgglyle.boson.security.web.view

import top.cgglyle.boson.security.domain.RoleName
import java.io.Serializable

/**
 * DTO for {@link top.cgglyle.boson.security.domain.entity.RoleEntity}
 */
data class RoleinfoView(val id: Long = -1, val roleName: RoleName = RoleName.ANONYMOUS) : Serializable