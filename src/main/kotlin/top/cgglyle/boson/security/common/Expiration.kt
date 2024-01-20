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

import jakarta.persistence.Embeddable
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

/**
 * @author: Lyle Liu
 */
@Embeddable
class Expiration(
    val time: Instant
) {
    companion object {
        val NEVER: Expiration = Expiration(
            LocalDateTime.of(3000, 1, 1, 1, 1, 1).toInstant(ZoneOffset.UTC)
        )
        val MIN: Expiration = Expiration(Instant.EPOCH)
    }

    fun isExpired(): Boolean {
        return this.time.isAfter(Instant.now())
    }
}