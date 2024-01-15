package top.cgglyle.boson.security.common.entity.password

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mockito.*
import org.springframework.security.crypto.password.PasswordEncoder
import top.cgglyle.boson.security.common.exception.ClientException

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