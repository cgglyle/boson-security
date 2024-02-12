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

package top.cgglyle.boson.security.auth

import org.springframework.security.core.CredentialsContainer
import org.springframework.security.core.userdetails.UserDetails
import top.cgglyle.boson.security.common.UID

/**
 * @author: Lyle Liu
 */
data class UidDetailUser(
    val uid: UID,
    private val userDetails: UserDetails,
) : UserDetails by userDetails, CredentialsContainer by userDetails as CredentialsContainer