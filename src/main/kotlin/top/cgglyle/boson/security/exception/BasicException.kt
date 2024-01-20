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

package top.cgglyle.boson.security.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.ErrorResponseException

abstract class BasicException(
    status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
    body: ProblemDetail = ProblemDetail.forStatus(status),
    cause: Throwable? = null,
    messageDetailCode: String? = null,
    messageDetailArguments: Array<out Any?>? = null
) : ErrorResponseException(status, body, cause, messageDetailCode, messageDetailArguments)