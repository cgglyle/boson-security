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
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import top.cgglyle.boson.security.common.Expiration

data class CreateAccountQuery(
    @Max(64)
    val username: String?,
    @Max(64)
    @Email
    val email: String?,
    @NotBlank
    @Size(min = 4, max = 128)
    val password: String,
    val roleNames: Set<String>,
    val enable: Boolean = true,
    val locked: Boolean = false,
    val accountExpiration: Expiration = Expiration.NEVER,
    val credentialExpiration: Expiration = Expiration.NEVER,
)
