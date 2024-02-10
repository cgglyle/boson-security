/*
 * Copyright 2024 Lyle Liu<cgglyle@outlook.com> and all contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.cgglyle.boson.security.client

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.transaction.annotation.Transactional
import top.cgglyle.boson.security.account.CreateAccountQuery
import top.cgglyle.boson.security.auth.AuthUserManager
import top.cgglyle.boson.security.auth.CurrentLoginUidUtil
import top.cgglyle.boson.security.authorization.CreateRoleDto
import top.cgglyle.boson.security.authorization.RoleFindable
import top.cgglyle.boson.security.authorization.RoleManager
import top.cgglyle.boson.security.authorization.RoleName
import top.cgglyle.boson.security.client.domain.ClientRepository
import top.cgglyle.boson.security.client.service.ClientService

@Configuration
class InitProjectConfig(
    val roleManager: RoleManager,
    val roleFindable: RoleFindable,
    val authUserManager: AuthUserManager,
    val clientService: ClientService, private val clientRepository: ClientRepository,
) : ApplicationRunner {

    @Transactional
    override fun run(args: ApplicationArguments?) {
        initSecurityUser()
        initUser()
        initClient()
    }

    fun initSecurityUser() {
        val contextHolderStrategy = SecurityContextHolder.getContextHolderStrategy()
        contextHolderStrategy.context.authentication = CurrentLoginUidUtil.newSystemAuthenticationToken()
    }

    fun initUser() {
        if (roleFindable.count() != 0L) {
            return
        }

        RoleName.entries.forEach { roleManager.create((CreateRoleDto(it))) }

        authUserManager.createUser(
            CreateAccountQuery(
                "user", "user@user.com", "username", mutableSetOf("ADMIN")
            )
        )
    }

    fun initClient() {
        if (clientRepository.count() != 0L) {
            return
        }

        clientService.save(
            CreateClientCommand(
                "client", "secret", mutableSetOf(ClientAuthenticationMethod.CLIENT_SECRET_BASIC), mutableSetOf(
                    AuthorizationGrantType.AUTHORIZATION_CODE,
                    AuthorizationGrantType.CLIENT_CREDENTIALS,
                    AuthorizationGrantType.REFRESH_TOKEN
                ), mutableSetOf("https://oauthdebugger.com/debug"), mutableSetOf("openid"), true
            )
        )
    }
}