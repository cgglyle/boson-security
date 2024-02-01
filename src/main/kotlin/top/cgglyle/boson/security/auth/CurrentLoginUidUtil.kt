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

import jakarta.transaction.SystemException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import top.cgglyle.boson.security.common.UID

class CurrentLoginUidUtil private constructor() {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(CurrentLoginUidUtil::class.java)
        private val local: ThreadLocal<UID> = ThreadLocal()
        fun getCurrentLoginUid(): UID {
            val authentication = SecurityContextHolder.getContext().authentication
            if (authentication == null) {
                val systemUID = getLocalUid() ?: UID.systemUID()
                logger.info("Current Security Authentication is null, current user maybe is SYSTEM Operation. '$systemUID'")
                setLocalUid(systemUID)
                return systemUID
            }
            if (authentication.isAuthenticated) {
                val principal = authentication.principal
                if (principal is UidDetailUser) {
                    return principal.uid
                }
                if (principal is String && principal.equals("anonymousUser") && authentication.authorities.contains(
                        SimpleGrantedAuthority("ROLE_ANONYMOUS")
                    )
                ) {
                    val uid = getLocalUid() ?: UID.anonymousUID()
                    setLocalUid(uid)
                    return uid
                }
            } else {
                val uid = getLocalUid() ?: UID.anonymousUID()
                setLocalUid(uid)
                return uid
            }
            throw SystemException("Get Security Info Error!")
        }

        private fun setLocalUid(uid: UID) {
            if (local.get() == null) {
                local.set(uid)
            }
        }

        private fun getLocalUid(): UID? = local.get()
    }

}