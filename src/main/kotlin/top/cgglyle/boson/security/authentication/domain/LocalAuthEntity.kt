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

package top.cgglyle.boson.security.authentication.domain

import jakarta.persistence.*
import org.springframework.security.crypto.password.PasswordEncoder
import top.cgglyle.boson.security.common.Expiration
import top.cgglyle.boson.security.common.PasswordEntity
import top.cgglyle.boson.security.common.UID

@Entity
@Table(name = "sys_local_auth")
class LocalAuthEntity(
    uid: UID,
    @JoinColumn(name = "password_entity_database_id", nullable = false)
    @OneToOne(cascade = [CascadeType.ALL], optional = false, orphanRemoval = true)
    protected val passwordEntity: PasswordEntity,
) : BasicAuthEntity(uid) {
    @AttributeOverride(name = "time", column = Column(name = "expiration", nullable = false))
    private var expiration: Expiration = Expiration.NEVER

    fun addPassword(newPassword: String, passwordEncoder: PasswordEncoder) {
        passwordEntity.addPassword(newPassword, passwordEncoder)
    }

    fun getPassword(): String {
        return this.passwordEntity.getLastedPassword().password
    }

    fun isCredentialsNonExpired(): Boolean {
        return this.expiration.isExpired()
    }

    fun setExpiration(expiration: Expiration) {
        this.expiration = expiration
    }
}