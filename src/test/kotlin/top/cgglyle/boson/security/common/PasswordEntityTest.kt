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

package top.cgglyle.boson.security.common

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mockito.*
import org.springframework.security.crypto.password.PasswordEncoder
import top.cgglyle.boson.security.exception.ClientException

class PasswordEntityTest {

    @InjectMocks
    lateinit var passwordEntity: PasswordEntity
    private val oldPass = "oldPass"
    private val encodePass = "{encode}oldPass"
    private val newPass = "newPass"

    @BeforeEach
    fun setUp() {
        val passwordEncoder = mock<PasswordEncoder>()
        `when`(passwordEncoder.encode(any())).thenReturn(encodePass)
        passwordEntity = PasswordEntity(oldPass, passwordEncoder)
    }

    @Test
    fun initPassword() {
        //given
        val passwordEncoder = mock(PasswordEncoder::class.java)
        `when`(passwordEncoder.encode(any())).thenReturn("{encode}newPass")

        //when
        val pe = PasswordEntity(newPass, passwordEncoder)

        //then
        assertEquals("{encode}newPass", pe.getLastedPassword().password)
    }


    @Test
    fun getLastedPassword() {
        val passwordEncoder = mock<PasswordEncoder>()
        `when`(passwordEncoder.matches(any(), any())).thenReturn(true)
        `when`(passwordEncoder.encode(any())).thenReturn("{encode}87654321")

        //when
        val oldPassword = passwordEntity.getLastedPassword().password
        passwordEntity.updatePassword(oldPass, newPass, passwordEncoder)

        //then
        assertEquals(encodePass, oldPassword)
        assertEquals("{encode}87654321", passwordEntity.getLastedPassword().password)
    }

    @Test
    fun updatePassword_OK() {
        //given
        val passwordEncoder = mock<PasswordEncoder>()
        `when`(passwordEncoder.matches(any(), any())).thenReturn(true)
        `when`(passwordEncoder.encode(any())).thenReturn("{encode}87654321")

        //when
        passwordEntity.updatePassword(oldPass, newPass, passwordEncoder)

        //then
        assertEquals("{encode}87654321", passwordEntity.getLastedPassword().password)
    }

    @Test
    fun updatePassword_Failed() {
        //given
        val passwordEncoder = mock<PasswordEncoder>()
        `when`(passwordEncoder.matches(any(), any())).thenReturn(false)

        //when
        assertThrows(ClientException::class.java) {
            passwordEntity.updatePassword(oldPass, newPass, passwordEncoder)
        }

        //then
        verify(passwordEncoder, never()).encode(any())
        assertEquals(encodePass, passwordEntity.getLastedPassword().password)
    }
}