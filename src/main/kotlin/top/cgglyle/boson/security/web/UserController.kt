package top.cgglyle.boson.security.web

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import top.cgglyle.boson.security.user.domain.entity.Account
import top.cgglyle.boson.security.user.domain.entity.AccountRepository

@RestController
@RequestMapping("/api/users")
class UserController(
    private val accountRepository: AccountRepository,
) {
//    @GetMapping()
//    fun getAllUserinfo(): Page<Profile> {
//        val page = userRepository.findPageBy()
//        return page
//    }

    @GetMapping("{id:\\d+}")
    fun getUserinfoById(@PathVariable id: String): Account? {
        val userEntityOperation = accountRepository.findById(id)
        if (userEntityOperation.isEmpty) {
            return null
        }
        val userEntity = userEntityOperation.get()
//        return userEntityMapper.toDto(userEntity)
        return userEntity
    }


//    @PostMapping
//    fun addUser(@Valid @RequestBody query: CreateUserQuery): UserinfoView {
//        return userEntityMapper.toDto(userService.createUser(query))
//    }
}