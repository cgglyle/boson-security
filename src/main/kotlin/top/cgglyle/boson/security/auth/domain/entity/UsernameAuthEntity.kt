package top.cgglyle.boson.security.auth.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.springframework.security.core.userdetails.UserDetails
import top.cgglyle.boson.security.auth.domain.command.CreateAuthCommand
import top.cgglyle.boson.security.user.domain.command.CreateAccountCommand
import top.cgglyle.boson.security.user.domain.entity.Account
import java.util.*

@Entity
@Table(name = "sys_local_username_auth")
class UsernameAuthEntity(
    command: CreateAuthCommand
) : BasicLocalAuthEntity(
    Account(
        CreateAccountCommand(
            command.username, null, command.roles
        )
    )
), UserDetails {

    @Column(name = "password", nullable = false)
    private var password: String = UUID.randomUUID().toString()

    init {
        this.password = command.password
    }

    override fun getPassword(): String {
        return this.password
    }

    override fun getUsername(): String {
        return super.account.username
    }
}