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

import jakarta.persistence.AttributeOverride
import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class, AuditingUsernameListener::class)
abstract class AbstractAuditingEntity(
    @CreatedBy
    @AttributeOverride(
        name = "value",
        column = Column(name = "created_by", updatable = false, nullable = false)
    )
    var createdBy: UID = UID.defaultUID(),


    @CreatedDate
    @Column(name = "created_date", updatable = false, nullable = false)
    var createdDate: Instant = Instant.now()
) : AbstractIDEntity() {
    @jakarta.persistence.Transient
    var createdUsername: String = "NULL"
}