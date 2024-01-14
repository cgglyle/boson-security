package top.cgglyle.boson.security.auth.domain

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import top.cgglyle.boson.security.common.entity.AbstractIDRepository
import top.cgglyle.boson.security.auth.domain.entity.LocalAuthEntity

interface LocalAuthRepository : AbstractIDRepository<LocalAuthEntity> {
    fun findByAccountUsername(username: String): LocalAuthEntity?

    @Query("select u from LocalAuthEntity u where (:username is null or u.account.username like %:username%)")
    fun findPageBy(
        @Param("username") username: String? = null,
        pageable: Pageable = PageRequest.of(0, 10)
    ): Page<LocalAuthEntity>
}