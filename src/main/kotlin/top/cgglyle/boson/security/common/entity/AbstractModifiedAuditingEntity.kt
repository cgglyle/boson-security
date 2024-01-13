package top.cgglyle.boson.security.common.entity

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant

@MappedSuperclass
abstract class AbstractModifiedAuditingEntity: AbstractAuditingEntity() {
    @LastModifiedBy
    @Column(name = "last_modified_by")
    var lastModifiedBy: Long? = null

    @LastModifiedDate
    @Column(name = "last_modified_date")
    var lastModifiedDate: Instant? = null
}