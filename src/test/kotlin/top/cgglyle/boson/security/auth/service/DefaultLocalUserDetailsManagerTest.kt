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
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import top.cgglyle.boson.security.account.AccountDto
import top.cgglyle.boson.security.account.AccountFindable
import top.cgglyle.boson.security.authentication.AuthenticationFindable
import top.cgglyle.boson.security.authentication.domain.LocalAuthEntityRepository
import top.cgglyle.boson.security.authorization.RoleFindable
import top.cgglyle.boson.security.exception.IllegalArgumentException

@ExtendWith(MockKExtension::class)
class DefaultLocalUserDetailsManagerTest {

    @MockK
    lateinit var localAuthEntityRepository: LocalAuthEntityRepository

    @MockK
    lateinit var accountFindable: AccountFindable

    @MockK
    lateinit var roleFindable: RoleFindable

    @MockK
    lateinit var authenticationFindable: AuthenticationFindable

    @MockK
    lateinit var passwordEncoder: PasswordEncoder

    @MockK
    lateinit var authenticationConfiguration: AuthenticationConfiguration

    @InjectMockKs
    lateinit var defaultLocalUserDetailsManager: DefaultLocalUserDetailsManager

    private val okUsername = "OK"
    private val failUsername = "Fail"

    @BeforeEach
    fun setUp() {
        every { localAuthEntityRepository.save(any()) }.returns(mockk())
    }

    @Test
    fun loadUserByUsername() {
        val accountDto = mockk<AccountDto>(relaxed = true)
        every { accountFindable.findByUsername(any()) }.returns(accountDto)
        every { accountDto.username }.returns(okUsername)
        every { authenticationFindable.getAuthenticationInfo(any()) }.returns(mockk(relaxed = true))

        // when
        val user = defaultLocalUserDetailsManager.loadUserByUsername(okUsername)

        // then
        assertNotNull(user)
    }

    @Test
    fun loadUserByUsernameFails() {

        every { accountFindable.findByUsername(any()) }.returns(null)

        // when
        assertThrows<UsernameNotFoundException> {
            defaultLocalUserDetailsManager.loadUserByUsername(failUsername)
        }
    }

    @Test
    fun loadUserByUsernameIsnNull_shouldThrowIllegalArgumentException() {
        // when
        assertThrows<IllegalArgumentException> {
            defaultLocalUserDetailsManager.loadUserByUsername(null)
        }
        assertThrows<IllegalArgumentException> {
            defaultLocalUserDetailsManager.loadUserByUsername("")
        }
    }

}