package top.cgglyle.boson.security.auth.domain.entity

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import top.cgglyle.boson.security.common.entity.password.PasswordEntity
import top.cgglyle.boson.security.user.domain.entity.Account

@MappedSuperclass
abstract class BasicLocalAuthEntity(
    account: Account,
    @JoinColumn(name = "password_entity_database_id", nullable = false)
    @OneToOne(cascade = [CascadeType.ALL], optional = false, orphanRemoval = true)
    val passwordEntity: PasswordEntity,
) : BasicAuthEntity(account), UserDetails {

    override fun getPassword(): String {
        return this.passwordEntity.getLastedPassword().password
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

    abstract fun formOtherLocalAuth(basicLocalAuthEntity: BasicLocalAuthEntity): BasicLocalAuthEntity
}