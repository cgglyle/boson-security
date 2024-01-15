package top.cgglyle.boson.security.repository

import top.cgglyle.boson.security.common.entity.basic.AbstractIDRepository
import top.cgglyle.boson.security.domain.RoleName
import top.cgglyle.boson.security.domain.entity.RoleEntity

interface RoleRepository : AbstractIDRepository<RoleEntity> {
    fun findRoleEntityByRoleName(roleName: RoleName): RoleEntity?
}