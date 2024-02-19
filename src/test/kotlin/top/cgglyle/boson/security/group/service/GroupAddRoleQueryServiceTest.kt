package top.cgglyle.boson.security.group.service

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import top.cgglyle.boson.security.account.AccountFindable
import top.cgglyle.boson.security.authorization.RoleFindable
import top.cgglyle.boson.security.common.UID
import top.cgglyle.boson.security.exception.DataNotFoundException
import top.cgglyle.boson.security.group.GID
import top.cgglyle.boson.security.group.domain.entity.GroupRepository


@ExtendWith(MockKExtension::class)
class GroupAddRoleQueryServiceTest {
    @InjectMockKs
    lateinit var groupService: GroupService

    @MockK(relaxed = true)
    lateinit var accountFindable: AccountFindable

    @MockK
    lateinit var groupRepository: GroupRepository

    @MockK
    lateinit var roleFindable: RoleFindable

    @Test
    fun findAllGroup() {
    }

    @Test
    fun findGroup() {
    }

    @Test
    fun createGroup() {
    }

    @Test
    fun removeGroup() {
    }

    @Test
    fun addAccount() {
        // given
        val gid = mockk<GID>()
        val uid = mockk<UID>()

        every { accountFindable.existsOrThrowException(any()) }.returns(Unit)
        every { groupRepository.findByGid(any()) }.returns(mockk(relaxed = true))

        // when
        assertDoesNotThrow {
            groupService.addAccount(gid, uid)
        }
    }

    @Test
    fun addAccount_AccountMustExits() {
        // given
        val gid = mockk<GID>()
        val uid = mockk<UID>()

        every { accountFindable.existsOrThrowException(any()) }.throws(DataNotFoundException())
        every { groupRepository.findByGid(any()) }.returns(mockk(relaxed = true))

        // when
        assertThrows<DataNotFoundException> {
            groupService.addAccount(gid, uid)
        }
    }

    @Test
    fun addAccount_GroupMustExits() {
        // given
        val gid = mockk<GID>()
        val uid = mockk<UID>()

        every { accountFindable.existsOrThrowException(any()) }.returns(Unit)
        every { groupRepository.findByGid(any()) }.returns(null)

        // when
        assertThrows<DataNotFoundException> {
            groupService.addAccount(gid, uid)
        }
    }

    @Test
    fun deletedAccount() {
    }
}