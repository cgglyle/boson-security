package top.cgglyle.boson.security.common.entity.password

import jakarta.persistence.*
import top.cgglyle.boson.security.common.entity.basic.AbstractModifiedAuditingEntity
import top.cgglyle.boson.security.common.exception.ClientException
import top.cgglyle.boson.security.user.domain.entity.UID

@Entity
@Table(name = "sys_password")
class PasswordEntity() : AbstractModifiedAuditingEntity(){


    @OneToMany(mappedBy = "passwordEntity", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
    var passwordHistoryList: MutableSet<PasswordItem> = mutableSetOf()

    fun addPassword(password: String, createUid: UID): PasswordItem {
        val passwordItem = PasswordItem(password, createUid, this)
        this.passwordHistoryList.add(passwordItem)
        return passwordItem
    }

    fun getLastedPassword(): PasswordItem {
        passwordHistoryList.ifEmpty { throw ClientException("Try get password, but not anyone password.") }
        return passwordHistoryList.maxBy { it.createDate }
    }
}