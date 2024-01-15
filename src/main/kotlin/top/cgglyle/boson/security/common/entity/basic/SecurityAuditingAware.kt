package top.cgglyle.boson.security.common.entity.basic

import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.stereotype.Component
import top.cgglyle.boson.security.user.domain.entity.UID
import top.cgglyle.boson.security.utils.CurrentLoginUidUtil
import java.util.*

@EnableJpaAuditing
@Component
class SecurityAuditingAware : AuditorAware<UID> {
    override fun getCurrentAuditor(): Optional<UID> {
        return Optional.of(CurrentLoginUidUtil.getCurrentLoginUid())
    }
}