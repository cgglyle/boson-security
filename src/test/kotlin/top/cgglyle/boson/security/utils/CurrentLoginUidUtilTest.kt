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
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import top.cgglyle.boson.security.auth.CurrentLoginUidUtil
import top.cgglyle.boson.security.common.UID
import top.cgglyle.boson.security.exception.DataNotFoundException
import top.cgglyle.boson.security.auth.UidDetailUser as UidUser

/**
 * @author: Lyle Liu
 */
class CurrentLoginUidUtilTest {

    @Test
    fun unAuthenticationShouldBeThrowException() {
        mockkStatic(SecurityContextHolder::class)
        every { SecurityContextHolder.getContext().authentication }.returns(null)
        mockkStatic(UID::class)

        // when
        assertThrows<DataNotFoundException> {
            CurrentLoginUidUtil.getCurrentLoginUid()
        }

    }

    @Test
    fun authenticationUserShouldIsUID() {
        mockkStatic(SecurityContextHolder::class)
        val authentication = mockk<Authentication>()
        every { SecurityContextHolder.getContext().authentication }.returns(authentication)
        every { authentication.isAuthenticated }.returns(true)
        val principal = mockk<UidUser>()
        every { authentication.principal }.returns(principal)
        every { principal.uid.value }.returns("1")

        // when
        val currentLoginUid = CurrentLoginUidUtil.getCurrentLoginUid()

        // then
        assertEquals("1", currentLoginUid.value)
    }
}