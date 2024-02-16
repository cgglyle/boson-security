/*
 * Copyright 2024 Lyle Liu<cgglyle@outlook.com> and all contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.cgglyle.boson.security.authorization.service

import org.springframework.stereotype.Service
import top.cgglyle.boson.security.authorization.*
import top.cgglyle.boson.security.authorization.domain.entity.RoleEntity
import top.cgglyle.boson.security.authorization.domain.entity.RoleRepository
import top.cgglyle.boson.security.exception.DataNotFoundException

/**
 * @author: Lyle Liu
 */
@Service
class RoleService(
    private val roleRepository: RoleRepository,
) : RoleFindable, RoleManager {
    override fun findByRid(rid: RID): RoleDto? {
        return roleRepository.findByRid(rid)
    }

    override fun getByRid(rid: RID): RoleDto {
        return findByRid(rid) ?: throw DataNotFoundException("Rid: '$rid' is not found!")
    }

    override fun getRIDByRoleCode(roleCode: String): RID {
        val role: RoleEntity? = roleRepository.findByRoleName(RoleName.valueOf(roleCode))
        role ?: throw DataNotFoundException("Role code: '$roleCode' not found!")
        return role.rid
    }

    override fun count(): Long {
        return roleRepository.count()
    }

    override fun exists(rid: RID): Boolean = roleRepository.existsByRid(rid)
    override fun existsOrThrowException(rid: RID) =
        if (exists(rid)) Unit else throw DataNotFoundException("Rid: $rid not found.")


    override fun create(role: CreateRoleDto) {
        roleRepository.save(RoleEntity(roleName = role.roleName))
    }
}