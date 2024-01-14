package top.cgglyle.boson.security.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import top.cgglyle.boson.security.domain.RoleName
import top.cgglyle.boson.security.domain.command.CreateUserCommand
import top.cgglyle.boson.security.auth.domain.entity.LocalAuthEntity
import top.cgglyle.boson.security.repository.RoleRepository
import top.cgglyle.boson.security.auth.domain.LocalAuthRepository
import top.cgglyle.boson.security.web.query.CreateUserQuery
import java.lang.RuntimeException

@Service
class UserService(
    private val localAuthRepository: LocalAuthRepository,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    fun createUser(query: CreateUserQuery): LocalAuthEntity {
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
        val localAuthEntity = LocalAuthEntity(command)
        return localAuthRepository.save(localAuthEntity)
    }
}