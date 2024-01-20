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

package top.cgglyle.boson.security.auth.service

import org.slf4j.LoggerFactory
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import top.cgglyle.boson.security.account.AccountFindable
import top.cgglyle.boson.security.auth.RidRole
import top.cgglyle.boson.security.auth.UidUser
import top.cgglyle.boson.security.auth.UsernameType
import top.cgglyle.boson.security.authentication.AuthenticationFindable
import top.cgglyle.boson.security.authorization.RoleFindable
import top.cgglyle.boson.security.exception.DataNotFoundException
import top.cgglyle.boson.security.exception.IllegalArgumentException
import top.cgglyle.boson.security.exception.SystemException

/**
 * Only the data related to the authenticated entity is processed here,
 * not any account role type of entity.
 *
 * If the certified entity needs these entities, you need to handle them
 * before calling the methods of this class.
 *
 * @author Lyle Liu
 */
@Service
class DefaultLocalUserDetailsManager(
    private val accountFindable: AccountFindable,
    private val roleFindable: RoleFindable,
    private val authenticationFindable: AuthenticationFindable,
) : UserDetailsService {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    override fun loadUserByUsername(username: String?): UserDetails {
        if (username.isNullOrBlank()) throw IllegalArgumentException("Username must not be null!")
        val accountDto = accountFindable.findByUsername(username)
            ?: throw DataNotFoundException("Username '$username' not found!")
        val uid = accountDto.uid
        val roleDtoSet: Set<RidRole> = accountDto.roles.map {
            val roleDto = roleFindable.getByRid(it)
            RidRole(roleDto.rid, roleDto.roleName.name)
        }.toSet()
        val usernameType = when (username) {
            accountDto.username -> UsernameType.USERNAME
            accountDto.email -> UsernameType.EMAIL
            else -> throw SystemException("Username: '$username' must match UsernameType!")
        }
        val authDto = authenticationFindable.getAuthenticationInfo(uid)
        val userDetails = with(accountDto) {
            UidUser(
                uid,
                usernameType,
                username,
                authDto.password,
                enable,
                accountNonExpired,
                authDto.isCredentialsExpired,
                accountNonLocked,
                roleDtoSet
            )
        }
        return userDetails
    }
}