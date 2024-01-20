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

package top.cgglyle.boson.security.account.domain

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length
import top.cgglyle.boson.security.account.CreateAccountCommand
import top.cgglyle.boson.security.account.CreateAccountEvent
import top.cgglyle.boson.security.authorization.RID
import top.cgglyle.boson.security.common.AbstractModifiedAuditingEntity
import top.cgglyle.boson.security.common.Expiration
import top.cgglyle.boson.security.common.UID
import top.cgglyle.boson.security.exception.IllegalArgumentException
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

    @ElementCollection
    @JoinTable(name = "sys_account_role")
    val roles: MutableSet<RID> = mutableSetOf()


    @AttributeOverride(name = "time", column = Column(name = "account_expiration", nullable = false))
    private var accountExpiration: Expiration = Expiration.NEVER

    @get:Transient
    val expired: Boolean
        get() = accountExpiration.isExpired()

    @Column(name = "locked", nullable = false)
    var locked: Boolean = false
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
        this.roles.clear()
        this.roles.addAll(command.roles)
        if (!command.email.isNullOrBlank()) {
            command.email.also { this.email = it }
        }
        command.isLocked.also { this.locked = it }
        command.isEnable.also { this.enable = it }

        super.registerEvent(CreateAccountEvent(this))
    }

    companion object {
        fun defaultAccount(): Account {
            return Account(CreateAccountCommand(UUID.randomUUID().toString(), null, mutableSetOf()))
        }
    }
}