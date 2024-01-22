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

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.context.SecurityContextHolderStrategy
import org.springframework.security.core.userdetails.UserDetailsService
import top.cgglyle.boson.security.account.AccountFindable
import top.cgglyle.boson.security.account.AccountManager
import top.cgglyle.boson.security.account.CreateAccountQuery
import top.cgglyle.boson.security.authorization.RoleFindable
import top.cgglyle.boson.security.exception.DataExistException
import top.cgglyle.boson.security.exception.IllegalArgumentException
import top.cgglyle.boson.security.auth.UidDetailUser as UidUser

/**
 * @author: Lyle Liu
 */
@ExtendWith(MockKExtension::class)
class UserServiceTest {
    @InjectMockKs
    lateinit var userService: UserService

    @MockK
    lateinit var roleFindable: RoleFindable

    @MockK
    lateinit var userDetailsService: UserDetailsService

    @MockK
    lateinit var accountManager: AccountManager

    @MockK
    lateinit var accountFindable: AccountFindable

    @MockK(relaxed = true)
    lateinit var authenticationManager: top.cgglyle.boson.security.authentication.AuthenticationManager

    @MockK
    lateinit var authenticationConfiguration: AuthenticationConfiguration

    @Test
    fun creatUser_shouldNotSameUser() {

        // given
        val createUserQuery = mockk<CreateAccountQuery>()
        every { createUserQuery.username }.returns("xx")
        every { createUserQuery.email }.returns("xx")
        every { accountFindable.existsByUsernameOrEmail(any(), any()) }.returns(true)

        // when
        assertThrows<DataExistException> {
            userService.createUser(createUserQuery)
        }
    }

    @Test
    fun creatUser_bothUsernameOrEmailNotNull() {

        // given
        val createUserQuery = mockk<CreateAccountQuery>()
        every { createUserQuery.username }.returns(null)
        every { createUserQuery.email }.returns(null)

        // when
        assertThrows<IllegalArgumentException> {
            userService.createUser(createUserQuery)
        }
    }

    @Test
    fun creatUser_ifQueryRoleIsNullShouldBeDefaultUserRole() {

        // given
        val createUserQuery = mockk<CreateAccountQuery>(relaxed = true)
        every { createUserQuery.username }.returns("xx")
        every { createUserQuery.email }.returns("xx")
        every { createUserQuery.password }.returns("xx")
        val stringSet = setOf<String>()
        every { createUserQuery.roleNames }.returns(stringSet)
        every { accountFindable.existsByUsernameOrEmail(any(), any()) }.returns(false)

        every { roleFindable.getRIDByRoleCode(any()) }.returns(mockk())
        every { accountManager.save(any()) }.returns(mockk(relaxed = true))
        every { authenticationManager.create(any()) }.returns(mockk())

        // when
        userService.createUser(createUserQuery)

        // then
        verify { roleFindable.getRIDByRoleCode(any()) }
    }

    @Test
    fun creatUser_mustCallUserDetailsCreate() {

        // given
        val createUserQuery = mockk<CreateAccountQuery>(relaxed = true)
        every { createUserQuery.username }.returns("xx")
        every { createUserQuery.email }.returns("xx")
        every { createUserQuery.password }.returns("xx")
        val stringSet = setOf<String>()
        every { createUserQuery.roleNames }.returns(stringSet)
        every { accountFindable.existsByUsernameOrEmail(any(), any()) }.returns(false)

        every { roleFindable.getRIDByRoleCode(any()) }.returns(mockk())
        every { accountManager.save(any()) }.returns(mockk(relaxed = true))
        every { authenticationManager.create(any()) }.returns(mockk())

        // when
        userService.createUser(createUserQuery)

        // then
        verify { authenticationManager.create(any()) }
    }


    @Test
    fun changePassword_shouldReAuthentication() {
        //given
        val oldPassword = "old password"
        val newPassword = "new password"

        val authenticationConfiguration = mockk<AuthenticationConfiguration>()

        val manager = UserService(
            roleFindable,
            userDetailsService,
            accountManager,
            accountFindable,
            authenticationManager,
            authenticationConfiguration
        )

        val authenticationManager = mockk<AuthenticationManager>()
        every { authenticationConfiguration.authenticationManager }.returns(authenticationManager)
        every { authenticationManager.authenticate(any()) }.returns(mockk())
        mockkStatic(SecurityContextHolder::class)

        val securityContextHolderStrategy = mockk<SecurityContextHolderStrategy>(relaxed = true)
        every { SecurityContextHolder.getContextHolderStrategy() }.returns(securityContextHolderStrategy)
        val au = mockk<Authentication>(relaxed = true)
        every { securityContextHolderStrategy.context.authentication }.returns(au)
        val uidUser = mockk<UidUser>(relaxed = true)
        every { au.principal }.returns(uidUser)
        every { userDetailsService.loadUserByUsername(any()) }.returns(mockk(relaxed = true))

        // when
        manager.changePassword(oldPassword, newPassword)

        // then
        verify { authenticationManager.authenticate(any()) }
    }

    @Test
    fun changePassword_shouldResetSecurityContext() {
        //given
        val oldPassword = "old password"
        val newPassword = "new password"

        val authenticationConfiguration = mockk<AuthenticationConfiguration>()
        val manager = UserService(
            roleFindable,
            userDetailsService,
            accountManager,
            accountFindable,
            authenticationManager,
            authenticationConfiguration
        )

        val authenticationManager = mockk<AuthenticationManager>()
        every { authenticationConfiguration.authenticationManager }.returns(authenticationManager)
        every { authenticationManager.authenticate(any()) }.returns(mockk())
        mockkStatic(SecurityContextHolder::class)

        val securityContextHolderStrategy = mockk<SecurityContextHolderStrategy>(relaxed = true)
        every { SecurityContextHolder.getContextHolderStrategy() }.returns(securityContextHolderStrategy)
        val au = mockk<Authentication>(relaxed = true)
        every { securityContextHolderStrategy.context.authentication }.returns(au)
        val uidUser = mockk<UidUser>(relaxed = true)
        every { au.principal }.returns(uidUser)
        every { userDetailsService.loadUserByUsername(any()) }.returns(mockk(relaxed = true))

        // when
        manager.changePassword(oldPassword, newPassword)

        // then
        verify { securityContextHolderStrategy.context = any() }
    }

    @Test
    fun changePassword_shouldSaveNewPassword() {
        //given
        val oldPassword = "old password"
        val newPassword = "new password"

        val authenticationConfiguration = mockk<AuthenticationConfiguration>()
        val manager = UserService(
            roleFindable,
            userDetailsService,
            accountManager,
            accountFindable,
            authenticationManager,
            authenticationConfiguration
        )

        val springAuthenticationManager = mockk<AuthenticationManager>()
        every { authenticationConfiguration.authenticationManager }.returns(springAuthenticationManager)
        every { springAuthenticationManager.authenticate(any()) }.returns(mockk())
        mockkStatic(SecurityContextHolder::class)

        val securityContextHolderStrategy = mockk<SecurityContextHolderStrategy>(relaxed = true)
        every { SecurityContextHolder.getContextHolderStrategy() }.returns(securityContextHolderStrategy)
        val au = mockk<Authentication>(relaxed = true)
        every { securityContextHolderStrategy.context.authentication }.returns(au)
        val uidUser = mockk<UidUser>(relaxed = true)
        every { au.principal }.returns(uidUser)
        every { authenticationManager.addPassword(any(), any()) }.returns(mockk(relaxed = true))
        every { userDetailsService.loadUserByUsername(any()) }.returns(mockk(relaxed = true))

        // when
        manager.changePassword(oldPassword, newPassword)

        // then
        verify { authenticationManager.addPassword(any(), any()) }
    }

    @Test
    fun changePassword_shouldOnlyChangeSelfPassword() {
        //given
        val oldPassword = "old password"
        val newPassword = "new password"

        val authenticationConfiguration = mockk<AuthenticationConfiguration>()
        val manager = UserService(
            roleFindable,
            userDetailsService,
            accountManager,
            accountFindable,
            authenticationManager,
            authenticationConfiguration
        )

        val springAuthenticationManager = mockk<AuthenticationManager>()
        every { authenticationConfiguration.authenticationManager }.returns(springAuthenticationManager)
        every { springAuthenticationManager.authenticate(any()) }.returns(mockk())
        mockkStatic(SecurityContextHolder::class)

        val securityContextHolderStrategy = mockk<SecurityContextHolderStrategy>(relaxed = true)
        every { SecurityContextHolder.getContextHolderStrategy() }.returns(securityContextHolderStrategy)
        val authentication = mockk<Authentication>(relaxed = true)
        every { securityContextHolderStrategy.context.authentication }.returns(authentication)
        val uidUser = mockk<UidUser>()
        every { authentication.principal }.returns(uidUser)
        every { authentication.name }.returns("username")
        val uid = mockk<top.cgglyle.boson.security.common.UID>()
        every { uidUser.uid }.returns(uid)
        every { userDetailsService.loadUserByUsername(any()) }.returns(mockk(relaxed = true))

        // when
        manager.changePassword(oldPassword, newPassword)

        // then
        verify { authenticationManager.addPassword(uid, any()) }
    }

    @Test
    fun changePassword_shouldCannotChangeUnAuthenticationUserPassword() {
        //given
        val oldPassword = "old password"
        val newPassword = "new password"

        val authenticationConfiguration = mockk<AuthenticationConfiguration>()
        val manager = UserService(
            roleFindable,
            userDetailsService,
            accountManager,
            accountFindable,
            authenticationManager,
            authenticationConfiguration
        )

        val springAuthenticationManager = mockk<AuthenticationManager>()
        every { authenticationConfiguration.authenticationManager }.returns(springAuthenticationManager)
        every { springAuthenticationManager.authenticate(any()) }.returns(mockk())
        mockkStatic(SecurityContextHolder::class)

        val securityContextHolderStrategy = mockk<SecurityContextHolderStrategy>(relaxed = true)
        every { SecurityContextHolder.getContextHolderStrategy() }.returns(securityContextHolderStrategy)
        every { securityContextHolderStrategy.context.authentication }.returns(null)
        val uidUser = mockk<UidUser>(relaxed = true)
        val uid = mockk<top.cgglyle.boson.security.common.UID>()
        every { uidUser.uid }.returns(uid)
        every { userDetailsService.loadUserByUsername(any()) }.returns(mockk(relaxed = true))

        // when
        assertThrows<AccessDeniedException> {
            manager.changePassword(oldPassword, newPassword)
        }

        // then
        verify(exactly = 0) {
            authenticationManager.addPassword(any(), any())
            springAuthenticationManager.authenticate(any())
            securityContextHolderStrategy.context = any()
        }
    }

    @Test
    fun deleteUser() {
        // then
        every { accountManager.delete(any()) }.returns(mockk())
        every { authenticationManager.delete(any()) }.returns(mockk())

        // when
        userService.deleteUser(mockk())

        // then
        verify {
            accountManager.delete(any())
            authenticationManager.delete(any())
        }
    }

    @Test
    fun userExists() {
        // then
        every { accountFindable.existUsername(any()) }.returns(true)

        // when
        val username = userService.userExists("username")

        // then
        Assertions.assertTrue(username)
    }
}