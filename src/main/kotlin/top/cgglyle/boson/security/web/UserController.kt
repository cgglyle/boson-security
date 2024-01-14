package top.cgglyle.boson.security.web

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import top.cgglyle.boson.security.auth.domain.entity.LocalAuthEntity
import top.cgglyle.boson.security.auth.domain.LocalAuthRepository
import top.cgglyle.boson.security.service.UserService

@RestController
@RequestMapping("/api/users")
class UserController(
    private val localAuthRepository: LocalAuthRepository,
    private val userService: UserService,
) {
//    @GetMapping()
//    fun getAllUserinfo(): Page<Profile> {
//        val page = userRepository.findPageBy()
//        return page
//    }

    @GetMapping("{id:\\d+}")
    fun getUserinfoById(@PathVariable id: String): LocalAuthEntity? {
        val userEntityOperation = localAuthRepository.findById(id)
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