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
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import top.cgglyle.boson.security.account.AccountFindable
import top.cgglyle.boson.security.account.AccountManager
import top.cgglyle.boson.security.account.CreateAccountDto
import top.cgglyle.boson.security.account.CreateAccountQuery
import top.cgglyle.boson.security.auth.AuthUserManager
import top.cgglyle.boson.security.auth.UidUser
import top.cgglyle.boson.security.authentication.AuthenticationManager
import top.cgglyle.boson.security.authentication.CreateAuthDto
import top.cgglyle.boson.security.authorization.RoleFindable
import top.cgglyle.boson.security.authorization.RoleName
import top.cgglyle.boson.security.common.UID
import top.cgglyle.boson.security.exception.DataExistException
import top.cgglyle.boson.security.exception.IllegalArgumentException

@Service
class UserService(
    private val roleFindable: RoleFindable,
    private val userDetailsService: UserDetailsService,
    private val accountManager: AccountManager,
    private val accountFindable: AccountFindable,
    private val authenticationManager: AuthenticationManager,
    private val authenticationConfiguration: AuthenticationConfiguration,
) : AuthUserManager {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    override fun createUser(query: CreateAccountQuery): UID {
        if (query.username.isNullOrBlank() && query.email.isNullOrBlank()) {
            throw IllegalArgumentException("Try create account, but both username and email are null.")
        }
        if (accountFindable.existsByUsernameOrEmail(query.username, query.email)) {
            throw DataExistException("Account ${query.username} or ${query.email} exist!")
        }

        val roleNames = query.roleNames
        val rids = if (roleNames.isEmpty()) {
            setOf(roleFindable.getRIDByRoleCode(RoleName.USER.name))
        } else {
            roleNames.map {
                roleFindable.getRIDByRoleCode(it)
            }.toSet()
        }

        val uid = accountManager.save(
            CreateAccountDto(
                query.username,
                query.email,
                rids,
                query.accountExpiration,
                query.locked,
                query.enable
            )
        )

        authenticationManager.create(CreateAuthDto(uid, query.password, query.credentialExpiration))

        return uid
    }

    @Transactional
    override fun deleteUser(uid: UID) {
        accountManager.delete(uid)
        authenticationManager.delete(uid)
    }

    @Transactional
    override fun changePassword(oldPassword: String, newPassword: String) {
        val contextHolderStrategy = SecurityContextHolder.getContextHolderStrategy()
        val currentUser = contextHolderStrategy.context.authentication
            ?: throw AccessDeniedException("Can't change password as no Authentication object found in context for current user.")
        val username = currentUser.name
        val systemAuthenticationManager = authenticationConfiguration.authenticationManager
        logger.debug("Re-authenticating user '$username' for password change request.")
        systemAuthenticationManager.authenticate(
            UsernamePasswordAuthenticationToken.unauthenticated(
                username,
                oldPassword
            )
        )

        logger.debug("Changing password for user '$username'")
        val uidUser = currentUser.principal as UidUser
        authenticationManager.addPassword(uidUser.uid, newPassword)
        val authentication = createNewAuthentication(currentUser, newPassword)
        val securityContext = contextHolderStrategy.createEmptyContext()
        securityContext.authentication = authentication
        contextHolderStrategy.context = securityContext
    }

    @Transactional
    override fun userExists(username: String): Boolean {
        return accountFindable.existUsername(username)
    }

    @Transactional
    protected fun createNewAuthentication(currentAuth: Authentication, newPassword: String): Authentication {
        val user = userDetailsService.loadUserByUsername(currentAuth.name)
        val newAuthentication = UsernamePasswordAuthenticationToken.authenticated(user, null, user.authorities)
        newAuthentication.details = currentAuth.details
        return newAuthentication
    }
}