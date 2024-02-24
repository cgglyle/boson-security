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

import jakarta.persistence.PostLoad
import org.springframework.beans.factory.ObjectFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Configurable

@Configurable
class AuditingUsernameListener {
    @Autowired
    lateinit var objectFactory: ObjectFactory<UsernameFindable>

    @PostLoad
    fun injectCreatedUsernameTo(entity: AbstractAuditingEntity) {
        val usernameFindable = objectFactory.`object`
        val createdUid = entity.createdBy
        val username = usernameFindable.findNameByUid(createdUid) ?: "Unknown uid: '$createdUid'"
        entity.createdUsername = username

        if (entity is AbstractModifiedAuditingEntity) {
            val lastModifiedUid = entity.lastModifiedBy
            val lastModifiedUsername =
                lastModifiedUid?.let { usernameFindable.findNameByUid(it) ?: "Unknown uid: '$it'" }
            entity.lastModifiedUsername = lastModifiedUsername
        }
    }
}