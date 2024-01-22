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

package top.cgglyle.boson.security.authentication.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import top.cgglyle.boson.security.authentication.AuthenticationFindable
import top.cgglyle.boson.security.authentication.AuthenticationManager
import top.cgglyle.boson.security.authentication.CreateAuthDto
import top.cgglyle.boson.security.authentication.LocalAuthDto
import top.cgglyle.boson.security.authentication.domain.LocalAuthEntity
import top.cgglyle.boson.security.authentication.domain.LocalAuthEntityRepository
import top.cgglyle.boson.security.common.PasswordEntity
import top.cgglyle.boson.security.common.UID
import top.cgglyle.boson.security.exception.DataNotFoundException

/**
 * @author: Lyle Liu
 */
@Service
class AuthenticationService(
    private val localAuthEntityRepository: LocalAuthEntityRepository,
    private val passwordEncoder: PasswordEncoder,
) : AuthenticationFindable, AuthenticationManager {
    override fun getAuthenticationInfo(uid: UID): LocalAuthDto {
        val localAuth = getLocalAuth(uid)
        return with(localAuth) {
            LocalAuthDto(
                id,
                createdBy,
                createdDate,
                lastModifiedBy,
                lastModifiedDate,
                uid,
                getPassword(),
                isCredentialsExpired()
            )
        }
    }

    override fun create(auth: CreateAuthDto) {
        val passwordEntity = PasswordEntity(auth.password, passwordEncoder)
        val localAuthEntity = LocalAuthEntity(auth.uid, passwordEntity)
        localAuthEntity.setExpiration(auth.expiration)
        localAuthEntityRepository.save(localAuthEntity)
    }

    override fun delete(uid: UID) {
        localAuthEntityRepository.deleteByUid(uid)
    }

    override fun addPassword(uid: UID, newPassword: String) {
        val localAuth = getLocalAuth(uid)
        localAuth.addPassword(newPassword, passwordEncoder)
    }


    protected fun getLocalAuth(uid: UID): LocalAuthEntity = localAuthEntityRepository.findByUid(uid)
        ?: throw DataNotFoundException("uid: '$uid' Local authentication not found!")
}