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

package top.cgglyle.boson.security.group.domain.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length
import org.slf4j.LoggerFactory
import top.cgglyle.boson.security.authorization.RID
import top.cgglyle.boson.security.common.AbstractModifiedAuditingEntity
import top.cgglyle.boson.security.common.NullMark
import top.cgglyle.boson.security.common.UID
import top.cgglyle.boson.security.group.GID
import top.cgglyle.boson.security.group.GroupAddAccountEvent
import top.cgglyle.boson.security.group.domain.entity.Group.Companion.TABLE_NAME

@Entity
@Table(name = TABLE_NAME)
class Group(
    @field:NotBlank
    @field:Length(min = 1, max = 64)
    @Column(name = "name", unique = true, nullable = false)
    val name: String,
    @Length(max = 256)
    val description: String = NullMark.NULL,
) : AbstractModifiedAuditingEntity() {
    @Embedded
    @AttributeOverride(
        name = "value", column = Column(name = "gid", updatable = false, nullable = false, unique = true)
    )
    val gid: GID = GID.randomGID()

    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable(name = "sys_account_group")
    @AttributeOverride(
        name = "value", column = Column(name = "gid", updatable = false, nullable = false)
    )
    private val uidList: MutableSet<UID> = mutableSetOf()

    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable(name = "sys_group_role")
    @AttributeOverride(
        name = "value", column = Column(name = "rid", updatable = false, nullable = false)
    )
    private val ridList: MutableSet<RID> = mutableSetOf()

    fun getUidList(): Set<UID> = uidList

    fun addUid(uid: UID) {
        uidList.add(uid)
        registerEvent(GroupAddAccountEvent(gid, uid))
    }

    fun removeUid(uid: UID) {
        uidList.remove(uid)
    }

    fun getRidList(): Set<RID> = ridList

    fun addRid(rid: RID) {
        ridList.add(rid)
    }

    fun removeRid(rid: RID) {
        ridList.remove(rid)
    }

    companion object {
        val logger = LoggerFactory.getLogger(Group::class.java)
        const val TABLE_NAME = "sys_group"
    }
}