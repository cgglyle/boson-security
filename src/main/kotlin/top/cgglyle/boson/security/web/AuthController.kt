package top.cgglyle.boson.security.web

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import top.cgglyle.boson.security.account.CreateAccountQuery
import top.cgglyle.boson.security.auth.AuthUserManager
import top.cgglyle.boson.security.common.UID
import top.cgglyle.boson.security.web.query.LoginQuery

@RestController
@RequestMapping("/api")
@Validated
class AuthController(
    val authUserManager: AuthUserManager,
) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("register")
    fun createUser(@RequestBody query: CreateAccountQuery): UID {
        return authUserManager.createUser(query)
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