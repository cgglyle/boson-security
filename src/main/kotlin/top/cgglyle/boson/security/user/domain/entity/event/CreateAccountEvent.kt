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

package top.cgglyle.boson.security.user.domain.entity.event

import org.slf4j.LoggerFactory
import top.cgglyle.boson.security.common.entity.basic.DomainEvent
import top.cgglyle.boson.security.user.domain.entity.Account

/**
 * @author: Lyle Liu
 */
data class CreateAccountEvent(
    val account: Account
): DomainEvent() {
    private val logger = LoggerFactory.getLogger(javaClass)
    init {
       logger.info("[Event] $this")
    }

    override fun toString(): String {
        return "CreateAccountEvent(account=$account) ${super.toString()}"
    }
}