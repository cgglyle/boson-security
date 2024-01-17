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

package top.cgglyle.boson.security.utils

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import top.cgglyle.boson.security.auth.domain.entity.LocalAuthEntity

/**
 * @author: Lyle Liu
 */
class CurrentLoginUidUtilTest {

    @Test
    fun unAuthenticationShouldIsSystemUID() {
        mockkStatic(SecurityContextHolder::class)
        every { SecurityContextHolder.getContext().authentication }.returns(null)
        mockkStatic(top.cgglyle.boson.security.user.domain.entity.UID::class)

        // when
        val currentLoginUid = CurrentLoginUidUtil.getCurrentLoginUid()

        // then
        val contains = currentLoginUid.value.contains("[SYSTEM DEFAULT UID]")
        assertTrue(contains)
    }

    @Test
    fun anonymousUserShouldIsAnonymousUID() {
        mockkStatic(SecurityContextHolder::class)
        val authentication = mockk<Authentication>()
        every { SecurityContextHolder.getContext().authentication }.returns(authentication)
        every { authentication.isAuthenticated }.returns(false)

        // when
        val currentLoginUid = CurrentLoginUidUtil.getCurrentLoginUid()

        // then
        val contains = currentLoginUid.value.contains("[ANONYMOUS DEFAULT UID]")
        assertTrue(contains)
    }
    @Test
    fun authenticationUserShouldIsUID() {
        mockkStatic(SecurityContextHolder::class)
        val authentication = mockk<Authentication>()
        every { SecurityContextHolder.getContext().authentication }.returns(authentication)
        every { authentication.isAuthenticated }.returns(true)
        val principal = mockk<LocalAuthEntity>()
        every { authentication.principal }.returns(principal)
        every { principal.account.uid.value }.returns("1")

        // when
        val currentLoginUid = CurrentLoginUidUtil.getCurrentLoginUid()

        // then
        assertEquals("1", currentLoginUid.value)
    }
}