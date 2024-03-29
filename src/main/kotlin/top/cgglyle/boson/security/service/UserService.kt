package top.cgglyle.boson.security.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import top.cgglyle.boson.security.auth.domain.EmailAuthRepository
import top.cgglyle.boson.security.auth.domain.UsernameAuthRepository
import top.cgglyle.boson.security.auth.domain.entity.EmailAuthEntity
import top.cgglyle.boson.security.auth.domain.entity.UsernameAuthEntity
import top.cgglyle.boson.security.common.entity.password.PasswordEntity
import top.cgglyle.boson.security.common.exception.DataExistException
import top.cgglyle.boson.security.common.exception.DataNotFoundException
import top.cgglyle.boson.security.common.exception.IllegalArgumentException
import top.cgglyle.boson.security.domain.RoleName
import top.cgglyle.boson.security.repository.RoleRepository
import top.cgglyle.boson.security.user.domain.command.CreateAccountCommand
import top.cgglyle.boson.security.user.domain.entity.Account
import top.cgglyle.boson.security.user.domain.entity.AccountRepository
import top.cgglyle.boson.security.web.query.CreateUserQuery

@Service
class UserService(
    private val usernameAuthRepository: UsernameAuthRepository,
    private val emailAuthRepository: EmailAuthRepository,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder,
    private val accountRepository: AccountRepository,
) {

    @Transactional
    fun createUser(query: CreateUserQuery): Account {
        if (query.username.isNullOrBlank() && query.email.isNullOrBlank()) {
            throw IllegalArgumentException("Try create account, but both username and email are null.")
        }
        if (accountRepository.existsByUsernameOrEmail(query.username, query.email)) {
            throw DataExistException("Account ${query.username} or ${query.email} exist!")
        }
        val roleNames = query.roleNames
        val roleEntities = roleNames.map {
            roleRepository.findRoleEntityByRoleName(RoleName.valueOf(it))
                ?: throw DataNotFoundException("Role: $it not found!")
        }.toSet()

        val command = CreateAccountCommand(
            query.username,
            query.email,
            roleEntities,
        )
        val account = accountRepository.save(Account(command))

        val passwordEntity = PasswordEntity(query.password, passwordEncoder)

        if (!query.username.isNullOrBlank()) {
            usernameAuthRepository.save(UsernameAuthEntity(passwordEntity, account))
        }


        if (!query.email.isNullOrBlank()) {
            emailAuthRepository.save(EmailAuthEntity(passwordEntity, account))
        }

        return account
    }
}