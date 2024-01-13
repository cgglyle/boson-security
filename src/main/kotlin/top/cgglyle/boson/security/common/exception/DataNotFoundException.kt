package top.cgglyle.boson.security.common.exception

import org.springframework.http.HttpStatus

class DataNotFoundException(
    messageDetail: String = "Data not found!",
    cause: Throwable? = null,
    httpStatus: HttpStatus = HttpStatus.NOT_FOUND
) : ClientException(messageDetail, httpStatus, cause) {
    init {
        setTitle("Data Not Found")
    }
}