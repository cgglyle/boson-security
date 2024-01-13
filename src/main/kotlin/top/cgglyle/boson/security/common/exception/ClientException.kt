package top.cgglyle.boson.security.common.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail

open class ClientException(
    status: HttpStatus = HttpStatus.BAD_REQUEST,
    body: ProblemDetail = ProblemDetail.forStatus(status),
    cause: Throwable? = null,
    messageDetailCode: String? = null,
    messageDetailArguments: Array<out Any?>? = null
) : BasicException(status, body, cause, messageDetailCode, messageDetailArguments) {
    constructor(messageDetail: String, status: HttpStatus = HttpStatus.BAD_REQUEST, cause: Throwable? = null) : this(
        status,
        ProblemDetail.forStatusAndDetail(status, messageDetail),
        cause
    )
}