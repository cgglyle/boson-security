package top.cgglyle.boson.security.common.exception

class IllegalArgumentException(
    messageDetail: String = "Illegal argument!",
) : ClientException(messageDetail) {
    init {
        setTitle("Illegal Argument")
    }
}