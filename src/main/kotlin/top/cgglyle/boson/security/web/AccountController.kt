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

import org.springframework.data.domain.PageRequest
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import top.cgglyle.boson.security.account.AccountDto
import top.cgglyle.boson.security.account.AccountFindable
import kotlin.math.max

@RestController
@RequestMapping("/api/account")
class AccountController(
    private val accountFindable: AccountFindable,
) {

    @Transactional
    @GetMapping
    fun getAllAccount(
        pageNumber: Int = 0,
        pageSize: Int = 10,
    ): WebPage<AccountDto> {
        val accountDtoPage = accountFindable.findAllAccount(PageRequest.of(max(0, pageNumber - 1), pageSize))
        return WebPage.form(accountDtoPage)
    }

}