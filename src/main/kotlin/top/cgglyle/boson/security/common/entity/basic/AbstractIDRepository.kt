package top.cgglyle.boson.security.common.entity.basic

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface AbstractIDRepository<T> : JpaRepository<T, String> {
}