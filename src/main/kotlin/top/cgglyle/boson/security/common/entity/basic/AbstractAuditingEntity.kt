package top.cgglyle.boson.security.common.entity.basic

import jakarta.persistence.AttributeOverride
import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import top.cgglyle.boson.security.user.domain.entity.UID
import java.time.Instant

@MappedSuperclass
abstract class AbstractAuditingEntity(
    @CreatedBy
    @AttributeOverride(name = "value", column = Column(name = "created_by", updatable = false, nullable = false))
    val createdBy: UID = UID.defaultUID(),

    @CreatedDate
    @Column(name = "created_date", updatable = false, nullable = false)
    val createdDate: Instant? = Instant.now()
) : AbstractIDEntity() {

}