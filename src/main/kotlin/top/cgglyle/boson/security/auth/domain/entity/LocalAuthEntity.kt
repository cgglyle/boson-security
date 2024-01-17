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