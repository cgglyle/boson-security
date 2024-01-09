package top.cgglyle.security.boson.security.web.query

data class CreateUserQuery(
    val username: String,
    val password: String,
    val roleNames: Set<String>,
) {

}
