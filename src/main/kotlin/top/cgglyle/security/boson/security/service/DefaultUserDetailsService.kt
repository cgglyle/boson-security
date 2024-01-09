package top.cgglyle.security.boson.security.service

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import top.cgglyle.security.boson.security.repository.UserRepository

@Service
class DefaultUserDetailsService(
    private val userRepository: UserRepository,
) : UserDetailsService {

    @Transactional
    override fun loadUserByUsername(username: String): UserDetails {
        return userRepository.findUserEntityByUsername(username)
            ?: throw UsernameNotFoundException("Username: $username does not found")
    }
}