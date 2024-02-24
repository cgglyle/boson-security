package top.cgglyle.boson.security.web

import jakarta.validation.Valid
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.security.core.CredentialsContainer
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import top.cgglyle.boson.security.account.CreateAccountQuery
import top.cgglyle.boson.security.auth.AuthUserManager
import top.cgglyle.boson.security.auth.OnceAuthenticationToken
import top.cgglyle.boson.security.auth.UidDetailUser
import top.cgglyle.boson.security.common.UID
import top.cgglyle.boson.security.exception.IllegalArgumentException
import top.cgglyle.boson.security.web.query.LoginQuery
import top.cgglyle.boson.security.web.query.RegisterQuery
import java.security.Principal

@RestController
@RequestMapping("/api")
@Validated
class AuthController(
    val authUserManager: AuthUserManager,
) {

    @PostMapping("register")
    fun createUser(@Valid @RequestBody query: RegisterQuery, principal: Principal?): UID {
        if (query.email == null && query.username == null) throw IllegalArgumentException("Both username and password must not be null!")
        val uid: UID? = if (principal == null) {
            logger.info("[Register User] Current login user is null!")
            val randomUID = UID.randomUID()
            createOnceAuthenticationToken(query.username!!, randomUID)
            randomUID
        } else null
        return authUserManager.createUser(
            CreateAccountQuery(
                query.username, query.email, query.password, setOf(), uid = uid,
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
        val userDetails = SecurityContextHolder.getContext().authentication.principal as UserDetails
        val credentialsContainer = userDetails as CredentialsContainer
        credentialsContainer.eraseCredentials()
        return userDetails
    }

    private fun createOnceAuthenticationToken(username: String, uid: UID) {
        val userDetails = User.withUsername(username)
            .password("")
            .authorities(SimpleGrantedAuthority("USER"))
            .build()
        val uidDetails = UidDetailUser(uid, userDetails)
        val onceAuthenticationToken = OnceAuthenticationToken(uidDetails)
        val contextHolderStrategy = SecurityContextHolder.getContextHolderStrategy()
        contextHolderStrategy.context.authentication = onceAuthenticationToken
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(AuthController::class.java)
    }
}