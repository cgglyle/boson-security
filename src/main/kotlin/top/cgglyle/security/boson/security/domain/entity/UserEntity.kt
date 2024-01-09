package top.cgglyle.security.boson.security.domain.entity

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import top.cgglyle.security.boson.security.common.AbstractIDEntity
import top.cgglyle.security.boson.security.domain.command.CreateUserCommand

@Entity
@Table(name = "app_user")
class UserEntity(
    private val username: String = "",
    private val password: String = "",
) : AbstractIDEntity(), UserDetails {
    @ManyToMany(cascade = [CascadeType.MERGE], fetch = FetchType.EAGER)
    @JoinTable(
        name = "app_user_role",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    private val roleEntities: MutableSet<RoleEntity> = mutableSetOf()
    private val accountNonExpired: Boolean = false
    private val accountNonLocked: Boolean = false
    private val credentialsNonExpired: Boolean = false
    private val enable: Boolean = false

    constructor(command: CreateUserCommand) : this(command.username, command.password) {
        command.roles?.let { this.roleEntities.addAll(it) }
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return this.roleEntities
    }

    override fun getPassword(): String {
        return this.password
    }

    override fun getUsername(): String {
        return this.username
    }

    override fun isAccountNonExpired(): Boolean {
        return !this.accountNonExpired
    }

    override fun isAccountNonLocked(): Boolean {
        return !this.accountNonLocked
    }

    override fun isCredentialsNonExpired(): Boolean {
        return !this.credentialsNonExpired
    }

    override fun isEnabled(): Boolean {
        return !this.enable
    }
}