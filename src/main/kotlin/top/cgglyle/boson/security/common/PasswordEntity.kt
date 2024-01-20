/*
 * Copyright 2024 Lyle Liu<cgglyle@outlook.com> and all contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.cgglyle.boson.security.common

import jakarta.persistence.*
import org.springframework.security.crypto.password.PasswordEncoder
import top.cgglyle.boson.security.exception.ClientException

@Entity
@Table(name = "sys_password")
class PasswordEntity(newPass: String, passwordEncoder: PasswordEncoder) : AbstractModifiedAuditingEntity() {

    @OneToMany(mappedBy = "passwordEntity", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
    private var passwordHistoryList: MutableSet<PasswordItem> = mutableSetOf()

    init {
        this.addPassword(newPass, passwordEncoder)
    }

    fun addPassword(password: String, passwordEncoder: PasswordEncoder): PasswordItem {
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