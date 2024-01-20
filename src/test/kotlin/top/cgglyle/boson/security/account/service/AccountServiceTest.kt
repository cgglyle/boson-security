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

package top.cgglyle.boson.security.account.service

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import top.cgglyle.boson.security.account.domain.AccountRepository

/**
 * @author: Lyle Liu
 */
@ExtendWith(MockKExtension::class)
class AccountServiceTest {
    @InjectMockKs
    lateinit var accountService: AccountService

    @MockK
    lateinit var accountRepository: AccountRepository

    @Test
    fun findByUsername_CanBeNull() {
        every { accountRepository.findByUsername("username") }.returns(null)
        val user = accountService.findByUsername("username")

        assertNull(user)

    }

    @Test
    fun existUid() {
    }

    @Test
    fun existUsername() {
    }

    @Test
    fun existsByUsernameOrEmail() {
    }

    @Test
    fun save() {
    }

    @Test
    fun delete() {
    }
}