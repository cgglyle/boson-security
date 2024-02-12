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
import java.io.Serializable
import java.time.Instant

/**
 * DTO for {@link top.cgglyle.boson.security.account.domain.Account}
 */
data class AccountDto(
    val id: Long,
    val createdBy: String,
    val createdDate: Instant,
    val lastModifiedBy: String?,
    val lastModifiedDate: Instant?,
    val uid: String,
    val username: String,
    val email: String,
    val roles: MutableSet<RID>,
    val accountExpired: Boolean,
    val accountLocked: Boolean,
    val enable: Boolean,
) : Serializable {
    companion object {
        fun from(account: Account?): AccountDto? {
            if (account == null) {
                return null
            }
            return with(account) {
                AccountDto(
                    id,
                    createdBy.value,
                    createdDate,
                    lastModifiedBy?.value,
                    lastModifiedDate,
                    uid.value,
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