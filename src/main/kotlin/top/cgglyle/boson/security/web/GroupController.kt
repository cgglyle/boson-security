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

package top.cgglyle.boson.security.web

import jakarta.validation.Valid
import org.springframework.data.domain.PageRequest
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import top.cgglyle.boson.security.authorization.RID
import top.cgglyle.boson.security.common.UID
import top.cgglyle.boson.security.group.*
import top.cgglyle.boson.security.web.query.GroupAddAccountQuery
import top.cgglyle.boson.security.web.query.GroupAddRoleQuery
import kotlin.math.max


@RestController
@RequestMapping("/api/group")
@Validated
class GroupController(
    val groupFindable: GroupFindable,
    val groupManager: GroupManager,
) {

    @Transactional
    @GetMapping
    fun getAll(
        pageNumber: Int = 0,
        pageSize: Int = 10,
    ): WebPage<GroupDto> {
        val accountDtoPage = groupFindable.findAllGroup(PageRequest.of(max(0, pageNumber - 1), pageSize))
        return WebPage.form(accountDtoPage)
    }

    @Transactional
    @PostMapping
    fun createGroup(@RequestBody @Valid query: CreateGroupQuery) {
        groupManager.createGroup(query)
    }

    @Transactional
    @PostMapping("account")
    fun addAccount(@RequestBody @Valid query: GroupAddAccountQuery) {
        val gid = GID(query.gid)
        val uid = UID(query.uid)
        groupManager.addAccount(gid, uid)
    }

    @Transactional
    @PostMapping("role")
    fun addRole(@RequestBody @Valid query: GroupAddRoleQuery) {
        val gid = GID(query.gid)
        val rid = RID(query.rid)
        groupManager.addRole(gid, rid)
    }
}