package top.cgglyle.boson.security.web

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import top.cgglyle.boson.security.service.UserService
import top.cgglyle.boson.security.web.query.CreateUserQuery
import top.cgglyle.boson.security.web.query.LoginQuery

@RestController
@RequestMapping("/api")
@Validated
class AuthController(
    val userService: UserService
) {

    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("register")
    fun createUser(@RequestBody query: CreateUserQuery) {
        userService.createUser(query)
    }

    @PostMapping(path = ["login"], consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun login(@Valid query: LoginQuery) {
    }

    @PostMapping("logout")
    fun logout() {}
}