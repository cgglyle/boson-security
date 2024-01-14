package top.cgglyle.boson.security.config

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.transaction.annotation.Transactional
import top.cgglyle.boson.security.domain.RoleName
import top.cgglyle.boson.security.domain.command.CreateClientCommand
import top.cgglyle.boson.security.domain.entity.RoleEntity
import top.cgglyle.boson.security.repository.ClientRepository
import top.cgglyle.boson.security.repository.RoleRepository
import top.cgglyle.boson.security.service.ClientService
import top.cgglyle.boson.security.service.UserService
import top.cgglyle.boson.security.web.query.CreateUserQuery

@Configuration
class InitProjectConfig(
    val roleRepository: RoleRepository,
    val userService: UserService,
    val clientService: ClientService, private val clientRepository: ClientRepository,
) : ApplicationRunner {

    @Transactional
    override fun run(args: ApplicationArguments?) {
        initUser()
        initClient()
    }

    fun initUser() {
        if (roleRepository.count() != 0L) {
            return
        }

        RoleName.entries.forEach {
            roleRepository.save(RoleEntity(it))
        }

        userService.createUser(
            CreateUserQuery(
                "user",
                null,
                "username",
                mutableSetOf("ADMIN")
            )
        )
    }

    fun initClient() {
        if (clientRepository.count() != 0L) {
            return
        }

        clientService.save(
            CreateClientCommand(
                "client",
                "secret",
                mutableSetOf(ClientAuthenticationMethod.CLIENT_SECRET_BASIC),
                mutableSetOf(
                    AuthorizationGrantType.AUTHORIZATION_CODE,
                    AuthorizationGrantType.CLIENT_CREDENTIALS,
                    AuthorizationGrantType.REFRESH_TOKEN
                ),
                mutableSetOf("https://oauthdebugger.com/debug"),
                mutableSetOf("openid"),
                true
            )
        )
    }
}