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

package top.cgglyle.boson.security.group.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import top.cgglyle.boson.security.account.AccountFindable
import top.cgglyle.boson.security.authorization.RID
import top.cgglyle.boson.security.authorization.RoleFindable
import top.cgglyle.boson.security.common.UID
import top.cgglyle.boson.security.exception.DataNotFoundException
import top.cgglyle.boson.security.exception.IllegalArgumentException
import top.cgglyle.boson.security.group.*
import top.cgglyle.boson.security.group.domain.entity.Group
import top.cgglyle.boson.security.group.domain.entity.GroupRepository

@Service
class GroupService(
    private val groupRepository: GroupRepository,
    private val accountFindable: AccountFindable,
    private val roleFindable: RoleFindable,
) : GroupFindable, GroupManager {

    @Transactional
    override fun findAllGroup(pageable: Pageable): Page<GroupDto> {
        val groupPage = groupRepository.findAll(pageable)
        return groupPage.map { it.toDto() }
    }

    @Transactional
    override fun findGroup(name: String): GroupDto? {
        val group = groupRepository.findByName(name)
        return group?.toDto()
    }

    @Transactional
    override fun createGroup(query: CreateGroupQuery) {
        val group = Group(query.name, query.description)
        groupRepository.save(group)
    }

    @Transactional
    override fun removeGroup(gid: GID) {
        groupRepository.deleteByGid(gid)
    }

    @Transactional
    override fun addAccount(gid: GID, uid: UID) {
        accountFindable.existsOrThrowException(uid)
        val group = findGroupOrThrowException(gid)
        group.addUid(uid)
    }

    @Transactional
    override fun deletedAccount(gid: GID, uid: UID) {
        accountFindable.existsOrThrowException(uid)
        val group = findGroupOrThrowException(gid)
        group.removeUid(uid)
    }

    @Transactional
    override fun addRole(gid: GID, rid: RID) {
        roleFindable.existsOrThrowException(rid)
        val group = findGroupOrThrowException(gid)
        group.addRid(rid)
    }

    @Transactional
    override fun deletedRole(gid: GID, rid: RID) {
        roleFindable.existsOrThrowException(rid)
        val group = findGroupOrThrowException(gid)
        group.removeRid(rid)
    }

    protected fun findGroupOrThrowException(query: Any): Group {
        val group = when (query) {
            is GID -> groupRepository.findByGid(query)
            is String -> groupRepository.findByName(query)
            else -> throw IllegalArgumentException("This query must is GID or Name!")
        } ?: throw DataNotFoundException("Group $query not found")
        return group
    }
}