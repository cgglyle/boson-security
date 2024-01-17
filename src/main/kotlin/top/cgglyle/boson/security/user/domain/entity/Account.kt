package top.cgglyle.boson.security.user.domain.entity

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length
import org.springframework.security.core.GrantedAuthority
import top.cgglyle.boson.security.common.entity.basic.AbstractModifiedAuditingEntity
import top.cgglyle.boson.security.common.exception.IllegalArgumentException
import top.cgglyle.boson.security.domain.entity.RoleEntity
import top.cgglyle.boson.security.user.domain.command.CreateAccountCommand
import top.cgglyle.boson.security.user.domain.entity.event.CreateAccountEvent
import java.util.*

@Entity
@Table(name = "sys_account")
class Account(command: CreateAccountCommand) : AbstractModifiedAuditingEntity() {
    @Embedded
    @AttributeOverride(
        name = "value",
        column = Column(name = "uid", updatable = false, nullable = false, unique = true)
    )
    val uid: UID = UID.randomUID()

    @NotBlank
    @Length(max = 64)
    @Column(name = "username", nullable = false, unique = true)
    var username: String = ""
        protected set

    @Email
    @Length(max = 64)
    @Column(name = "email", unique = true)
    var email: String = ""
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
    var accountNonLocked: Boolean = true
        protected set

    @Column(name = "credentials_non_expired", nullable = false)
    var credentialsNonExpired: Boolean = true
        protected set

    @Column(name = "enable", nullable = false)
    var enable: Boolean = true
        protected set

    init {
        if (command.username.isNullOrBlank() && command.email.isNullOrBlank()) {
            throw IllegalArgumentException("Try create account, but both username and email are null.")
        }
        (if (command.username.isNullOrBlank()) {
            UUID.randomUUID().toString()
        } else command.username).also { this.username = it }
        this.roleEntities.clear()
        this.roleEntities.addAll(command.roles)
        if (!command.email.isNullOrBlank()) {
            command.email.also { this.email = it }
        }
        command.isAccountNonLocked.also { this.accountNonLocked = it }
        command.isEnable.also { this.enable = it }

        super.registerEvent(CreateAccountEvent(this))
    }


    companion object {
        fun defaultAccount(): Account {
            return Account(CreateAccountCommand(UUID.randomUUID().toString(), null, mutableSetOf(RoleEntity())))
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