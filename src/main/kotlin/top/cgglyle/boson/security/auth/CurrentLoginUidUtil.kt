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

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import top.cgglyle.boson.security.common.UID
import top.cgglyle.boson.security.exception.DataNotFoundException

class CurrentLoginUidUtil private constructor() {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(CurrentLoginUidUtil::class.java)
        fun getCurrentLoginUid(): UID {
            val securityContext = SecurityContextHolder.getContext()
            val authentication = securityContext.authentication
            isAuthentication(authentication)
            return if (isAnonymousUser(authentication)) {
                getAnonymousUid(authentication)
            } else {
                val uidDetailUser = getUidDetailUser(authentication)
                getUid(uidDetailUser)
            }
        }

        private fun isAuthentication(authentication: Authentication?) {
            if (authentication == null) {
                throw DataNotFoundException("Current caller is not authentication.")
            }
        }

        private fun isAnonymousUser(authentication: Authentication): Boolean =
            authentication is AnonymousAuthenticationToken

        private fun getAnonymousUid(authentication: Authentication): UID {
            return if (authentication is SystemAuthenticationToken) {
                val (uid) = authentication.principal as UidDetailUser
                return uid
            } else if (authentication is AnonymousAuthenticationToken) {
                UID(authentication.name + authentication.keyHash)
            } else {
                throw DataNotFoundException("")
            }
        }

        private fun getUidDetailUser(authentication: Authentication) = authentication.principal as UidDetailUser

        private fun getUid(uidDetailUser: UidDetailUser) = uidDetailUser.uid

        fun newSystemAuthenticationToken(): SystemAuthenticationToken {
            val systemUserDetail = createSystemUserDetail()
            return SystemAuthenticationToken(
                systemUserDetail.hashCode().toString(), systemUserDetail, systemUserDetail.authorities
            )
        }

        private fun createSystemUserDetail(): UidDetailUser {
            val userDetails =
                User.withUsername("System User").password("").authorities(SimpleGrantedAuthority("SYSTEM")).build()
            return UidDetailUser(UID.systemUID(), userDetails)
        }

    }
}