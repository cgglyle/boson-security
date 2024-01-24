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

package top.cgglyle.boson.security.auth.handler

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import top.cgglyle.boson.security.exception.ClientException
import java.net.URI


/**
 * @author: Lyle Liu
 */
class DefaultAccessDeniedHandler(private val objectMapper: ObjectMapper) : AccessDeniedHandler {
    override fun handle(
        request: HttpServletRequest, response: HttpServletResponse, accessDeniedException: AccessDeniedException?
    ) {
        if (response.isCommitted) {
            return
        }

        response.status = HttpStatus.FORBIDDEN.value()
        val clientException = ClientException(
            accessDeniedException?.message ?: "Forbidden", HttpStatus.FORBIDDEN, accessDeniedException
        )
        clientException.setInstance(URI.create(request.requestURI))
        val responseValue = objectMapper.writeValueAsString(clientException.body)
        response.contentType = "application/problem+json"
        response.characterEncoding = "UTF-8"
        response.writer.append(responseValue)
    }
}