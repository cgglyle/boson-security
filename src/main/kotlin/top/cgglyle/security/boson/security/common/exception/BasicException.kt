package top.cgglyle.security.boson.security.common.exception

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