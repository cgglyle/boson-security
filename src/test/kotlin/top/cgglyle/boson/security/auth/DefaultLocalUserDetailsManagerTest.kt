package top.cgglyle.boson.security.auth

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.context.SecurityContextHolderStrategy
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import top.cgglyle.boson.security.auth.domain.entity.LocalAuthEntity
import top.cgglyle.boson.security.auth.domain.entity.LocalAuthEntityRepository
import top.cgglyle.boson.security.common.exception.DataNotFoundException
import top.cgglyle.boson.security.common.exception.IllegalArgumentException

@ExtendWith(MockKExtension::class)
class DefaultLocalUserDetailsManagerTest {

    @MockK
    lateinit var localAuthEntityRepository: LocalAuthEntityRepository

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
        every { localAuthEntityRepository.findByUsername(okUsername) }.returns(mockk())
        every { localAuthEntityRepository.findByUsername(failUsername) }.returns(null)
        every { localAuthEntityRepository.save(any()) }.returns(mockk())
    }

    @Test
    fun loadUserByUsername() {
        // when
        val user = defaultLocalUserDetailsManager.loadUserByUsername(okUsername)

        // then
        assertNotNull(user)
    }

    @Test
    fun loadUserByUsernameFails() {
        // when
        assertThrows<DataNotFoundException> {
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

    @Test
    fun createUser_ok() {
        // given
        val userDetails = mockk<LocalAuthEntity>()

        // when
        defaultLocalUserDetailsManager.createUser(userDetails)

        // then
        verify { localAuthEntityRepository.save(any()) }
    }

    @Test
    fun createUser_userDetailNotLocalAuthEntityShouldThrowIllegalArgumentException() {
        // given
        val userDetails = mockk<UserDetails>()

        // when
        assertThrows<IllegalArgumentException> {
            defaultLocalUserDetailsManager.createUser(userDetails)
        }

        // then
        verify(exactly = 0) { localAuthEntityRepository.save(any()) }
    }

    @Test
    fun changePassword_shouldReAuthentication() {
        //given
        val oldPassword = "old password"
        val newPassword = "new password"

        val authenticationConfiguration = mockk<AuthenticationConfiguration>()
        val localAuthEntityRepository = mockk<LocalAuthEntityRepository>(relaxed = true)
        val manager = DefaultLocalUserDetailsManager(localAuthEntityRepository, authenticationConfiguration, mockk())

        val authenticationManager = mockk<AuthenticationManager>()
        every { authenticationConfiguration.authenticationManager }.returns(authenticationManager)
        every { authenticationManager.authenticate(any()) }.returns(mockk())
        mockkStatic(SecurityContextHolder::class)

        val securityContextHolderStrategy = mockk<SecurityContextHolderStrategy>(relaxed = true)
        every { SecurityContextHolder.getContextHolderStrategy() }.returns(securityContextHolderStrategy)
        every { securityContextHolderStrategy.context.authentication.name }.returns("username")
        every { localAuthEntityRepository.findByUsername(any()) }.returns(mockk(relaxed = true))
        every { localAuthEntityRepository.save(any()) }.returns(mockk())

        // when
        manager.changePassword(oldPassword, newPassword)

        // then
        verify { authenticationManager.authenticate(any()) }
    }

    @Test
    fun changePassword_shouldResetSecurityContext() {
        //given
        val oldPassword = "old password"
        val newPassword = "new password"

        val authenticationConfiguration = mockk<AuthenticationConfiguration>()
        val localAuthEntityRepository = mockk<LocalAuthEntityRepository>(relaxed = true)
        val manager = DefaultLocalUserDetailsManager(localAuthEntityRepository, authenticationConfiguration, mockk())

        val authenticationManager = mockk<AuthenticationManager>()
        every { authenticationConfiguration.authenticationManager }.returns(authenticationManager)
        every { authenticationManager.authenticate(any()) }.returns(mockk())
        mockkStatic(SecurityContextHolder::class)

        val securityContextHolderStrategy = mockk<SecurityContextHolderStrategy>(relaxed = true)
        every { SecurityContextHolder.getContextHolderStrategy() }.returns(securityContextHolderStrategy)
        every { securityContextHolderStrategy.context.authentication.name }.returns("username")
        every { localAuthEntityRepository.findByUsername(any()) }.returns(mockk(relaxed = true))
        every { localAuthEntityRepository.save(any()) }.returns(mockk())

        // when
        manager.changePassword(oldPassword, newPassword)

        // then
        verify { securityContextHolderStrategy.context = any() }
    }

    @Test
    fun changePassword_shouldSaveNewPassword() {
        //given
        val oldPassword = "old password"
        val newPassword = "new password"

        val authenticationConfiguration = mockk<AuthenticationConfiguration>()
        val localAuthEntityRepository = mockk<LocalAuthEntityRepository>(relaxed = true)
        val manager = DefaultLocalUserDetailsManager(localAuthEntityRepository, authenticationConfiguration, mockk())

        val authenticationManager = mockk<AuthenticationManager>()
        every { authenticationConfiguration.authenticationManager }.returns(authenticationManager)
        every { authenticationManager.authenticate(any()) }.returns(mockk())
        mockkStatic(SecurityContextHolder::class)

        val securityContextHolderStrategy = mockk<SecurityContextHolderStrategy>(relaxed = true)
        every { SecurityContextHolder.getContextHolderStrategy() }.returns(securityContextHolderStrategy)
        every { securityContextHolderStrategy.context.authentication.name }.returns("username")
        every { localAuthEntityRepository.findByUsername(any()) }.returns(mockk<LocalAuthEntity>(relaxed = true))
        every { localAuthEntityRepository.save(any()) }.returns(mockk())

        // when
        manager.changePassword(oldPassword, newPassword)

        // then
        verify { localAuthEntityRepository.save(any<LocalAuthEntity>()) }
    }

    @Test
    fun changePassword_shouldOnlyChangeSelfPassword() {
        //given
        val oldPassword = "old password"
        val newPassword = "new password"

        val authenticationConfiguration = mockk<AuthenticationConfiguration>()
        val localAuthEntityRepository = mockk<LocalAuthEntityRepository>(relaxed = true)
        val manager = DefaultLocalUserDetailsManager(localAuthEntityRepository, authenticationConfiguration, mockk())

        val authenticationManager = mockk<AuthenticationManager>()
        every { authenticationConfiguration.authenticationManager }.returns(authenticationManager)
        every { authenticationManager.authenticate(any()) }.returns(mockk())
        mockkStatic(SecurityContextHolder::class)

        val securityContextHolderStrategy = mockk<SecurityContextHolderStrategy>(relaxed = true)
        every { SecurityContextHolder.getContextHolderStrategy() }.returns(securityContextHolderStrategy)
        every { securityContextHolderStrategy.context.authentication.name }.returns("username")
        every { localAuthEntityRepository.findByUsername(any()) }.returns(mockk<LocalAuthEntity>(relaxed = true))
        every { localAuthEntityRepository.save(any()) }.returns(mockk())

        // when
        manager.changePassword(oldPassword, newPassword)

        // then
        verify { localAuthEntityRepository.findByUsername("username") }
    }

    @Test
    fun changePassword_shouldCannotChangeUnAuthenticationUserPassword() {
        //given
        val oldPassword = "old password"
        val newPassword = "new password"

        val authenticationConfiguration = mockk<AuthenticationConfiguration>()
        val localAuthEntityRepository = mockk<LocalAuthEntityRepository>(relaxed = true)
        val manager = DefaultLocalUserDetailsManager(localAuthEntityRepository, authenticationConfiguration, mockk())

        val authenticationManager = mockk<AuthenticationManager>()
        every { authenticationConfiguration.authenticationManager }.returns(authenticationManager)
        every { authenticationManager.authenticate(any()) }.returns(mockk())
        mockkStatic(SecurityContextHolder::class)

        val securityContextHolderStrategy = mockk<SecurityContextHolderStrategy>(relaxed = true)
        every { SecurityContextHolder.getContextHolderStrategy() }.returns(securityContextHolderStrategy)
        every { securityContextHolderStrategy.context.authentication }.returns(null)
        every { localAuthEntityRepository.findByUsername(any()) }.returns(mockk<LocalAuthEntity>(relaxed = true))
        every { localAuthEntityRepository.save(any()) }.returns(mockk())

        // when
        assertThrows<AccessDeniedException> {
            manager.changePassword(oldPassword, newPassword)
        }

        // then
        verify(exactly = 0) {
            localAuthEntityRepository.findByUsername(any())
            authenticationManager.authenticate(any())
            securityContextHolderStrategy.context = any()
            localAuthEntityRepository.save(any())
        }
    }

    @Test
    fun deleteUser() {
        // then
        every { localAuthEntityRepository.delete(any()) }.returns(Unit)

        // when
        defaultLocalUserDetailsManager.deleteUser(okUsername)

        // then
        verify { localAuthEntityRepository.delete(any()) }
    }

    @Test
    fun userExists() {
        // then
        every { localAuthEntityRepository.userExists("username") }.returns(true)
        every { localAuthEntityRepository.userExists("fail") }.returns(false)

        // when
        val username = defaultLocalUserDetailsManager.userExists("username")
        val fail = defaultLocalUserDetailsManager.userExists("fail")

        // then
        assertTrue(username)
        assertFalse(fail)
    }
}