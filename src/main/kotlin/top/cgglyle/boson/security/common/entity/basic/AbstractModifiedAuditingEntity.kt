package top.cgglyle.boson.security.common.entity.basic

import jakarta.persistence.AttributeOverride
import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import top.cgglyle.boson.security.user.domain.entity.UID
import java.time.Instant

@MappedSuperclass
abstract class AbstractModifiedAuditingEntity : AbstractAuditingEntity() {
    @LastModifiedBy
    @AttributeOverride(name = "value", column = Column(name = "last_modified_by"))
    var lastModifiedBy: UID? = null

    @LastModifiedDate
    @Column(name = "last_modified_date")
    var lastModifiedDate: Instant? = null
}