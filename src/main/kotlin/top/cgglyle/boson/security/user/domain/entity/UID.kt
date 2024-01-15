package top.cgglyle.boson.security.user.domain.entity

import jakarta.persistence.Embeddable
import java.io.Serializable
import java.util.UUID

@Embeddable
data class UID(
    val value: String
) : Serializable {
    companion object {
        fun randomUID(): UID {
            return UID(UUID.randomUUID().toString())
        }

        fun defaultUID(): UID {
            return UID("[UNINITIALIZED DEFAULT UID] ${UUID.randomUUID()}")
        }

        fun anonymousUID(): UID {
            return UID("[ANONYMOUS DEFAULT UID] ${UUID.randomUUID()}")
        }

        fun systemUID(): UID {
            return UID("[SYSTEM UID] ${UUID.randomUUID()}")
        }
    }
}
