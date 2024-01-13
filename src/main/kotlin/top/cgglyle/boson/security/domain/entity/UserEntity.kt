package top.cgglyle.boson.security.domain.entity

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import top.cgglyle.boson.security.common.entity.AbstractModifiedAuditingEntity
import top.cgglyle.boson.security.common.exception.IllegalArgumentException
import top.cgglyle.boson.security.domain.command.CreateUserCommand

@Entity
@Table(
    name = "app_user",
    uniqueConstraints = [
        UniqueConstraint(name = "uk_username", columnNames = ["username"])
    ]
)
class UserEntity(
    @Column(name = "username", nullable = false)
    private var username: String = "",

    @Column(name = "password", nullable = false)
    private var password: String = "",
) : AbstractModifiedAuditingEntity(), UserDetails {
    @ManyToMany(cascade = [CascadeType.MERGE], fetch = FetchType.EAGER)
    @JoinTable(
        name = "app_user_role",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    private val roleEntities: MutableSet<RoleEntity> = mutableSetOf()

    @Column(name = "account_non_expired", nullable = false)
    private val accountNonExpired: Boolean = false

    @Column(name = "account_non_locked", nullable = false)
    private val accountNonLocked: Boolean = false

    @Column(name = "credentials_non_expired", nullable = false)
    private val credentialsNonExpired: Boolean = false

    @Column(name = "enable", nullable = false)
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

    fun updateUsername(value: String) {
        if (value.isBlank()) {
            throw IllegalArgumentException("Update username must not be blank!")
        }
        if (value.length > 64) {
            throw IllegalArgumentException("Update username length must no be long 64")
        }
        this.username = value
    }
}