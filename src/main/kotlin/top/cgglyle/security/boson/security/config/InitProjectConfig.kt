package top.cgglyle.security.boson.security.config

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.Transactional
import top.cgglyle.security.boson.security.domain.RoleName
import top.cgglyle.security.boson.security.domain.entity.RoleEntity
import top.cgglyle.security.boson.security.repository.RoleRepository
import top.cgglyle.security.boson.security.service.UserService
import top.cgglyle.security.boson.security.web.query.CreateUserQuery

@Configuration
class InitProjectConfig(
    val roleRepository: RoleRepository,
    val userService: UserService,
) : ApplicationRunner {

    @Transactional
    override fun run(args: ApplicationArguments?) {
        if (roleRepository.count() != 0L) {
            return
        }

        RoleName.entries.forEach {
            roleRepository.save(RoleEntity(it))
        }

        userService.createUser(CreateUserQuery("user", "user", mutableSetOf("ADMIN")))
    }
}