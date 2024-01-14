package top.cgglyle.boson.security.service

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import top.cgglyle.boson.security.auth.domain.UsernameAuthRepository

@Service
class DefaultUserDetailsService(
    private val usernameAuthRepository: UsernameAuthRepository,
) : UserDetailsService {

    @Transactional
    override fun loadUserByUsername(username: String): UserDetails {
        return usernameAuthRepository.findByAccountUsername(username)
            ?: throw UsernameNotFoundException("Username: $username does not found")
    }
}