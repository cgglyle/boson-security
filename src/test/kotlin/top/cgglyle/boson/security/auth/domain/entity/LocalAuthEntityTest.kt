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

package top.cgglyle.boson.security.auth.domain.entity

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.crypto.password.PasswordEncoder
import top.cgglyle.boson.security.common.entity.password.PasswordEntity
import top.cgglyle.boson.security.user.domain.entity.Account

/**
 * @author: Lyle Liu
 */
@ExtendWith(MockKExtension::class)
class LocalAuthEntityTest {

    @MockK(relaxed = true)
    lateinit var account: Account

    @MockK
    lateinit var passwordEncoder: PasswordEncoder

    @MockK(relaxed = true)
    lateinit var passwordEntity: PasswordEntity

    @InjectMockKs
    lateinit var localAuthEntity: LocalAuthEntity

    @Test
    fun addPassword() {
        every { passwordEntity.addPassword(any(), any()) }.returns(mockk())
        // when
        localAuthEntity.addPassword("", passwordEncoder)

        // then
        verify { passwordEntity.addPassword(any(), any()) }
    }

    @Test
    fun getPassword() {
        every { passwordEntity.getLastedPassword() }.returns(mockk(relaxed = true))
        // when
        localAuthEntity.password

        verify { passwordEntity.getLastedPassword() }
    }

    @Test
    fun getUsername() {
        // when
        localAuthEntity.username

        verify { account.username }
    }

    @Test
    fun getAuthorities() {
        localAuthEntity.authorities

        verify { account.getAuthorities() }
    }

    @Test
    fun isAccountNonExpired() {
        localAuthEntity.isAccountNonExpired

        verify { account.accountNonExpired }
    }

    @Test
    fun isAccountNonLocked() {
        localAuthEntity.isAccountNonLocked

        verify { account.accountNonLocked }
    }

    @Test
    fun isCredentialsNonExpired() {
        localAuthEntity.isCredentialsNonExpired

        verify { account.credentialsNonExpired }
    }

    @Test
    fun isEnabled() {
        localAuthEntity.isEnabled

        verify { account.enable }
    }
}