package top.cgglyle.boson.security.service

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import top.cgglyle.boson.security.auth.domain.EmailAuthRepository
import top.cgglyle.boson.security.auth.domain.UsernameAuthRepository
import top.cgglyle.boson.security.auth.domain.entity.BasicLocalAuthEntity

@Service
class DefaultUserDetailsService(
    private val usernameAuthRepository: UsernameAuthRepository,
    private val emailAuthRepository: EmailAuthRepository,
) : UserDetailsService {

    @Transactional
    override fun loadUserByUsername(username: String): UserDetails {
        val usernameAuthEntity: BasicLocalAuthEntity? =
            usernameAuthRepository.findByAccountUsername(username)
                ?: emailAuthRepository.findByAccountEmail(username)
        return usernameAuthEntity ?: throw UsernameNotFoundException("Account: $username does not found")
    }
}