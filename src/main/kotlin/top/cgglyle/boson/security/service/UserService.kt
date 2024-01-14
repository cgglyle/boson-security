package top.cgglyle.boson.security.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import top.cgglyle.boson.security.domain.RoleName
import top.cgglyle.boson.security.auth.domain.entity.UsernameAuthEntity
import top.cgglyle.boson.security.repository.RoleRepository
import top.cgglyle.boson.security.auth.domain.UsernameAuthRepository
import top.cgglyle.boson.security.auth.domain.command.CreateAuthCommand
import top.cgglyle.boson.security.common.exception.DataNotFoundException
import top.cgglyle.boson.security.common.exception.IllegalArgumentException
import top.cgglyle.boson.security.web.query.CreateUserQuery

@Service
class UserService(
    private val usernameAuthRepository: UsernameAuthRepository,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    fun createUser(query: CreateUserQuery): UsernameAuthEntity {
        val roleNames = query.roleNames
        val roleEntities = roleNames.map {
            roleRepository.findRoleEntityByRoleName(RoleName.valueOf(it))
                ?: throw DataNotFoundException("Role: $it not found!")
        }.toSet()
        if (query.username.isNullOrBlank() && query.email.isNullOrBlank()) {
            throw IllegalArgumentException("Try create account, but both username and email are null.")
        }
        val command = CreateAuthCommand(
            query.username ?: "",
            passwordEncoder.encode(query.password),
            query.email,
            roleEntities,
        )
        val usernameAuthEntity = UsernameAuthEntity(command)
        return usernameAuthRepository.save(usernameAuthEntity)
    }
}