package top.cgglyle.boson.security.auth.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import top.cgglyle.boson.security.domain.command.CreateUserCommand
import top.cgglyle.boson.security.domain.entity.RoleEntity
import top.cgglyle.boson.security.user.domain.entity.Account
import java.util.*

@Entity
@Table(name = "sys_auth_local")
class LocalAuthEntity(
    username: String,
    password: String,
    roles: Set<RoleEntity>
) : BasicAuthEntity(Account(username, roles)), UserDetails {

    @Column(name = "password", nullable = false)
    private var password: String = UUID.randomUUID().toString()

    constructor(command: CreateUserCommand) : this(
        command.username,
        command.password,
        command.roles ?: setOf(RoleEntity())
    )

    init {
        this.password = password
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return this.account.getAuthorities()
    }

    override fun getPassword(): String {
        return this.password
    }

    override fun getUsername(): String {
        return super.account.username
    }

    override fun isAccountNonExpired(): Boolean {
        return super.account.accountNonExpired
    }

    override fun isAccountNonLocked(): Boolean {
        return super.account.accountNonLocked
    }

    override fun isCredentialsNonExpired(): Boolean {
        return super.account.credentialsNonExpired
    }

    override fun isEnabled(): Boolean {
        return super.account.enable
    }

}