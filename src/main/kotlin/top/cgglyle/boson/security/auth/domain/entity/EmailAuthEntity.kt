package top.cgglyle.boson.security.auth.domain.entity

import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.springframework.security.core.userdetails.UserDetails
import top.cgglyle.boson.security.common.entity.password.PasswordEntity
import top.cgglyle.boson.security.user.domain.entity.Account

@Entity
@Table(name = "sys_local_email_auth")
class EmailAuthEntity(
    password: PasswordEntity,
    account: Account,
) : BasicLocalAuthEntity(account, password), UserDetails {
    override fun formOtherLocalAuth(basicLocalAuthEntity: BasicLocalAuthEntity): BasicLocalAuthEntity {
        return UsernameAuthEntity(basicLocalAuthEntity.passwordEntity, basicLocalAuthEntity.account)
    }

    override fun getUsername(): String {
        return super.account.email
    }
}