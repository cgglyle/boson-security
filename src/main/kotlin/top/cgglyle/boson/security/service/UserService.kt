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

package top.cgglyle.boson.security.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.UserDetailsManager
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import top.cgglyle.boson.security.auth.domain.entity.LocalAuthEntity
import top.cgglyle.boson.security.common.entity.password.PasswordEntity
import top.cgglyle.boson.security.common.exception.DataExistException
import top.cgglyle.boson.security.common.exception.DataNotFoundException
import top.cgglyle.boson.security.common.exception.IllegalArgumentException
import top.cgglyle.boson.security.domain.RoleName
import top.cgglyle.boson.security.repository.RoleRepository
import top.cgglyle.boson.security.user.domain.command.CreateAccountCommand
import top.cgglyle.boson.security.user.domain.entity.Account
import top.cgglyle.boson.security.user.domain.entity.AccountRepository
import top.cgglyle.boson.security.web.query.CreateUserQuery

@Service
class UserService(
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder,
    private val accountRepository: AccountRepository,
    private val userDetailsManager: UserDetailsManager,
) {

    @Transactional
    fun createUser(query: CreateUserQuery): Account {
        if (query.username.isNullOrBlank() && query.email.isNullOrBlank()) {
            throw IllegalArgumentException("Try create account, but both username and email are null.")
        }
        if (accountRepository.existsByUsernameOrEmail(query.username, query.email)) {
            throw DataExistException("Account ${query.username} or ${query.email} exist!")
        }
        val roleNames = query.roleNames
        val roleEntities =
            if (roleNames.isEmpty()) {
                setOf(roleRepository.findRoleEntityByRoleName(RoleName.USER)!!)
            } else {
                roleNames.map {
                    roleRepository.findRoleEntityByRoleName(RoleName.valueOf(it))
                        ?: throw DataNotFoundException("Role: $it not found!")
                }.toSet()
            }

        val command = CreateAccountCommand(
            query.username,
            query.email,
            roleEntities,
        )
        val account = accountRepository.save(Account(command))

        val passwordEntity = PasswordEntity(query.password, passwordEncoder)

        val localAuthEntity = LocalAuthEntity(account, passwordEntity)

        userDetailsManager.createUser(localAuthEntity)

        return account
    }
}