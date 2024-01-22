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

package top.cgglyle.boson.security.account

import top.cgglyle.boson.security.account.domain.Account
import top.cgglyle.boson.security.authorization.RID
import top.cgglyle.boson.security.common.UID
import java.io.Serializable
import java.time.Instant

/**
 * DTO for {@link top.cgglyle.boson.security.account.domain.Account}
 */
data class AccountDto(
    val id: Long = -1,
    val createdBy: UID = UID.defaultUID(),
    val createdDate: Instant = Instant.now(),
    val lastModifiedBy: UID? = null,
    val lastModifiedDate: Instant? = null,
    val uid: UID = UID.randomUID(),
    val username: String = "",
    val email: String = "",
    val roles: MutableSet<RID> = mutableSetOf(),
    val accountExpired: Boolean = true,
    val accountLocked: Boolean = true,
    val enable: Boolean = true
) : Serializable {
    companion object {
        fun from(account: Account?): AccountDto? {
            if (account == null) {
                return null
            }
            return with(account) {
                AccountDto(
                    id,
                    createdBy,
                    createdDate,
                    lastModifiedBy,
                    lastModifiedDate,
                    uid,
                    username,
                    email,
                    roles,
                    expired,
                    locked,
                    enable
                )
            }
        }
    }
}