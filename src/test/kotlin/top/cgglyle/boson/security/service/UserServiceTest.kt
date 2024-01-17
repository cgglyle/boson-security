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

import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.UserDetailsManager
import top.cgglyle.boson.security.common.exception.DataExistException
import top.cgglyle.boson.security.common.exception.DataNotFoundException
import top.cgglyle.boson.security.common.exception.IllegalArgumentException
import top.cgglyle.boson.security.domain.RoleName
import top.cgglyle.boson.security.repository.RoleRepository
import top.cgglyle.boson.security.user.domain.entity.AccountRepository
import top.cgglyle.boson.security.web.query.CreateUserQuery

/**
 * @author: Lyle Liu
 */
@ExtendWith(MockKExtension::class)
class UserServiceTest {
    @InjectMockKs
    lateinit var userService: UserService

    @MockK
    lateinit var roleRepository: RoleRepository

    @MockK
    lateinit var passwordEncoder: PasswordEncoder

    @MockK
    lateinit var accountRepository: AccountRepository

    @MockK
    lateinit var userDetailsManager: UserDetailsManager


    @Test
    fun creatUser_shouldNotSameUser() {

        // given
        val createUserQuery = mockk<CreateUserQuery>()
        every { createUserQuery.username }.returns("xx")
        every { createUserQuery.email }.returns("xx")
        every { accountRepository.existsByUsernameOrEmail(any(), any()) }.returns(true)

        // when
        assertThrows<DataExistException> {
            userService.createUser(createUserQuery)
        }
    }

    @Test
    fun creatUser_bothUsernameOrEmailNotNull() {

        // given
        val createUserQuery = mockk<CreateUserQuery>()
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
        val createUserQuery = mockk<CreateUserQuery>(relaxed = true)
        every { createUserQuery.username }.returns("xx")
        every { createUserQuery.email }.returns("xx")
        every { createUserQuery.password }.returns("xx")
        val stringSet = setOf<String>()
        every { createUserQuery.roleNames }.returns(stringSet)
        every { accountRepository.existsByUsernameOrEmail(any(), any()) }.returns(false)

        every { roleRepository.findRoleEntityByRoleName(any()) }.returns(null)
        every { roleRepository.findRoleEntityByRoleName(RoleName.USER) }.returns(mockk())
        every { accountRepository.save(any()) }.returns(mockk())
        every { userDetailsManager.createUser(any()) }.returns(mockk())
        every { passwordEncoder.encode(any()) }.returns("xx")


        // when
        userService.createUser(createUserQuery)

        // then
        verify { roleRepository.findRoleEntityByRoleName(any()) }
    }

    @Test
    fun creatUser_mustCallUserDetailsCreate() {

        // given
        val createUserQuery = mockk<CreateUserQuery>(relaxed = true)
        every { createUserQuery.username }.returns("xx")
        every { createUserQuery.email }.returns("xx")
        every { createUserQuery.password }.returns("xx")
        val stringSet = setOf<String>()
        every { createUserQuery.roleNames }.returns(stringSet)
        every { accountRepository.existsByUsernameOrEmail(any(), any()) }.returns(false)

        every { roleRepository.findRoleEntityByRoleName(any()) }.returns(null)
        every { roleRepository.findRoleEntityByRoleName(RoleName.USER) }.returns(mockk())
        every { accountRepository.save(any()) }.returns(mockk())
        every { userDetailsManager.createUser(any()) }.returns(mockk())
        every { passwordEncoder.encode(any()) }.returns("xx")


        // when
        userService.createUser(createUserQuery)

        // then
        verify { userDetailsManager.createUser(any()) }
    }

    @Test
    fun creatUser_roleNameMustExist() {

        // given
        val createUserQuery = mockk<CreateUserQuery>(relaxed = true)
        every { createUserQuery.username }.returns("xx")
        every { createUserQuery.email }.returns("xx")
        every { createUserQuery.password }.returns("xx")
        val stringSet = setOf<String>("xxx")
        every { createUserQuery.roleNames }.returns(stringSet)
        mockkStatic(RoleName::class)
        every { RoleName.valueOf(any()) }.returns(mockk())
        every { accountRepository.existsByUsernameOrEmail(any(), any()) }.returns(false)

        every { roleRepository.findRoleEntityByRoleName(any()) }.returns(null)
        every { roleRepository.findRoleEntityByRoleName(RoleName.USER) }.returns(mockk())
        every { accountRepository.save(any()) }.returns(mockk())
        every { userDetailsManager.createUser(any()) }.returns(mockk())
        every { passwordEncoder.encode(any()) }.returns("xx")


        // when
        assertThrows<DataNotFoundException> {
            userService.createUser(createUserQuery)
        }
    }
}