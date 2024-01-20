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

package top.cgglyle.boson.security.authentication.service

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.crypto.password.PasswordEncoder
import top.cgglyle.boson.security.authentication.domain.LocalAuthEntityRepository

/**
 * @author: Lyle Liu
 */
@ExtendWith(MockKExtension::class)
class AuthenticationServiceTest {
    @InjectMockKs
    lateinit var authenticationService: AuthenticationService

    @MockK
    lateinit var localAuthEntityRepository: LocalAuthEntityRepository

    @MockK(relaxed = true)
    lateinit var passwordEncoder: PasswordEncoder

    @Test
    fun findAuthenticationInfo() {
        every { localAuthEntityRepository.findByUid(any()) }.returns(mockk(relaxed = true))

        val authenticationInfo = authenticationService.getAuthenticationInfo(mockk())

        Assertions.assertNotNull(authenticationInfo)
    }

    @Test
    fun create() {
        every { localAuthEntityRepository.save(any()) }.returns(mockk())
        authenticationService.create(mockk(relaxed = true))

        verify { localAuthEntityRepository.save(any()) }
    }


    @Test
    fun addPassword() {
        every { localAuthEntityRepository.findByUid(any()) }.returns(mockk(relaxed = true))

        authenticationService.addPassword(mockk(), "sss")

        verify { localAuthEntityRepository.findByUid(any()) }
    }
}