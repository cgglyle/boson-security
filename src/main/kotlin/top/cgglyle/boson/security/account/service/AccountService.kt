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

package top.cgglyle.boson.security.account.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import top.cgglyle.boson.security.account.*
import top.cgglyle.boson.security.account.domain.Account
import top.cgglyle.boson.security.account.domain.AccountRepository
import top.cgglyle.boson.security.common.UID
import top.cgglyle.boson.security.common.UsernameFindable
import top.cgglyle.boson.security.exception.DataNotFoundException
import top.cgglyle.boson.security.exception.IllegalArgumentException

/**
 * @author: Lyle Liu
 */
@Service
class AccountService(
    private val accountRepository: AccountRepository
) : AccountFindable, AccountManager, UsernameFindable {
    @Transactional
    override fun findAllAccount(pageable: Pageable): Page<AccountDto> {
        return accountRepository.findAll(pageable).map { it.toAccountDto() }
    }

    override fun findByUsername(username: String): AccountDto? {
        val account = accountRepository.findByUsername(username)
        return account?.toAccountDto()
    }

    override fun findByUid(uid: UID): AccountDto? {
        val account = accountRepository.findByUid(uid)
        return account?.toAccountDto()
    }

    override fun existUid(uid: UID): Boolean {
        return accountRepository.existsByUid(uid)
    }

    override fun existUsername(username: String): Boolean {
        return existsByUsernameOrEmail(username, username)
    }

    override fun existsByUsernameOrEmail(username: String?, email: String?): Boolean {
        return accountRepository.existsByUsernameOrEmail(username, email)
    }

    override fun existsOrThrowException(uid: UID) =
        if (existUid(uid)) Unit else throw DataNotFoundException("Uid: $uid not found!")

    override fun save(accountDto: CreateAccountDto): UID {
        val newAccount = if (accountDto.uid == null) {
            createAccount(accountDto)
        } else createAccountUseUid(accountDto)
        val account = accountRepository.save(newAccount)
        return account.uid
    }

    override fun delete(uid: UID) {
        accountRepository.deleteByUid(uid)
    }

    override fun findNameByUid(uid: UID): String? {
        return findByUid(uid)?.username
    }

    private fun createAccount(accountDto: CreateAccountDto): Account {
        return Account(
            CreateAccountCommand(
                accountDto.username,
                accountDto.email,
                accountDto.roles,
                accountDto.expiration,
                accountDto.locked,
                accountDto.enable
            )
        )
    }

    private fun createAccountUseUid(accountDto: CreateAccountDto): Account {
        if (accountDto.uid == null) {
            throw IllegalArgumentException("uid must not be null!")
        }
        return Account(
            CreateAccountCommand(
                accountDto.username,
                accountDto.email,
                accountDto.roles,
                accountDto.expiration,
                accountDto.locked,
                accountDto.enable
            ),
            accountDto.uid,
        )
    }
}