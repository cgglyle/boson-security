package top.cgglyle.boson.security.web

import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import top.cgglyle.boson.security.service.UserService
import top.cgglyle.boson.security.web.query.CreateUserQuery

@RestController
@RequestMapping("auth")
class AuthController(
    val userService: UserService
) {

    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun createUser(@RequestBody query: CreateUserQuery) {
        userService.createUser(query)
    }
}