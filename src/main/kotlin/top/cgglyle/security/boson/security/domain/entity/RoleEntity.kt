package top.cgglyle.security.boson.security.domain.entity

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import org.springframework.security.core.GrantedAuthority
import top.cgglyle.security.boson.security.common.entity.AbstractIDEntity
import top.cgglyle.security.boson.security.domain.RoleName

@Entity
@Table(name = "app_role")
class RoleEntity(
    @Enumerated(EnumType.STRING)
    val roleName: RoleName = RoleName.ANONYMOUS
): AbstractIDEntity(), GrantedAuthority {
    override fun getAuthority(): String {
        return roleName.name
    }
}