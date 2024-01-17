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

package top.cgglyle.boson.security.web

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import top.cgglyle.boson.security.user.domain.entity.Account
import top.cgglyle.boson.security.user.domain.entity.AccountRepository
import java.lang.RuntimeException

@RestController
@RequestMapping("/api/users")
class AccountController(
    private val accountRepository: AccountRepository,
) {
//    @GetMapping()
//    fun getAllUserinfo(): Page<Profile> {
//        val page = userRepository.findPageBy()
//        return page
//    }

    @GetMapping("{id:\\d+}")
    fun getUserinfoById(@PathVariable id: String): Account? {
        val userEntityOperation = accountRepository.findById(id)
        if (userEntityOperation.isEmpty) {
            return null
        }
        val userEntity = userEntityOperation.get()
//        return userEntityMapper.toDto(userEntity)
        return userEntity
    }


//    @PostMapping
//    fun addUser(@Valid @RequestBody query: CreateUserQuery): UserinfoView {
//        return userEntityMapper.toDto(userService.createUser(query))
//    }
}