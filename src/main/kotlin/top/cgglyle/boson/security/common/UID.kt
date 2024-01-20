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
import java.io.Serializable
import java.util.*

@Embeddable
data class UID(
    val value: String
) : Serializable {
    companion object {
        fun randomUID(): UID {
            return UID(UUID.randomUUID().toString())
        }

        fun defaultUID(): UID {
            return UID("[UNINITIALIZED DEFAULT UID] ${UUID.randomUUID()}")
        }

        fun anonymousUID(): UID {
            return UID("[ANONYMOUS DEFAULT UID] ${UUID.randomUUID()}")
        }

        fun systemUID(): UID {
            return UID("[SYSTEM DEFAULT UID] ${UUID.randomUUID()}")
        }
    }
}
