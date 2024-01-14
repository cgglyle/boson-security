package top.cgglyle.boson.security.service

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import top.cgglyle.boson.security.auth.domain.LocalAuthRepository

@Service
class DefaultUserDetailsService(
    private val localAuthRepository: LocalAuthRepository,
) : UserDetailsService {

    @Transactional
    override fun loadUserByUsername(username: String): UserDetails {
        return localAuthRepository.findByAccountUsername(username)
            ?: throw UsernameNotFoundException("Username: $username does not found")
    }
}