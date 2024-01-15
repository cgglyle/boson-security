package top.cgglyle.boson.security.domain.entity

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import org.springframework.security.core.GrantedAuthority
import top.cgglyle.boson.security.common.entity.basic.AbstractIDEntity
import top.cgglyle.boson.security.domain.RoleName

@Entity
@Table(name = "sys_role")
class RoleEntity(
    @Enumerated(EnumType.STRING)
    val roleName: RoleName = RoleName.ANONYMOUS
) : AbstractIDEntity(), GrantedAuthority {
    override fun getAuthority(): String {
        return roleName.name
    }
}