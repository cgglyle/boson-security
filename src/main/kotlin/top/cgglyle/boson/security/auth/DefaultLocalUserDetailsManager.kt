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

package top.cgglyle.boson.security.auth

import org.slf4j.LoggerFactory
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.UserDetailsManager
import org.springframework.stereotype.Service
import top.cgglyle.boson.security.auth.domain.entity.LocalAuthEntity
import top.cgglyle.boson.security.auth.domain.entity.LocalAuthEntityRepository
import top.cgglyle.boson.security.common.exception.DataNotFoundException
import top.cgglyle.boson.security.common.exception.IllegalArgumentException

@Service
class DefaultLocalUserDetailsManager(
    private val localAuthEntityRepository: LocalAuthEntityRepository,
    private val authenticationConfiguration: AuthenticationConfiguration,
    private val passwordEncoder: PasswordEncoder,
) : UserDetailsService, UserDetailsManager {
    private val logger = LoggerFactory.getLogger(javaClass)
    override fun loadUserByUsername(username: String?): UserDetails {
        if (username.isNullOrBlank()) throw IllegalArgumentException("Username must not be null!")
        return localAuthEntityRepository.findByUsername(username)
            ?: throw DataNotFoundException("Username '$username' not found!")
    }

    override fun createUser(user: UserDetails) {
        if (user is LocalAuthEntity) {
            localAuthEntityRepository.save(user)
        } else throw IllegalArgumentException("UserDetails is not LocalAuthEntity cannot be processed")
    }

    override fun updateUser(user: UserDetails?) {
        TODO("Not yet implemented")
    }

    override fun deleteUser(username: String?) {
        val userDetails = loadUserByUsername(username) as LocalAuthEntity
        localAuthEntityRepository.delete(userDetails)
    }

    override fun changePassword(oldPassword: String, newPassword: String) {
        val contextHolderStrategy = SecurityContextHolder.getContextHolderStrategy()
        val currentUser = contextHolderStrategy.context.authentication
            ?: throw AccessDeniedException("Can't change password as no Authentication object found in context for current user.")
        val username = currentUser.name
        val authenticationManager = authenticationConfiguration.authenticationManager
        logger.debug("Re-authenticating user '$username' for password change request.")
        authenticationManager.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(username, oldPassword))

        logger.debug("Changing password for user '$username'")
        val dbUser = loadUserByUsername(username) as LocalAuthEntity
        dbUser.addPassword(newPassword, passwordEncoder)
        val authentication = createNewAuthentication(currentUser, newPassword)
        val securityContext = contextHolderStrategy.createEmptyContext()
        securityContext.authentication = authentication
        contextHolderStrategy.context = securityContext
        localAuthEntityRepository.save(dbUser)
    }

    override fun userExists(username: String): Boolean {
        return localAuthEntityRepository.userExists(username)
    }

    protected fun createNewAuthentication(currentAuth: Authentication, newPassword: String): Authentication {
        val user = loadUserByUsername(currentAuth.name)
        val newAuthentication = UsernamePasswordAuthenticationToken.authenticated(user, null, user.authorities)
        newAuthentication.details = currentAuth.details
        return newAuthentication
    }
}