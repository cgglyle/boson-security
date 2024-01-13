package top.cgglyle.boson.security.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import top.cgglyle.boson.security.domain.RoleName
import top.cgglyle.boson.security.domain.command.CreateUserCommand
import top.cgglyle.boson.security.domain.entity.UserEntity
import top.cgglyle.boson.security.repository.RoleRepository
import top.cgglyle.boson.security.repository.UserRepository
import top.cgglyle.boson.security.web.query.CreateUserQuery
import java.lang.RuntimeException

@Service
class UserService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    fun createUser(query: CreateUserQuery) {
        val roleNames = query.roleNames
        val roleEntities = roleNames.map {
            roleRepository.findRoleEntityByRoleName(RoleName.valueOf(it))
                ?: throw RuntimeException("Role: $it not found!")
        }.toSet()
        val command = CreateUserCommand(
            query.username,
            passwordEncoder.encode(query.password),
            roleEntities,
        )
        val userEntity = UserEntity(command)
        userRepository.save(userEntity)
    }
}