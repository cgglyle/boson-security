package top.cgglyle.boson.security.common.entity.basic

import jakarta.persistence.MappedSuperclass

@MappedSuperclass
abstract class AbstractValueEntity(): AbstractIDEntity() {
}