package top.cgglyle.boson.security.user.domain.entity

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import top.cgglyle.boson.security.common.entity.AbstractModifiedAuditingEntity
import top.cgglyle.boson.security.common.exception.IllegalArgumentException
import top.cgglyle.boson.security.domain.entity.RoleEntity
import java.util.*

@Entity
@Table(name = "sys_account")
class Account(
    username: String,
    roles: Set<RoleEntity>,
    isAccountNonLocked: Boolean = true,
    isEnable: Boolean = true,
) : AbstractModifiedAuditingEntity() {
    @Column(name = "uid", updatable = false, nullable = false, unique = true)
    val uid: String = UUID.randomUUID().toString()

    @Column(name = "username", nullable = false, unique = true)
    var username: String = ""
        protected set

    @ManyToMany(cascade = [CascadeType.MERGE], fetch = FetchType.EAGER)
    @JoinTable(
        name = "sys_account_role",
        joinColumns = [JoinColumn(name = "account_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    private val roleEntities: MutableSet<RoleEntity> = mutableSetOf()

    @Column(name = "account_non_expired", nullable = false)
    var accountNonExpired: Boolean = true
        protected set

    @Column(name = "account_non_locked", nullable = false)
    final var accountNonLocked: Boolean = true
        protected set

    @Column(name = "credentials_non_expired", nullable = false)
    var credentialsNonExpired: Boolean = true
        protected set

    @Column(name = "enable", nullable = false)
    final var enable: Boolean = true
        protected set

    init {
        this.username = username
        this.roleEntities.clear()
        this.roleEntities.addAll(roles)
        this.accountNonLocked = isAccountNonLocked
        this.enable = isEnable
    }

    companion object {
        fun defaultAccount(): Account {
            return Account(UUID.randomUUID().toString(), mutableSetOf(RoleEntity()))
        }
    }

    fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return this.roleEntities
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