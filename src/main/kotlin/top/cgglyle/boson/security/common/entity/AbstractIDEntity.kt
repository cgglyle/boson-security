package top.cgglyle.boson.security.common.entity

import jakarta.persistence.*

@MappedSuperclass
abstract class AbstractIDEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "database_id", nullable = false, updatable = false)
    val id: Long = -1
) {
    @Version
    @Column(name = "version")
    var version: Int? = null
        protected set

    override fun toString(): String {
        return "AbstractIDEntity(id='$id')"
    }
}