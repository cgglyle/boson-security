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

package top.cgglyle.boson.security.web.query

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length

/**
 * @author: Lyle Liu
 */
data class RegisterQuery(
    @field:NotBlank
    @field:Length(min = 2, max = 64)
    val username: String?,

    @field:NotBlank
    @field:Length(max = 64)
    @field:Email
    val email: String?,

    @field:NotBlank
    @field:Length(min = 4, max = 64)
    val password: String,
)
