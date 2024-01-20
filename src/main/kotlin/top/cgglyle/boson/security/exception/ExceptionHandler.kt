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

import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

/**
 * @author: Lyle Liu
 */
@RestControllerAdvice
class ExceptionHandler() : ResponseEntityExceptionHandler() {

    @ExceptionHandler(ConstraintViolationException::class)
    fun constraintViolationException(e: ConstraintViolationException, request: WebRequest): ProblemDetail {
        val constraintViolations = e.constraintViolations
        val messages = constraintViolations.joinToString { "${it.propertyPath}: ${it.message}" }
        return createProblemDetail(
            e,
            HttpStatus.BAD_REQUEST,
            messages,
            null,
            null,
            request
        )
    }

    @ExceptionHandler(Exception::class)
    fun undefinedException(e: Exception, request: WebRequest): ProblemDetail {
        return createProblemDetail(
            e,
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Oops! System Error.",
            null,
            null,
            request
        )
    }
}