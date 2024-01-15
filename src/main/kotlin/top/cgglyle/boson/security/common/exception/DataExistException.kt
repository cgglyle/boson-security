package top.cgglyle.boson.security.common.exception

import org.springframework.http.HttpStatus

class DataExistException(
    messageDetail: String = "Data Exist!",
    cause: Throwable? = null,
    httpStatus: HttpStatus = HttpStatus.NOT_FOUND
) : ClientException(messageDetail, httpStatus, cause) {
    init {
        setTitle("Data Exist")
    }
}