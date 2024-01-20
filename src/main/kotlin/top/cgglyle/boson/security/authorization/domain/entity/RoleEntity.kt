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

package top.cgglyle.boson.security.authorization.domain.entity

import jakarta.persistence.*
import top.cgglyle.boson.security.authorization.RID
import top.cgglyle.boson.security.authorization.RoleName
import top.cgglyle.boson.security.common.AbstractIDEntity

@Entity
@Table(name = "sys_role")
class RoleEntity(
    @Column(name = "rid", updatable = false, nullable = false, unique = true)
    val rid: RID = RID.randomRID(),
    @Enumerated(EnumType.STRING)
    val roleName: RoleName = RoleName.ANONYMOUS
) : AbstractIDEntity() {
}