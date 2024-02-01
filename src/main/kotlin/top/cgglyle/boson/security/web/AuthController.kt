package top.cgglyle.boson.security.web

import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import top.cgglyle.boson.security.account.CreateAccountQuery
import top.cgglyle.boson.security.auth.AuthUserManager
import top.cgglyle.boson.security.common.UID
import top.cgglyle.boson.security.exception.IllegalArgumentException
import top.cgglyle.boson.security.web.query.LoginQuery
import top.cgglyle.boson.security.web.query.RegisterQuery

@RestController
@RequestMapping("/api")
@Validated
class AuthController(
    val authUserManager: AuthUserManager,
) {

    @PostMapping("register")
    fun createUser(@Valid @RequestBody query: RegisterQuery): UID {
        if (query.email == null && query.username == null) throw IllegalArgumentException("Both username and password must not be null!")
        return authUserManager.createUser(
            CreateAccountQuery(
                query.username, query.email, query.password, setOf()
            )
        )
    }

    @PostMapping(path = ["login"], consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun login(@Valid query: LoginQuery) {
    }

    @PostMapping("logout")
    fun logout() {
    }

    @GetMapping("userinfo")
    fun getCurrentLoginUserDetails(): UserDetails {
        return SecurityContextHolder.getContext().authentication.principal as UserDetails
    }
}