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

package top.cgglyle.boson.security.account.domain

import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import top.cgglyle.boson.security.account.CreateAccountCommand
import top.cgglyle.boson.security.exception.IllegalArgumentException

/**
 * @author: Lyle Liu
 */
@ExtendWith(MockKExtension::class)
class AccountTest {

    @Test
    fun getUid_mustNotBeNull() {
        // given
        val createAccountCommand = CreateAccountCommand("username", "user@email.com", mutableSetOf())
        // when
        val account = Account(createAccountCommand)
        // then
        assertNotNull(account.uid)
    }

    @Test
    fun getUsername_mustNotBeNull() {
        // given
        val createAccountCommand = CreateAccountCommand("username", "user@email.com", mutableSetOf())
        // when
        val account = Account(createAccountCommand)
        // then
        assertNotNull(account.username)
    }

    @Test
    fun getUsername_ifNullShouldGenerate() {
        // given
        val createAccountCommand = CreateAccountCommand(null, "user@email.com", mutableSetOf())
        // when
        val account = Account(createAccountCommand)
        // then
        assertNotNull(account.username)
    }

    @Test
    fun getEmail_canBeNull() {
        // given
        val createAccountCommand = CreateAccountCommand("username", null, mutableSetOf())
        // when
        val account = Account(createAccountCommand)
        // then
        assertNotNull(account.email)
    }

    @Test
    fun bothUsernameAndEmailMustNotBeNull() {
        // given
        val createAccountCommand = CreateAccountCommand(null, null, mutableSetOf())
        // when
        assertThrows<IllegalArgumentException> {
            Account(createAccountCommand)
        }
    }

    @Test
    fun getRoles_MustNotBeNull() {
        // given
        val createAccountCommand = CreateAccountCommand("username", "user@email.com", mutableSetOf())
        // when
        val account = Account(createAccountCommand)
        // then
        assertNotNull(account.roles)
    }

    @Test
    fun getRolesSet_CanBeNull() {
        // given
        val createAccountCommand = CreateAccountCommand("username", "user@email.com", mutableSetOf())
        // when
        val account = Account(createAccountCommand)
        // then
        assertTrue(account.roles.size == 0)
    }

    @Test
    fun getExpired() {
        // given
        val createAccountCommand = CreateAccountCommand("username", "user@email.com", mutableSetOf())
        // when
        val account = Account(createAccountCommand)
        // then
        assertNotNull(account.expired)
    }

    @Test
    fun getLocked() {
        // given
        val createAccountCommand = CreateAccountCommand("username", "user@email.com", mutableSetOf())
        // when
        val account = Account(createAccountCommand)
        // then
        assertNotNull(account.locked)
    }

    @Test
    fun getEnable() {
        // given
        val createAccountCommand = CreateAccountCommand("username", "user@email.com", mutableSetOf())
        // when
        val account = Account(createAccountCommand)
        // then
        assertNotNull(account.enable)
    }

    @Test
    fun getDefaultAccount() {
        val defaultAccount = Account.defaultAccount()
        assertNotNull(defaultAccount.uid)
        assertNotNull(defaultAccount.username)
        assertNotNull(defaultAccount.roles)
        assertTrue(defaultAccount.roles.size == 0)
    }
}