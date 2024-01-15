package top.cgglyle.boson.security.utils

import jakarta.transaction.SystemException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import top.cgglyle.boson.security.auth.domain.entity.BasicLocalAuthEntity
import top.cgglyle.boson.security.user.domain.entity.UID

class CurrentLoginUidUtil private constructor() {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(CurrentLoginUidUtil::class.java)
        fun getCurrentLoginUid(): UID {
            val authentication = SecurityContextHolder.getContext().authentication
            if (authentication == null) {
                logger.info("Security authentication is null, current user maybe is SYSTEM")
                return UID.systemUID()
            }
            if (authentication.isAuthenticated) {
                val details = authentication.details
                if (details is BasicLocalAuthEntity) {
                    return details.account.uid
                }
            } else {
                return UID.anonymousUID()
            }
            throw SystemException("Get Security Info Error!")
        }
    }
}