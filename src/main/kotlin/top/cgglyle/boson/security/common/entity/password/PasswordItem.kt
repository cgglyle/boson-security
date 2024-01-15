package top.cgglyle.boson.security.common.entity.password

import jakarta.persistence.*
import org.hibernate.proxy.HibernateProxy
import top.cgglyle.boson.security.common.entity.basic.AbstractValueEntity

@Entity
@Table(name = "sys_password_item")
class PasswordItem(
    @Column(name = "password", nullable = false, updatable = false)
    val password: String,
    @ManyToOne
    @JoinColumn(name = "password_entity_database_id")
    private val passwordEntity: PasswordEntity,
) : AbstractValueEntity() {
    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        val oEffectiveClass =
            if (other is HibernateProxy) other.hibernateLazyInitializer.persistentClass else other.javaClass
        val thisEffectiveClass =
            if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass else this.javaClass
        if (thisEffectiveClass != oEffectiveClass) return false
        other as PasswordItem

        return id == other.id && password == other.password
    }

    final override fun hashCode(): Int =
        if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass.hashCode() else javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , password = $password)"
    }
}