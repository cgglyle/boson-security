package top.cgglyle.boson.security.auth.domain.entity

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import top.cgglyle.boson.security.common.entity.password.PasswordEntity
import top.cgglyle.boson.security.user.domain.entity.Account

@Entity
@Table(name = "sys_local_auth")
class LocalAuthEntity(
    account: Account,
    @JoinColumn(name = "password_entity_database_id", nullable = false)
    @OneToOne(cascade = [CascadeType.ALL], optional = false, orphanRemoval = true)
    protected val passwordEntity: PasswordEntity,
) : BasicAuthEntity(account), UserDetails {

    fun addPassword(newPassword: String, passwordEncoder: PasswordEncoder) {
       passwordEntity.addPassword(newPassword, passwordEncoder)
    }

    override fun getPassword(): String {
        return this.passwordEntity.getLastedPassword().password
    }

    override fun getUsername(): String {
        return super.account.username
    }

    @Transient
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return super.account.getAuthorities()
    }

    @Transient
    override fun isAccountNonExpired(): Boolean {
        return super.account.accountNonExpired
    }

    @Transient
    override fun isAccountNonLocked(): Boolean {
        return super.account.accountNonLocked
    }

    @Transient
    override fun isCredentialsNonExpired(): Boolean {
        return super.account.credentialsNonExpired
    }

    @Transient
    override fun isEnabled(): Boolean {
        return super.account.enable
    }
}