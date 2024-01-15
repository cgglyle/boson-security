package top.cgglyle.boson.security.common.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail

open class SystemException(
    status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
    body: ProblemDetail = ProblemDetail.forStatus(status),
    cause: Throwable? = null,
    messageDetailCode: String? = null,
    messageDetailArguments: Array<out Any?>? = null
) : BasicException(status, body, cause, messageDetailCode, messageDetailArguments) {
    constructor(
        messageDetail: String,
        status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
        cause: Throwable? = null
    ) : this(
        status,
        ProblemDetail.forStatusAndDetail(status, messageDetail),
        cause
    )
}