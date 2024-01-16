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

package top.cgglyle.boson.security.auth.domain.entity

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import top.cgglyle.boson.security.common.entity.basic.AbstractIDRepository

interface LocalAuthEntityRepository : AbstractIDRepository<LocalAuthEntity> {

    @Query("SELECT u FROM LocalAuthEntity u WHERE u.account.username = :username OR u.account.email = :username")
    fun findByUsername(@Param("username") username: String): LocalAuthEntity?


    @Query(
        "select (count(l) > 0) from LocalAuthEntity l where upper(l.account.username) = upper(:username) or upper(l.account.email) = upper(:username)"
    )
    fun userExists(@Param("username") username: String): Boolean
}