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

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length
import top.cgglyle.boson.security.authorization.RID
import top.cgglyle.boson.security.common.Expiration
import java.io.Serializable

/**
 * DTO for {@link top.cgglyle.boson.security.account.domain.Account}
 */
data class CreateAccountDto(
    @NotBlank @Length(max = 64)
    val username: String?,
    @Email @Length(max = 64)
    val email: String?,
    val roles: Set<RID> = mutableSetOf(),
    val expiration: Expiration = Expiration.NEVER,
    val locked: Boolean = false,
    val enable: Boolean = true
) : Serializable