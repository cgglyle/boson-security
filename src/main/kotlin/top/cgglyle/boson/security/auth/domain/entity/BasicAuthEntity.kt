package top.cgglyle.boson.security.auth.domain.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.MappedSuperclass
import top.cgglyle.boson.security.common.entity.AbstractModifiedAuditingEntity
import top.cgglyle.boson.security.user.domain.entity.Account

@MappedSuperclass
abstract class BasicAuthEntity(
    @ManyToOne(
        cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH],
        optional = false
    )
    @JoinColumn(name = "account_database_id", nullable = false, unique = true, updatable = false)
    val account: Account
) : AbstractModifiedAuditingEntity()