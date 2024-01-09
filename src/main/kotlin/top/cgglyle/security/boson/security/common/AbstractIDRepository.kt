package top.cgglyle.security.boson.security.common

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface AbstractIDRepository<T> : JpaRepository<T, String> {
}