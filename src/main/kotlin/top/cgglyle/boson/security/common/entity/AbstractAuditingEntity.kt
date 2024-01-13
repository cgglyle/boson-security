package top.cgglyle.boson.security.common.entity

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import java.time.Instant

@MappedSuperclass
abstract class AbstractAuditingEntity(
    @CreatedBy
    @Column(name = "created_by", updatable = false, nullable = false)
    val createdBy: Long = -1,

    @CreatedDate
    @Column(name = "created_date", updatable = false, nullable = false)
    val createdDate: Instant? = Instant.now()
) : AbstractIDEntity() {

}