package top.cgglyle.boson.security.web.query

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.NotBlank

data class LoginQuery(
    @NotBlank
    @Max(64)
    val username: String,
    @NotBlank
    @Max(128)
    val password: String
)