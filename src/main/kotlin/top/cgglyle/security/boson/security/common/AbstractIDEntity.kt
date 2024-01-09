package top.cgglyle.security.boson.security.common

import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass

@MappedSuperclass
abstract class AbstractIDEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "database_id", nullable = false, updatable = false)
    val id: Long = -1
){
    override fun toString(): String {
        return "AbstractIDEntity(id='$id')"
    }
}