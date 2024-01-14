package top.cgglyle.boson.security.web.query

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateUserQuery(
    @NotBlank
    @Max(64)
    val username: String,
    @NotBlank
    @Size(min = 4, max = 128)
    val password: String,
    val roleNames: Set<String>,
)
