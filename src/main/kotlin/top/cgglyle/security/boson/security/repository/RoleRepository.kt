package top.cgglyle.security.boson.security.repository

import top.cgglyle.security.boson.security.common.AbstractIDRepository
import top.cgglyle.security.boson.security.domain.RoleName
import top.cgglyle.security.boson.security.domain.entity.RoleEntity

interface RoleRepository : AbstractIDRepository<RoleEntity> {
    fun findRoleEntityByRoleName(roleName: RoleName): RoleEntity?
}