package top.cgglyle.boson.security.common.entity.password

import jakarta.persistence.*
import org.springframework.security.crypto.password.PasswordEncoder
import top.cgglyle.boson.security.common.entity.basic.AbstractModifiedAuditingEntity
import top.cgglyle.boson.security.common.exception.ClientException

@Entity
@Table(name = "sys_password")
class PasswordEntity(newPass: String, passwordEncoder: PasswordEncoder) : AbstractModifiedAuditingEntity() {

    @OneToMany(mappedBy = "passwordEntity", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
    private var passwordHistoryList: MutableSet<PasswordItem> = mutableSetOf()

    init {
        addPassword(newPass, passwordEncoder)
    }

    private fun addPassword(password: String, passwordEncoder: PasswordEncoder): PasswordItem {
        val encodePass = passwordEncoder.encode(password)
        val passwordItem = PasswordItem(encodePass, this)
        this.passwordHistoryList.add(passwordItem)
        return passwordItem
    }

    fun getLastedPassword(): PasswordItem {
        passwordHistoryList.ifEmpty { throw ClientException("Try get password, but not anyone password.") }
        return passwordHistoryList.maxBy { it.createdDate }
    }

    fun updatePassword(oldPass: String, newPass: String, passwordEncoder: PasswordEncoder) {
        val lastedPassword = getLastedPassword()
        if (passwordEncoder.matches(oldPass, lastedPassword.password)) {
            addPassword(newPass, passwordEncoder)
        } else {
            throw ClientException("Password is wrong! Do nothing.")
        }
    }
}